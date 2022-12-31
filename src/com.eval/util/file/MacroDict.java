package com.eval.util.file;

import com.eval.grammar.MacroCmdFunction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MacroDict implements Dict {

    private static String path;
    private static Map<String, String> macroCmdBlock = new HashMap<>();
    private static Map<String, String> macroReplaceBlock = new HashMap<>();
    private static Map<String, Map<String, String>> holder = null;

    public MacroDict(String path) {
        this.path = path;
    }

    @Override
    public void loadSource() {
        System.out.println("加载宏定义文件...");
        readMacro();
    }

    @Override
    public Map getSource() {
        if (holder == null) {
            holder = new HashMap<>();
            holder.put("cmd", macroCmdBlock);
            holder.put("replace", macroReplaceBlock);
        }
        return holder;
    }

    private static void readMacro() {

        List<String> ls;
        try {
            ls = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        for (int i = 0; i < ls.size(); ++i) {
            // 忽略注释
            if (ls.get(i).trim().startsWith("//")) {
                continue;
            }
            int pos = ls.get(i).indexOf(")");
            String cmdBlock = ls.get(i).substring(0, pos+1).trim();
            String replaceBlock = ls.get(i).substring(pos+1).trim();
            String cmdName = MacroCmdFunction.getMacroName(cmdBlock);

            macroCmdBlock.put(cmdName, cmdBlock);
            macroReplaceBlock.put(cmdName, replaceBlock);
        }
    }
}
