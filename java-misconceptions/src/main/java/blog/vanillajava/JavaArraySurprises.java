package blog.vanillajava;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Based on the post https://blog.vanillajava.blog/2024/11/java-arrays-wat.html by Peter Lawrey.
 */
public class JavaArraySurprises {

    public static void main(String[] args) {
        isArrayOrNot();
        whereDidMyBracketsGo();
        differenceBetweenBracketPositions();
        multiDimensionalArrayBehavior();
        arrayClassTypes();
        superClassOfArrays();
    }

    private static void superClassOfArrays() {
        int[] intArray = new int[5];
        System.out.println("intArray is of type: " + intArray.getClass()); // class [I (int[])

        System.out.println("intArray.getClass().getComponentType() " + intArray.getClass().getComponentType());
        System.out.println("int.class.getSuperclass() = " + int.class.getSuperclass());
        // Getting the superclass of an array
        Class<?> superclass = intArray.getClass().getSuperclass();
        System.out.println("Superclass of intArray is: " + superclass); // class java.lang.Object
    }

    /**
     * Demonstrates that arrays are objects and can be assigned to Serializable or Object variables.
     */
    private static void isArrayOrNot() {
        // Creating a Serializable array and assigning it to a Serializable variable
        Serializable array = new Serializable[9];
        System.out.println("Is 'array' an instance of Serializable? " + (array instanceof Serializable)); // true
        System.out.println("Is 'array' an instance of Object? " + (array instanceof Object));             // true
        System.out.println("Is 'array' an instance of int[]? " + (array instanceof int[]));               // false
        System.out.println("Is 'array' an instance of Serializable[]? " + (array instanceof Serializable[])); // true

        // Assigning an array to an Object variable
        Object o = new Object[9];
        Array.set(o, 0, 1);
        System.out.println("Is 'o' an instance of Object[]? " + (o instanceof Object[]));                  // true
        System.out.println("Is 'o' an instance of int[]? " + (o instanceof int[]));                        // false
        System.out.println("Is 'o' an instance of Integer[]? " + (o instanceof Integer[]));                // false
        System.out.println("Is 'o[0]' an instance of Integer[]? " + (Array.get(o, 0) instanceof Integer)); // true

        // Trying to assign a primitive array to Serializable and Object references
        int[] primitiveArray = {1, 2, 3};
        System.out.println("Is 'primitiveArray' an instance of Object? " + (primitiveArray instanceof Object)); // true
        System.out.println("Is 'primitiveArray' an instance of Serializable? " + (primitiveArray instanceof Serializable)); // false
    }

    /**
     * Demonstrates the quirky syntax of returning an array from a method with varargs.
     */
    private static void whereDidMyBracketsGo() {
        // Calling the method to see how it returns an int[] array
        int[][] result = method(new int[]{1, 2, 3}, new int[]{4, 5, 6});
        System.out.println("result[0]: " + Arrays.toString(result[0])); // Outputs [1, 2, 3]
        System.out.println("result[1]: " + Arrays.toString(result[1])); // Outputs [4, 5, 6]
    }

    /**
     * Demonstrates the difference between placing brackets before and after the variable name.
     */
    private static void differenceBetweenBracketPositions() {
        int[] array, x[]; // array is int[], x is int[][]
        int array2[], y[]; // array2 is int[], y is int[]

        array = new int[5];
        x = new int[5][5];
        array2 = new int[10];
        y = new int[10];

        System.out.println("array is of type: " + array.getClass());      // class [I (int[])
        System.out.println("x is of type: " + x.getClass());              // class [[I (int[][])
        System.out.println("array2 is of type: " + array2.getClass());    // class [I (int[])
        System.out.println("y is of type: " + y.getClass());              // class [I (int[])
    }

    /**
     * Demonstrates behavior of multidimensional arrays, which are essentially arrays of arrays.
     */
    private static void multiDimensionalArrayBehavior() {
        int[][] multiArray = new int[3][];
        multiArray[0] = new int[]{1, 2};
        multiArray[1] = new int[]{3, 4, 5};
        multiArray[2] = new int[]{6};

        System.out.println("Multi-dimensional array:");
        for (int[] innerArray : multiArray) {
            System.out.println(Arrays.toString(innerArray)); // Different sizes in inner arrays
        }

        // Using reflection to see the types of each sub-array
        System.out.println("multiArray is of type: " + multiArray.getClass());          // class [[I (int[][])
        System.out.println("multiArray[0] is of type: " + multiArray[0].getClass());    // class [I (int[])
    }

    /**
     * Demonstrates how arrays of different types show up in the class type and the "L" prefix in their type name.
     */
    private static void arrayClassTypes() {
        String[] stringArray = {"Hello", "World"};
        int[] intArray = {1, 2, 3};
        Object[] objectArray = {new Object(), new Object()};

        System.out.println("String array type: " + stringArray.getClass()); // class [Ljava.lang.String;
        System.out.println("Integer array type: " + intArray.getClass());   // class [I
        System.out.println("Object array type: " + objectArray.getClass()); // class [Ljava.lang.Object;

        // Further inspection of what happens with arrays of arrays
        Object[][] multiDimObjectArray = {objectArray, new Object[]{"A", "B"}};
        System.out.println("multiDimObjectArray type: " + multiDimObjectArray.getClass()); // class [[Ljava.lang.Object;
    }

    /**
     * Example method with an unusual return type syntax.
     * Returns the first element of a variable-length argument list.
     */
    public static int[] method(int[]... args)[] {
        return args;
    }
}
