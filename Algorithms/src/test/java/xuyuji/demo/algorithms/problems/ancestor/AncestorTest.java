package xuyuji.demo.algorithms.problems.ancestor;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;

public class AncestorTest {

    private Ancestor ancestor;

    @Before
    public void setup() {
        ancestor = new Ancestor();
        ancestor.add(1, -1);
        ancestor.add(3, 1);
        ancestor.add(4, 1);
        ancestor.add(5, 1);
        ancestor.add(6, 1);
        ancestor.add(7, 1);
        ancestor.add(8, 1);
        ancestor.add(9, 1);
        ancestor.add(10, 1);
        ancestor.add(2, 10);
    }

    @Test
    public void testAsking() {
        assertArrayEquals(new int[] { 1, 0, 0, 0, 2 }, ancestor.asking(5, new int[][] { { 1, 2 }, { 2, 3 }, { 2, 4 }, { 2, 5 }, { 2, 10 } }));
    }
}
