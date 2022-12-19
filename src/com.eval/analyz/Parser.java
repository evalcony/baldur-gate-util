package com.eval.analyz;

import com.eval.Container;

import java.util.List;

public class Parser {

    /*
    任何一个结构，根据起始位置、关键标识，识别出该语法的各个组成部分
    if - if () {...}
    or - OR ()
    loop - loop(x,from,to){ ...<x>}

    loop() 的难度和 if/or 的难度为什么差那么多
        参数简单，数量固定。而or和if都要先识别一堆数量不固定的参数，并进行组装。
     */

    public List<String> parseGrammar(Container container, int startIndex, int type) {


        return null;
    }

}
