package blog.vanillajava;

import java.util.Arrays;

/**
 * Based on the post <a href="https://blog.vanillajava.blog/2024/11/some-common-java-gotchas.html">some-common-java-gotchas.html</a> by Peter Lawrey.
 */

public class CommonJavaGotchas {

    public static void main(String[] args) {
        variablesAreReferencesOrPrimitives();
        referenceComparison();
        passReferencesByValue();
        hashCodeAndToStringSurprises();
    }

    /**
     * Illustrates that variables are references or primitives, not objects.
     */
    private static void variablesAreReferencesOrPrimitives() {
        String s = "Hello"; // 's' is a reference to the String "Hello", not the object itself
        String t = s;       // 't' now points to the same object as 's'
        s += "!";           // This creates a new String object, "Hello!", and 's' now points to it
        System.out.println("After s += \"!\": " + s); // Outputs: "Hello!"
        System.out.println("t: " + t);               // Outputs: "Hello" (unchanged)
    }

    /**
     * Demonstrates that == compares references, not object contents.
     */
    private static void referenceComparison() {
        String s1 = "Hi", s2 = "Hi";      // Strings with identical values can be pooled
        Integer a = 12, b = 12;           // Integers between -128 and 127 are pooled
        System.out.println("s1 == s2: " + (s1 == s2)); // true (pooled Strings)
        System.out.println("a == b: " + (a == b));     // true (pooled Integers)

        String s3 = new String(s1);       // Creates a new String object
        Integer c = -222, d = -222;       // Outside of pooling range
        System.out.println("s1 == s3: " + (s1 == s3));         // false (different String objects)
        System.out.println("s1.equals(s3): " + s1.equals(s3)); // true (content is the same)
        System.out.println("c == d: " + (c == d));             // false (different Integer objects)
        System.out.println("c.equals(d): " + c.equals(d));     // true (content is the same)
    }

    /**
     * Illustrates that Java passes references by value, not the actual objects.
     */
    private static void passReferencesByValue() {
        StringBuilder sb = new StringBuilder("first ");
        addWord(sb);
        System.out.println("StringBuilder after addWord: " + sb); // Outputs: "first word"
    }

    private static void addWord(StringBuilder sb) {
        sb.append("word"); // This modifies the content of the original StringBuilder object
        sb = null;         // Only reassigns the local reference, doesn't affect the caller
    }

    /**
     * Demonstrates hashCode and toString() behaviors in Java.
     */
    private static void hashCodeAndToStringSurprises() {
        // Example with hashCode
        Object obj = new Object();
        System.out.println("Default hashCode: " + obj.hashCode()); // Generates a hashCode for the object

        // Example with toString on an array
        String[] words = {"Hello", "World"};
        System.out.println("Default array toString: " + words.toString()); // Prints array class name and hashCode
        System.out.println("Arrays.toString for readability: " + Arrays.toString(words)); // Prints: [Hello, World]
    }
}
