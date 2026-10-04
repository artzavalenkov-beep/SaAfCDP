package ru.vsu.cs.SiACOD.zavalenkov;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FindDuplicate {
    //память O(n*l) в худшем случае, время O(n^2)
    public static Set<String> findPoparno(List<String> list) {
        Set<String> res = new HashSet<>();
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).equals(list.get(j))) {
                    res.add(list.get(i));
                }
            }
        }
        return res;
    }

    // O(n*l) память, время O(nlogn)
    public static Set<String> findSortPoparno(List<String> list) {
        Set<String> res = new HashSet<>();
        List<String> copy = new ArrayList<>(list);
        Collections.sort(copy);
        for (int i = 1; i < copy.size(); i++) {
            if (copy.get(i).equals(copy.get(i - 1))) {
                res.add(copy.get(i));
            }
        }
        return res;
    }

    public static Set<String> findHashSet(List<String> list) {
        Set<String> res = new HashSet<>();
        Set<String> seen = new HashSet<>();
        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            if (seen.contains(s)) {
                res.add(s);
            } else {
                seen.add(s);
            }
        }
        return res;
    }
}