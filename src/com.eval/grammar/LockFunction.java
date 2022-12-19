package com.eval.grammar;

import com.eval.Container;

public class LockFunction extends AbstractFunction {
    /**
     lock {
        ...
     }
     */
    @Override
    boolean check(Container container, int startIndex) throws Exception {
        if (container.rawLines.get(startIndex).contains("lock {") || container.rawLines.get(startIndex).contains("lock{"))
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
