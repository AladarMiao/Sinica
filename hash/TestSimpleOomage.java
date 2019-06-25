package hw3.hash;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;


public class TestSimpleOomage {

    /**
     * Calls tests for SimpleOomage.
     */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestSimpleOomage.class);
    }

    @Test
    public void testHashCodeDeterministic() {
        SimpleOomage so = SimpleOomage.randomSimpleOomage();
        int hashCode = so.hashCode();
        for (int i = 0; i < 100; i += 1) {
            assertEquals(hashCode, so.hashCode());
        }
    }

    @Test
    public void hue() {
        SimpleOomage one = new SimpleOomage(5, 10, 20);
        int hue = one.hashCode();
        int hehe = one.hashCode();

    }

    @Test
    public void testHashCodePerfect() {


        SimpleOomage one = new SimpleOomage(5, 10, 20);
        SimpleOomage on = new SimpleOomage(5, 20, 10);
        SimpleOomage o = new SimpleOomage(10, 20, 5);
        SimpleOomage tw = new SimpleOomage(20, 5, 10);
        SimpleOomage two = new SimpleOomage(10, 5, 20);
        SimpleOomage t = new SimpleOomage(20, 10, 5);

        assertNotEquals(one.hashCode(), two.hashCode());
        assertNotEquals(one.hashCode(), on.hashCode());
        assertNotEquals(one.hashCode(), o.hashCode());
        assertNotEquals(one.hashCode(), tw.hashCode());
        assertNotEquals(one.hashCode(), t.hashCode());
        /*
          meaning no two SimpleOomages should EVER have the same
          hashCode UNLESS they have the same red, blue, and green values!
         */
    }

    @Test
    public void testEquals() {
        SimpleOomage ooA = new SimpleOomage(5, 10, 20);
        SimpleOomage ooA2 = new SimpleOomage(5, 10, 20);
        SimpleOomage ooB = new SimpleOomage(50, 50, 50);
        assertEquals(ooA, ooA2);
        assertNotEquals(ooA, ooB);
        assertNotEquals(ooA2, ooB);
        assertNotEquals(ooA, "ketchup");
    }

    @Test
    public void testHashCodeAndEqualsConsistency() {
        SimpleOomage ooA = new SimpleOomage(5, 10, 20);
        SimpleOomage ooA2 = new SimpleOomage(5, 10, 20);
        HashSet<SimpleOomage> hashSet = new HashSet<>();
        hashSet.add(ooA);
        assertTrue(hashSet.contains(ooA2));
    }


    @Test
    public void testRandomOomagesHashCodeSpread() {
        List<Oomage> oomages = new ArrayList<>();
        int N = 10000;

        for (int i = 0; i < N; i += 1) {
            oomages.add(SimpleOomage.randomSimpleOomage());
        }

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(oomages, 10));
    }
}
