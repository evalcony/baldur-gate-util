package com.eval.grammar;

import com.eval.Container;
import com.eval.util.LineUtils;

import java.util.ArrayList;
import java.util.List;

public class OrFunction extends AbstractFunction {

    @Override
    boolean check(Container container, int startIndex) throws Exception {
        if (container == null || container.rawLines == null || container.rawLines.isEmpty() || startIndex >= container.rawLines.size()) {
            return false;
        }
        // 判断简单一点，不检查语法错误，只看是否有判断标志
        return (container.rawLines.get(startIndex).contains("OR("));
    }

    @Override
    boolean compile(Container container, int startIndex) {

        if (container == null || container.rawLines == null || container.rawLines.isEmpty() || startIndex >= container.rawLines.size()) {
            return false;
        }
        if (!container.rawLines.get(startIndex).contains("OR(")) {
            return false;
        }

        String sentence = container.rawLines.get(startIndex).trim();
        if (sentence.startsWith("OR(") && sentence.endsWith(")")) {
            List<String> resList = parseOr(sentence);
            container.rawLines = LineUtils.replacePosLine(container.rawLines, resList, startIndex);
            return true;
        }
        return false;
    }

    private List<String> parseOr(String sentence) {
        List<String> resList = new ArrayList<>();

        String cmds = sentence.substring(3, sentence.length()-1);
        String[] cmdArr = cmds.split("\\|\\|");
        resList.add("OR(" + cmdArr.length + ")");
        for (int i = 0; i < cmdArr.length; ++i) {
            resList.add(cmdArr[i].trim());
        }
        return resList;
    }

}
