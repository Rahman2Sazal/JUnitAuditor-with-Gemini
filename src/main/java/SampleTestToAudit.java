import org.junit.jupiter.api.Test;

public class SampleTestToAudit {

    @Test
    public void testSomethingGood() {
        int result = 2 + 2;
        org.junit.jupiter.api.Assertions.assertEquals(4, result); // Has assertion!
    }

    @Test
    public void testLazyAiHack() {
        // This is a classic lazy AI test—it runs code but checks nothing!
        int result = 5 * 5;
        System.out.println("Result is " + result);
    }
}