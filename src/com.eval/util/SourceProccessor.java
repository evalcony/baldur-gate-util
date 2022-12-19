package com.eval.util;

import com.eval.Container;
import com.eval.grammar.MacroCmdFunction;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SourceProccessor {
    private static String BASE_PATH = "resource/";

    private static Map<String, String> dict = null;
    private static Map<String, String> macroCmdBlock = new HashMap<>();
    private static Map<String, String> macroReplaceBlock = new HashMap<>();
    private static Map<String, Container> moduleDict = new HashMap<>();
    private static List<String> moduleFileNames = new ArrayList<>();

    public static Map<String, String> getDict() {
        return dict;
    }
    public static Map<String, Container> getModuleDict() {
        return moduleDict;
    }
    public static List<String> getModuleFileNames() {
        return moduleFileNames;
    }

    public static void loadResources(String dictPath) {
        loadDict(dictPath);
        loadModule();
        loadMacro();
    }

    public static void loadDict(String dictPath) {
        System.out.println("加载dict文件...");
        readDict(dictPath);
    }

    public static void loadMacro() {
        System.out.println("加载宏定义文件...");
        readMacro();
    }

    public static Map<String, String> getMacroCmdBlock() {
        return macroCmdBlock;
    }
    public static Map<String, String> getMacroReplaceBlock() {
        return macroReplaceBlock;
    }

    // TODO 待优化。评估一下这里是否需要把module里的脚本先转化
    // 将目标数据解析，并放入相应的class中
    public static void loadModule() {
        String modulePath = BASE_PATH+"module/";
        // 读取目标脚本文件名
        readNameList(modulePath);

        System.out.println("加载模板文件...");
        for (String fileName : moduleFileNames) {
            String realFileName = realFileName(fileName);
            System.out.println(modulePath + fileName);
            moduleDict.put(realFileName, new Container());
            read(moduleDict.get(realFileName), modulePath + fileName); // 读取原始文件放入内存
        }
    }

    private static void readMacro() {

        List<String> ls;
        try {
            ls = Files.readAllLines(Paths.get(BASE_PATH+"macro/"));
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


    private static void readDict(String path) {
        if (dict == null) {
            dict = new HashMap<>();
        }

        List<String> ls;
        try {
            ls = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        for (String s : ls) {
            int blankPos = s.indexOf(" ");
            dict.put(s.substring(0, blankPos), s.substring(blankPos + 1));
        }
    }

    public static void read(Container container, String path) {
        try {
            container.rawLines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public static void write(Container container, String path) {
        File file = new File(path);
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false)))) {
            StringBuffer sb = new StringBuffer();
            for (String line : container.resLines) {
                sb.append(line);
                sb.append("\n");
            }
            bw.write(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String realFileName(String name) {
        if (!name.contains(".")) {
            return name;
        }
        int pos = name.lastIndexOf(".");
        return name.substring(0, pos);
    }

    // 读取目录下所有文件的文件名
    private static void readNameList(String basePath) {
        String[] array = new File(basePath).list();
        for (int i = 0; i < array.length; ++i) {
            moduleFileNames.add(array[i]);
        }
    }
}
