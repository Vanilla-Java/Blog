package blog.vanillajava;

import java.util.ArrayList;
import java.util.List;

public class A {
    public static void main(String[] args) {
List<Integer> ints = new ArrayList<>();
ints.add(2);
ints.add(1);
ints.add(0);
ints.remove(0);
System.out.println("ints: " + ints);

List<Long> longs = new ArrayList<>();
longs.add(2L);
longs.add(1L);
longs.add(0L);
longs.remove(0L);
System.out.println("longs: " + longs);
    }
}
