package com.eval;

import com.eval.util.SourceProccessor;
import com.eval.util.Translator;

public class Main {

    public static void main(String[] args) {
        if (args == null || args.length != 3) {
            System.out.println("缺失启动参数 参考readme.txt");
            return;
        }

        if (args != null && args.length > 0) {
            System.out.println("程序启动参数：" + args[0] + " " + args[1] +" " + args[2]);
        }

        String inputPath = args[0];
        String outputPath = args[1];
        String dictPath = args[2];

        // 加载资源文件
        SourceProccessor.loadResources(dictPath);

        Container container = new Container();
        // 加载脚本
        SourceProccessor.read(container, inputPath);
        // 翻译脚本
        Translator.getInstance().translate(container);
        // 输出脚本
        SourceProccessor.write(container, outputPath);

        System.out.println("执行结束");
    }
}
