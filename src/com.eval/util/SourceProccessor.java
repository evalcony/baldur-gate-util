package com.eval.util;

import com.eval.Container;
import com.eval.util.file.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SourceProccessor {
    private static String BASE_PATH = "resource/";
    private static List<Dict> dictList = new ArrayList<>();

    public static Map<String, String> getDict() {
        return dictList.get(2).getSource();
    }
    public static Map<String, String> getIds() {
        return dictList.get(0).getSource();
    }
    public static Map<String, Container> getModuleDict() {
        return dictList.get(1).getSource();
    }
    public static Map<String, String> getMacroCmdBlock() {
        return (Map<String, String>) dictList.get(3).getSource().get("cmd");
    }
    public static Map<String, String> getMacroReplaceBlock() {
        return (Map<String, String>) dictList.get(3).getSource().get("replace");
    }

    public static void loadResources(String dictPath) {
        dictList.add(new IdsDict(BASE_PATH+"ids/"));
        dictList.add(new ModuleDict(BASE_PATH+"module/"));
        dictList.add(new UserDefineDict(dictPath));
        dictList.add(new MacroDict(BASE_PATH+"macro/"));

        for (int i = 0; i < dictList.size(); ++i) {
            dictList.get(i).loadSource();
        }
    }


    public static void readInput(Container container, String path) {
        new FileProcessor().read(container, path);
    }

    public static void writeOutput(Container container, String path) {
        new FileProcessor().write(container, path);
    }
}
