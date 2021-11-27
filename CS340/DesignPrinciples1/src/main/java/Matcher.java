/*
1. What design principles does this code violate?
    There is a lack of decomposition, so Single Responsibility Principle
2. Refactor the code to improve its design.

        Matcher.java

 */

public class Matcher {
    public Matcher() {}
    public boolean match(int[] expected, int[] actual, int clipLimit, int delta) {
        int[] clone = actual.clone();
        clipArray(clone, clipLimit);

        // Check for length differences
        if (clone.length != expected.length)
            return false;

        return isWithinBounds(expected, clone, delta);
    }

    private void clipArray(int[] actual, int clipLimit) {
        // Clip "too-large" values
        for (int i = 0; i < actual.length; i++) {
            if (actual[i] > clipLimit) {
                actual[i] = clipLimit;
            }
        }
    }

    private boolean isWithinBounds(int[] expected, int[] actual, int delta) {
        // Check that each entry within expected +/- delta
        for (int i = 0; i < actual.length; i++) {
            if (Math.abs(expected[i] - actual[i]) > delta) {
                return false;
            }
        }
        return true;
    }
}
