package com.eval.grammar;

import com.eval.Container;
import com.eval.util.LineUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：对于指定代码块，将一组数据根据顺序填充进特定位置中
 * 语法
 * foreach(i,[a1,a2,a3,...]) {
 *     xxx  <i>  xxx
 * }
 * 效果
 * xxx  a1  xxx
 * xxx  a2  xxx
 * xxx  a3  xxx
 * ...
 *
 */
public class ForeachFunction extends AbstractFunction {
    @Override
    public String getName() {
        return "foreach";
    }

    @Override
    boolean check(Container container, int startIndex) throws Exception {
        return container.rawLines.get(startIndex).startsWith("foreach");
    }

    @Override
    boolean compile(Container container, int startIndex) {

        String sentence = container.rawLines.get(startIndex);
        int leftBrackPos = sentence.indexOf("(");
        int rightBrackPos = sentence.lastIndexOf(")");
        String paramStr = sentence.substring(leftBrackPos+1, rightBrackPos);
        Holder paramHolder = parseParam(paramStr);
        try {
            findBounce(container, startIndex, paramHolder);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        fillParam(container, startIndex, paramHolder);
        return true;
    }

    // 解析参数
    private Holder parseParam(String params) {
        int left = params.indexOf("[");
        int right = params.indexOf("]");
        String[] param1 = params.substring(0, left).split(",");
        // 占位符
        String token = param1[0].trim();
        // 替换值列表
        String[] eleArr = params.substring(left+1,right).split(",");

        return new Holder(token, eleArr);
    }

    private void findBounce(Container container, int startIndex, Holder paramHolder) throws Exception {
        int stk = 0;
        int endIndex = 0;
        for (int i = startIndex; i < container.rawLines.size(); ++i) {
            if (container.rawLines.get(i).contains("{")) {
                ++stk;
            }
            if (container.rawLines.get(i).contains("}")) {
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
        paramHolder.startPos = startIndex;
        paramHolder.endPos = endIndex;
    }

    private void fillParam(Container container, int startIndex, Holder paramHolder) {
        List<String> block = new ArrayList<>();
        for (int i = 0; i < paramHolder.eleArr.length; ++i) {
            for (int j = paramHolder.startPos+1; j < paramHolder.endPos; ++j) {
                String line = container.rawLines.get(j);
                // 对下标替换
                line = line.replace("<"+paramHolder.token+">", paramHolder.eleArr[i]);
                block.add(line);
            }
        }
        // 将[startIndex,endIndex]范围内代码替换为block代码
        container.rawLines = LineUtils.replaceRangeLine(container.rawLines, block, startIndex, paramHolder.endPos);
    }

    class Holder {
        String token;
        String[] eleArr;

        int startPos;
        int endPos;

        Holder(String token, String[] eleArr) {
            this.token = token;
            this.eleArr = eleArr;
        }
    }
}
