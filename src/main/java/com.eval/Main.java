package com.eval;

import com.eval.util.SourceProccessor;

public class Main {

    public static void main(String[] args) {
//        if (args == null || args.length != 3) {
//            System.out.println("缺失启动参数 参考readme.txt");
//            return;
//        }
//
//        if (args != null && args.length > 0) {
//            System.out.println("程序启动参数：" + args[0] + " " + args[1] +" " + args[2]);
//        }

        String inputPath = "input.txt";
        String outputPath = "output.txt";
        String dictPath = "dict.txt";

        SourceProccessor sourceProccessor = new SourceProccessor();
        // 加载资源文件
        sourceProccessor.loadResources(dictPath);

        Container container = new Container();
        // 加载脚本
        sourceProccessor.readInput(container, inputPath);
        // 翻译脚本
        Translator.getInstance().translate(container);
        // 输出脚本
        sourceProccessor.writeOutput(container, outputPath);

        System.out.println("执行结束");
    }
}
