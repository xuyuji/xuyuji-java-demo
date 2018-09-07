package xuyuji.demo.algorithms.problems.exam;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ExamTest {
    private Exam exam;

    @Before
    public void setup() {
        exam = new Exam();
    }

    @Test
    public void testExec1() {
        assertEquals(94, exam
                .exec(new int[][] { { 20, 20, 100, 60 }, { 50, 30, 80, 55 }, { 100, 60, 110, 88 }, { 5, 3, 10, 6 } }));
    }
    
    @Test
    public void testExec2() {
        assertEquals(80, exam
                .exec(new int[][] { { 50, 50, 100, 60 }, { 50, 30, 80, 45 } }));
    }
}
