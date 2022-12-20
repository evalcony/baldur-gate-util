package com.eval.util;

import com.eval.Container;
import com.eval.grammar.*;

import java.lang.management.LockInfo;
import java.util.Map;

public class Translator {

    // 目前，语法检测有先后顺序
    private static AbstractFunction[] functions = {
            new LoopFunction(),
            new IfFunction(),
            new MacroCmdFunction(),
            new OrFunction(),//or必须放在if之后
            new BetweenFunction(),
            new LockFunction(),
    };

    private static Translator instance = new Translator();

    private Translator(){}

    public static Translator getInstance() {
        return instance;
    }

    public void translate(Container container) {
        transGrammarToRaw(container);
        transModuleToRaw(container);
        transRawToScript(container);
    }

    /**
     * 将包含grammar语法的语句翻译为略缩代码
     * @param container
     */
    private void transGrammarToRaw(Container container) {
        try {
            for (int j = 0; j < functions.length; ++j) {
                for (int i = 0; i < container.rawLines.size(); ++i) {
                    functions[j].grammerToRaw(container, i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 包含模板的翻译
    /**
     * 把包含 ~模板 解析成raw代码
     * @param container
     */
    private void transModuleToRaw(Container container) {
        for (int i = 0; i < container.rawLines.size(); ++i) {
            String line = container.rawLines.get(i);
            // 将模板代号替换为模板代码
            if (line.contains("~")) {
                String moduleName = SourceProccessor.getDict().get(line.trim());
                Container moduleContainer = SourceProccessor.getModuleDict().get(moduleName);
                if (moduleContainer == null)
                    continue;
                Translator.getInstance().translate(moduleContainer);
                container.rawLines = LineUtils.replacePosLine(container.rawLines, moduleContainer.rawLines, i);
            }
        }
    }

    // 将略缩代码翻译为脚本script代码
    private void transRawToScript(Container container) {
        try {
            for (int i = 0; i < container.rawLines.size(); ++i) {
                container.resLines.add(repl(container.rawLines.get(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
