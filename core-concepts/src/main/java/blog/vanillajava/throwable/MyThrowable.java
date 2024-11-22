package blog.vanillajava.throwable;

/**
 * A custom Throwable class used to represent a specific error condition
 * that is neither an Exception nor an Error. This demonstrates that
 * Throwable can be directly extended, though it's a rare use case.
 */
public class MyThrowable extends Throwable {
    /**
     * Constructs a MyThrowable with a specific message.
     *
     * @param message the detail message.
     */
    public MyThrowable(String message) {
        super(message);
    }

    /**
     * Demonstrates the behaviour of MyThrowable.
     *
     * @param args command-line arguments (not used).
     * @throws MyThrowable if a custom error condition is met.
     */
    public static void main(String... args) throws MyThrowable {
        // Simulate a condition where MyThrowable is thrown
        checkCondition(false);
    }

    /**
     * Checks a condition and throws MyThrowable if the condition is false.
     *
     * @param condition a boolean condition to check.
     * @throws MyThrowable if the condition is false.
     */
    private static void checkCondition(boolean condition) throws MyThrowable {
        if (!condition) {
            throw new MyThrowable("Condition was false");
        }
        System.out.println("Condition passed");
    }
}
