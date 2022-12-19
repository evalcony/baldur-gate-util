package com.eval.util;

import java.util.ArrayList;
import java.util.List;

public class LineUtils {

    public static List<String> replacePosLine(List<String> a, List<String> b, int indexOfA) {
        List<String> res = new ArrayList<>(a.size()+b.size()-1);
        for (int i = 0; i < indexOfA; ++i) {
            res.add(a.get(i));
        }
        for (int i = 0; i < b.size(); ++i) {
            res.add(b.get(i));
        }
        for (int i = indexOfA+1; i < a.size(); ++i) {
            res.add(a.get(i));
        }
        return res;
    }

    /**
     * @param a 原list
     * @param b 目标list
     * @param startOfA 原list被替换位置的起始下标
     * @param endOfA 原list被替换位置的结束下标
     * @return
     */
    public static List<String> replaceRangeLine(List<String> a, List<String> b, int startOfA, int endOfA) {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < startOfA; ++i) {
            res.add(a.get(i));
        }
        for (int i = 0; i < b.size(); ++i) {
            res.add(b.get(i));
        }
        for (int i = endOfA+1; i < a.size(); ++i) {
            res.add(a.get(i));
        }
        return res;
    }
}
