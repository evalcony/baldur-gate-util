package com.eval.grammar;


import com.eval.Container;
import com.eval.util.LineUtils;

import java.util.ArrayList;
import java.util.List;

// between(obj, left, right)
public class BetweenFunction extends AbstractFunction {
    @Override
    public String getName() {
        return "between";
    }

    @Override
    boolean check(Container container, int startIndex) throws Exception {
        return container.rawLines.get(startIndex).contains("between(") && container.rawLines.get(startIndex).contains(")");
    }

    @Override
    boolean compile(Container container, int startIndex) {
        String sentence = container.rawLines.get(startIndex);
        int lBracketPos = sentence.indexOf("(");
        int rBracketPos = sentence.lastIndexOf(")");
        String paramSeg = sentence.substring(lBracketPos+1, rBracketPos);
        String[] params = paramSeg.split(",");
        List<String> resList = new ArrayList<>();
        // !Range(obj, x) && Range(obj,y)
        resList.add("!Range(" + params[0] + "," + params[1] + ")" + " && " + "Range(" + params[0] + "," + params[2] + ")");
        container.rawLines = LineUtils.replacePosLine(container.rawLines, resList, startIndex);
        return true;
    }
}
