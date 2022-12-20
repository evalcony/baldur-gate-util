package com.eval.grammar;

import com.eval.Container;
import com.eval.util.LineUtils;

import java.util.ArrayList;
import java.util.List;

public class AndFunction extends AbstractFunction {
    @Override
    public String getName() {
        return "and";
    }

    @Override
    boolean check(Container container, int startIndex) throws Exception {
        if (container.rawLines.get(startIndex).contains("&&")) {
            return true;
        }
        return false;
    }

    @Override
    boolean compile(Container container, int startIndex) {

        String[] arr = container.rawLines.get(startIndex).split("&&");
        List<String> res = new ArrayList<>();
        for (int i = 0; i < arr.length; ++i) {
            res.add(arr[i].trim());
        }
        container.rawLines = LineUtils.replacePosLine(container.rawLines, res, startIndex);
        return true;
    }
}
