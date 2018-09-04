package xuyuji.demo.algorithms.problems.ancestor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ancestor {

    private class Person {
        int code;
        int parentCode;
        int level;
    }

    private Person top;

    private Map<Integer, Person> familyTree;

    private Map<Integer, List<Integer>> relationMap;

    private boolean treeIsInit;

    public Ancestor() {
        this.familyTree = new HashMap<>();
        this.relationMap = new HashMap<>();
        this.treeIsInit = false;
    }

    public void add(int a, int b) {
        if (a == -1) {
            top = new Person();
            top.code = b;
            top.parentCode = a;
            top.level = 1;
            familyTree.put(b, top);
        } else if (b == -1) {
            top = new Person();
            top.code = a;
            top.parentCode = b;
            top.level = 1;
            familyTree.put(a, top);
        } else {
            if (relationMap.containsKey(a)) {
                relationMap.get(a).add(b);
            } else {
                List<Integer> list = new ArrayList<>();
                list.add(b);
                relationMap.put(a, list);
            }

            if (relationMap.containsKey(b)) {
                relationMap.get(b).add(a);
            } else {
                List<Integer> list = new ArrayList<>();
                list.add(a);
                relationMap.put(b, list);
            }
        }
    }

    private void initTree() {
        List<Person> currLevel = new ArrayList<>();
        currLevel.add(top);
        int level = top.level;

        while (!currLevel.isEmpty()) {
            List<Person> nextLevel = new ArrayList<>();
            level++;
            for (Person p : currLevel) {
                for (Integer code : relationMap.get(p.code)) {
                    if (code != p.parentCode) {
                        Person child = new Person();
                        child.code = code;
                        child.parentCode = p.code;
                        child.level = level;
                        nextLevel.add(child);
                        familyTree.put(code, child);
                    }
                }
            }
            currLevel = nextLevel;
        }

        relationMap = null;
    }

    private boolean isAncestor(Person elder, Person junior) {
        while (elder.level <= junior.level) {
            if (junior.parentCode == elder.code) {
                return true;
            }
            junior = familyTree.get(junior.parentCode);
        }
        return false;
    }

    public int[] asking(int n, int[][] questions) {
        if (!treeIsInit) {
            initTree();
        }

        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            Person a = familyTree.get(questions[i][0]);
            Person b = familyTree.get(questions[i][1]);
            
            if(a.level > b.level && isAncestor(b, a)) {
                result[i] = 2;
                continue;
            }
            
            if(a.level < b.level && isAncestor(a, b)) {
                result[i] = 1;
                continue;
            }
            
            result[i] = 0;
        }

        return result;
    }
}
