package com.eval.grammar;

import com.eval.Container;
import com.eval.util.LineUtils;

import java.util.ArrayList;
import java.util.List;

public class LoopFunction extends AbstractFunction {

    private List<String> paramList;
    private int endIndex;

    /*
        grammar

        loop(name,from,to) {
            ...<name>
        }
        例如
        @loop(i,1,6) {
            ...<i> // 注意，写的时候要用<>把变量包起来
        }
     */

    @Override
    boolean check(Container container, int startIndex) throws Exception {
        if (container == null)
            return false;
        List<String> rawLine = container.rawLines;
        if (rawLine == null || rawLine.isEmpty() || startIndex >= rawLine.size()) {
            return false;
        }
        if (!rawLine.get(startIndex).contains("loop")) {
            return false;
        }
        paramList = new ArrayList<>();
        findSetupValues(rawLine.get(startIndex), paramList);
        int stk = 0;
        for (int i = startIndex; i < rawLine.size(); ++i) {
            if (rawLine.get(i).contains("{")) {
                ++stk;
            }
            if (rawLine.get(i).contains("}")) {
                --stk;
                if (stk == 0) {
                    endIndex = i;
                    break;
                }
            }
        }
        if (stk > 0) {
            throw new Exception("grammar 语法异常");
        }
        return true;
    }

    @Override
    boolean compile(Container container, int startIndex) {
        String varname = paramList.get(0);
        int from = Integer.parseInt(paramList.get(1));
        int to = Integer.parseInt(paramList.get(2));

        List<String> block = new ArrayList<>();
        for (int i = from; i <= to; ++i) {
            for (int j = startIndex+1; j < endIndex; ++j) {
                String line = container.rawLines.get(j);
                // 对下标替换
                line = line.replace("<"+varname+">", i+"");
                block.add(line);
            }
        }
        // 将[startIndex,endIndex]范围内代码替换为block代码
        container.rawLines = LineUtils.replaceRangeLine(container.rawLines, block, startIndex, endIndex);
        return true;
    }

    private void findSetupValues(String sentence, List<String> paramList) {
        int start = sentence.indexOf("(");
        int end = sentence.lastIndexOf(")");
        String[] vars = sentence.substring(start+1, end).split(",");
        paramList.add(vars[0]);
        paramList.add(vars[1]);
        paramList.add(vars[2]);
    }
}
