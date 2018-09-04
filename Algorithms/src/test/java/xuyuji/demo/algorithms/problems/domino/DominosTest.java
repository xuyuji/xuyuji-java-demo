package xuyuji.demo.algorithms.problems.domino;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;

public class DominosTest {

    private Dominos dominos;

    @Before
    public void setup() {
        dominos = new Dominos(4);
        dominos.add(16, 5);
        dominos.add(20, 5);
        dominos.add(10, 10);
        dominos.add(18, 2);
    }

    @Test
    public void testExec() {
        assertArrayEquals(new int[] { 3, 1, 4, 1 }, dominos.exec());
    }
}
