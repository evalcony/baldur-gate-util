package com.eval.util;

import com.eval.Container;
import com.eval.grammar.*;

import java.util.Map;

public class Translator {

    // 目前，语法检测有先后顺序
    private static AbstractFunction[] functions = {
            new LoopFunction(),
            new IfFunction(),
            new OrFunction(),
            new MacroCmdFunction(),
            new AndFunction(),
            new BetweenFunction(),
            new LockFunction(),
    };
//    private static MacroCmdGrammar macroCmdGrammar = new MacroCmdGrammar();

    private static Translator instance = new Translator();

    private Translator(){}

    public static Translator getInstance() {
        return instance;
    }

    public void translate(Container container) {
        System.out.println("开始翻译...");

        for (int i = 0; i < container.rawLines.size(); ++i) {
            translateEachLine(container, i);
        }
    }

    private void translateEachLine(Container container, int startPos) {
        transModuleToRaw(container, startPos); // 模板
        transGrammarToRaw(container, startPos); // 具体语法
        transRawToScript(container, startPos); // 缩略
    }

    /**
     * 将包含grammar语法的语句翻译为略缩代码
     * @param container
     */
    private void transGrammarToRaw(Container container, int startPos) {
        try {
            boolean flag = false;
            do {
                flag = false;
                for (int j = 0; j < functions.length; ++j) {
                    if (functions[j].grammerToRaw(container, startPos) && "macro".equals(functions[j].getName())) {
                        flag = true;
                    }
                }
            } while (flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 把包含 ~模板 解析成raw代码
     * @param container
     */
    private void transModuleToRaw(Container container, int startPos) {
        boolean flag;
        do {
            flag = false;
            String line = container.rawLines.get(startPos);
            // 将模板代号替换为模板代码
            if (!line.startsWith("~")) {
                return;
            }
            String moduleName = SourceProccessor.getDict().get(line.trim());
            Container moduleContainer = SourceProccessor.getModuleDict().get(moduleName);
            if (moduleContainer == null)
                return;
            container.rawLines = LineUtils.replacePosLine(container.rawLines, moduleContainer.rawLines, startPos);
            flag = true;
        } while (flag);
    }

    // 将略缩代码翻译为脚本script代码
    private void transRawToScript(Container container, int startPos) {
        try {
            container.resLines.add(repl(container.rawLines.get(startPos)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //--------------------

//    public void translatex(Container container) {
//        for (int i = 0; i < container.rawLines.size(); ++i) {
//            transGrammarToRaw(container);
//            transModuleToRaw(container);
//            transRawToScript(container);
//        }
//    }
//
//    /**
//     * 将包含grammar语法的语句翻译为略缩代码
//     * @param container
//     */
//    private void transGrammarToRaw(Container container) {
//        try {
//            boolean flag = true;
//            for (int j = 0; j < functions.length; ++j) {
//                for (int i = 0; i < container.rawLines.size(); ++i) {
//                    functions[j].grammerToRaw(container, i);
//                    flag = true;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // 包含模板的翻译
//    /**
//     * 把包含 ~模板 解析成raw代码
//     * @param container
//     */
//    private void transModuleToRaw(Container container) {
//        for (int i = 0; i < container.rawLines.size(); ++i) {
//            String line = container.rawLines.get(i);
//            // 将模板代号替换为模板代码
//            if (line.contains("~")) {
//                String moduleName = SourceProccessor.getDict().get(line.trim());
//                Container moduleContainer = SourceProccessor.getModuleDict().get(moduleName);
//                if (moduleContainer == null)
//                    continue;
//                Translator.getInstance().translate(moduleContainer);
//                container.rawLines = LineUtils.replacePosLine(container.rawLines, moduleContainer.rawLines, i);
//            }
//        }
//    }
//
//    // 将略缩代码翻译为脚本script代码
//    private void transRawToScript(Container container) {
//        try {
//            for (int i = 0; i < container.rawLines.size(); ++i) {
//                container.resLines.add(repl(container.rawLines.get(i)));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private String repl(String source) {
        String line = source;
        for (Map.Entry<String, String> p : SourceProccessor.getDict().entrySet()) {
            if (line.contains(p.getKey())) {
                String r = line.replace(p.getKey(), p.getValue());
                line = r;
            }
        }
        return line;
    }

}
