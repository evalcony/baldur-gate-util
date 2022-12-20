package com.eval.grammar;

import com.eval.Container;

/**
 lock {
 ...
 }
 效果: 保护lock中间代码块不被打断。
 */
public class LockFunction extends AbstractFunction {
    @Override
    public String getName() {
        return "lock";
    }

    @Override
    boolean check(Container container, int startIndex) throws Exception {
        if (container.rawLines.get(startIndex).startsWith("lock {") || container.rawLines.get(startIndex).startsWith("lock{"))
            return true;
        return false;
    }

    @Override
    boolean compile(Container container, int startIndex) {
        int endPos = startIndex;
        while (!container.rawLines.get(endPos).trim().equals("}")) {
            endPos++;
        }
        container.rawLines.set(startIndex, "SetInterrupt(FALSE)");
        container.rawLines.set(endPos, "SetInterrupt(TRUE)");
        return true;
    }
}
