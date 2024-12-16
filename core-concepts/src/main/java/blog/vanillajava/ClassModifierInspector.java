package blog.vanillajava;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ClassModifierInspector {
    // ------------------------------------------------------------------------------------
    // Constants for Class and Interface Modifiers
    // ------------------------------------------------------------------------------------

    /**
     * The Java source modifiers that can be applied to a class.
     *
     * @jls 8.1.1 Class Modifiers
     */
    private static final int CLASS_MODIFIERS =
            Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE |
                    Modifier.ABSTRACT | Modifier.STATIC | Modifier.FINAL |
                    Modifier.STRICT;

    /**
     * The Java source modifiers that can be applied to an interface.
     *
     * @jls 9.1.1 Interface Modifiers
     */
    private static final int INTERFACE_MODIFIERS =
            Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE |
                    Modifier.ABSTRACT | Modifier.STATIC | Modifier.STRICT;

    // ------------------------------------------------------------------------------------
    // Utility Methods
    // ------------------------------------------------------------------------------------

    static String getDescription(Class<?> clazz) {
        return getDescription(clazz, clazz.getSimpleName());
    }

    static String getDescription(Class<?> clazz, String simpleName) {
        return getDescription(clazz.getModifiers()) + " "
                + (clazz.getSimpleName().isEmpty() ? "/* has no name */ " : "")
                + simpleName + " { "
                + getFields(clazz, true, "") + "}";
    }

    private static String getFields(Class<?> clazz, boolean isBase, String modifierString) {
        if (clazz.getSuperclass() != null)
            modifierString = getFields(clazz.getSuperclass(), false, modifierString);
        for (Field field : clazz.getDeclaredFields()) {
            modifierString += field.getType().getSimpleName() + " "
                    + (isBase ? "" : clazz.getSimpleName() + ".")
                    + field.getName() + "; ";
        }
        return modifierString;
    }

    static String getDescription(int modifier) {
        List<String> modifiers = new ArrayList<>();
        if (Modifier.isPublic(modifier))
            modifiers.add("public");
        if (Modifier.isProtected(modifier))
            modifiers.add("protected");
        if (Modifier.isPrivate(modifier))
            modifiers.add("private");
        if (modifiers.isEmpty())
            modifiers.add("/*package-private*/");
        if (Modifier.isAbstract(modifier))
            modifiers.add("abstract");
        if (Modifier.isStatic(modifier))
            modifiers.add("static");
        if (Modifier.isFinal(modifier))
            modifiers.add("final");
        if (Modifier.isStrict(modifier))
            modifiers.add("strictfp");
        if (Modifier.isInterface(modifier))
            modifiers.add("interface");
        return String.join(" ", modifiers);
    }

    // ------------------------------------------------------------------------------------
    // Main Method
    // ------------------------------------------------------------------------------------

    public static void main(String[] args) {
        System.out.println("ModifierMain:");
        System.out.println("CLASS_MODIFIERS: " + getDescription(CLASS_MODIFIERS));
        System.out.println("INTERFACE_MODIFIERS: " + getDescription(INTERFACE_MODIFIERS));
        System.out.println(getDescription(ClassModifierInspector.class));
        System.out.println(getDescription(public_class.class));
        System.out.println(getDescription(public_class.inner_class.class, "public_class.inner_class"));
        System.out.println(getDescription(protected_class.class));
        System.out.println(getDescription(private_class.class));
        System.out.println(getDescription(nested_annotation.class));
        System.out.println(getDescription(nested_annotation.inner_class.class, "nested_annotation.inner_class"));
        System.out.println(getDescription(annotation_interface.class));
        System.out.println(getDescription(nested_interface.class));
        System.out.println(getDescription(nested_interface.inner_class.class, "nested_interface.inner_class"));
        System.out.println(getDescription(inner_class.class));
        System.out.println(getDescription(nested_class.class));
        System.out.println(getDescription(inner_abstract_class.class));
        System.out.println(getDescription(inherits_inner_abstract_class.class));
        System.out.println(getDescription(abstract_static_class.class));
        System.out.println(getDescription(final_inner_class.class));
        System.out.println(getDescription(nested_enum_class.class));
        System.out.println(getDescription(static_nested_enum_class.class));

        System.out.println(getDescription(new ClassModifierInspector().anonymous_class, "anonymous_class"));
        System.out.println(getDescription(static_anonymous_class, "static_anonymous_class"));

        class LocalClass {
        }
        System.out.println(getDescription(LocalClass.class));

        System.out.println(getDescription(_annotation.class));
        System.out.println(getDescription(enum_class.class));
        System.out.println(getDescription(enum_class.B.getClass(), "enum_class.B"));
        System.out.println(getDescription(abstract_class.class));
        System.out.println(getDescription(_interface.class));

        System.out.println(getDescription(((Runnable) Thread::yield).getClass(), "Method reference"));
        System.out.println(getDescription(((Runnable) System.out::println).getClass(), "Capturing method reference"));
        System.out.println(getDescription(((Runnable) () -> {}).getClass(), "Lambda class"));
        System.out.println(getDescription(((Runnable) () -> System.out.println()).getClass(), "Lambda class"));
        ClassModifierInspector cmi = new ClassModifierInspector();
        System.out.println(getDescription(((Runnable) () -> System.out.println(cmi)).getClass(), "Capturing lambda class"));
    }

    // ------------------------------------------------------------------------------------
    // Nested Classes
    // ------------------------------------------------------------------------------------

    // Public nested class
    public static class public_class {
        class inner_class {
        }
    }

    // Protected nested class
    protected static class protected_class {
    }

    // Private nested class
    private static class private_class {
    }

    // Annotation type
    abstract static
    @interface nested_annotation {
        class inner_class {
        }
    }

    // Interfaces
    abstract static
    interface annotation_interface extends _annotation {
    }

    abstract static
    interface nested_interface {
        class inner_class {
        }
    }

    // Inner classes (non-static)
    class inner_class {
    }

    static class nested_class {
    }

    abstract class inner_abstract_class {
        long this$0;
    }

    class inherits_inner_abstract_class extends inner_abstract_class {
        int this$0;
        double this$0$;
    }

    abstract static class abstract_static_class {
    }

    final class final_inner_class {
    }

    enum nested_enum_class {
    }

    static
    enum static_nested_enum_class {
    }

    final Class<?> anonymous_class = new Object() {
    }.getClass();

    static final Class<?> static_anonymous_class = new Object() {
    }.getClass();
}

// Top-level annotations, enums, classes, and interfaces
@interface _annotation {
}

enum enum_class {
    A,
    B {
        @Override
        public String toString() {
            return "B";
        }
    }
}

abstract class abstract_class {
}

interface _interface {
}
