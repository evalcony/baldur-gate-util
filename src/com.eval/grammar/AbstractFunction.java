package com.eval.grammar;

import com.eval.Container;

public abstract class AbstractFunction {
    // 判断语法是否满足脚本规则
    abstract boolean check(Container container, int startIndex) throws Exception;
    // 将特定语法解析成对应功能脚本
    abstract boolean compile(Container container, int startIndex);

    public void grammerToRaw(Container container, int curLineIndex) throws Exception {
        if (container == null || container.rawLines == null || curLineIndex >= container.rawLines.size()) {
            return;
        }
        if (!check(container, curLineIndex)) {
            return;
        }
        compile(container, curLineIndex);
    }
}
