package xuyuji.demo.algorithms.problems.domino;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Dominos {

    private class Domino {
        private int x;
        private int h;
        private int index;
    }

    private int size;

    private int index;

    private List<Domino> list;

    private Comparator<Domino> comparator;

    public Dominos(int size) {
        this.size = size;
        index = 0;
        list = new ArrayList<>();
        comparator = new Comparator<Domino>() {
            @Override
            public int compare(Domino d1, Domino d2) {
                if (d1.x > d2.x) {
                    return 1;
                } else if (d1.x == d2.x) {
                    return 0;
                } else {
                    return -1;
                }
            }
        };
    }

    public void add(int x, int h) {
        Domino domino = new Domino();
        domino.x = x;
        domino.h = h;
        domino.index = index++;
        list.add(domino);
    }

    public int[] exec() {
        if (list.size() != size) {
            return null;
        }

        Collections.sort(list, comparator);

        int[] result = new int[size];
        int i = size - 1;
        while (i >= 0) {
            Domino domino = list.get(i);
            int num = 1;
            for (int j = i + 1; j < size; j++) {
                Domino nextDomino = list.get(j);
                if (domino.x + domino.h > nextDomino.x) {
                    num = Math.max(num, result[list.get(j).index] + (j - i));
                } else {
                    break;
                }
            }
            result[domino.index] = num;
            i--;
        }

        return result;
    }
}
