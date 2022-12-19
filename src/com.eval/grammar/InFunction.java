package com.eval.grammar;

import com.eval.Container;

// TODO
// in(List<String> list, [p1,p2,...])
// list 中用占位符<name> 表示
public class InFunction extends AbstractFunction {
    @Override
    boolean check(Container container, int startIndex) throws Exception {
        return false;
    }

    @Override
    boolean compile(Container container, int startIndex) {
        return false;
    }
}
