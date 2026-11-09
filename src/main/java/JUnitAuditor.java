import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileNotFoundException;

public class JUnitAuditor {

    // Our Visitor class that walks through the Java file syntax tree
    public static class TestVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(MethodDeclaration md, Void arg) {
            super.visit(md, arg);

            // Check if the method has the @Test annotation
            boolean hasTestAnnotation = md.getAnnotations().stream()
                    .anyMatch(a -> a.getNameAsString().equals("Test"));

            if (hasTestAnnotation) {
                // Check if the method calls any assertion (e.g., assertEquals, assertTrue)
                boolean hasAssertion = md.findAll(MethodCallExpr.class).stream()
                        .anyMatch(mc -> mc.getNameAsString().startsWith("assert"));

                if (!hasAssertion) {
                    System.out.println("❌ ALERT: Trivial/Empty test found in method: " + md.getNameAsString());
                } else {
                    System.out.println("✅ PASSED: Valid test found in method: " + md.getNameAsString());
                }
            }
        }
    }

    public static void main(String[] args) {
        // Path to the test file we want to audit
        File fileToAudit = new File("src/main/java/SampleTestToAudit.java");

        try {
            System.out.println("Auditing file: " + fileToAudit.getName() + "\n");
            CompilationUnit cu = StaticJavaParser.parse(fileToAudit);

            // Run our visitor across the parsed file
            TestVisitor visitor = new TestVisitor();
            visitor.visit(cu, null);

        } catch (FileNotFoundException e) {
            System.out.println("Could not find file: " + fileToAudit.getPath());
        }
    }
}