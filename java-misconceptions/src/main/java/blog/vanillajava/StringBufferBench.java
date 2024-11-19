package blog.vanillajava;

/*
Ryzen 5950X, Ubuntu, Azul 21.0.5, performance governor
StringBuffer took an average of 456 ns
StringBuilder took an average of 125 ns
StringBuffer took an average of 461 ns
StringBuilder took an average of 162 ns
StringBuffer took an average of 400 ns
StringBuilder took an average of 97 ns
StringBuffer took an average of 387 ns
StringBuilder took an average of 98 ns
StringBuffer took an average of 388 ns
StringBuilder took an average of 97 ns
 */
public class StringBufferBench {
    public static void main(String... args) {
        String text = "A short piece of text for copying";
        int runs = 1000000;

        for (int i = 0; i < 5; i++) {
            {
                long start = System.nanoTime();
                StringBuffer sb = new StringBuffer(text);
                for (int r = 0; r < runs; r++)
                    copyStringBuffer(sb);
                long time = System.nanoTime() - start;
                System.out.printf("StringBuffer took an average of %,d ns%n", time/runs);
            }
            {
                long start = System.nanoTime();
                StringBuilder sb = new StringBuilder(text);
                for (int r = 0; r < runs; r++)
                    copyStringBuilder(sb);
                long time = System.nanoTime() - start;
                System.out.printf("StringBuilder took an average of %,d ns%n", time/runs);
            }
        }
    }

    public static String copyStringBuffer(StringBuffer text) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < text.length(); i++)
            sb.append(text.charAt(i));
        return sb.toString();
    }

    public static String copyStringBuilder(StringBuilder text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++)
            sb.append(text.charAt(i));
        return sb.toString();
    }
}
