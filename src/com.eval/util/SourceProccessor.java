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
    private static Map<String, String> ids = null;
    private static Map<String, String> macroCmdBlock = new HashMap<>();
    private static Map<String, String> macroReplaceBlock = new HashMap<>();
    private static Map<String, Container> moduleDict = new HashMap<>();
    private static List<String> moduleFileNames = new ArrayList<>();
    private static List<String> idsFileNames = new ArrayList<>();

    public static Map<String, String> getDict() {
        return dict;
    }
    public static Map<String, String> getIds() {
        return ids;
    }
    public static Map<String, Container> getModuleDict() {
        return moduleDict;
    }
    public static List<String> getModuleFileNames() {
        return moduleFileNames;
    }


    public static void loadResources(String dictPath) {
        loadDict(dictPath);
        loadIds();
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

    public static void loadIds() {
        String idsPath = BASE_PATH+"ids/";
        idsFileNames = readNameList(idsPath);
        System.out.println("加载ids文件...");
        for (String fileName : idsFileNames) {
            System.out.println(idsPath+fileName);
            readIds(idsPath+fileName);
        }
    }

    // 将目标数据解析，并放入相应的class中
    public static void loadModule() {
        String modulePath = BASE_PATH+"module/";
        // 读取目标脚本文件名
        moduleFileNames = readNameList(modulePath);

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
        read(dict,path);
    }

    private static void readIds(String path) {
        if (ids == null) {
            ids = new HashMap<>();
        }
        read(ids,path);
    }

    private static void read(Map<String,String> map, String path) {
        List<String> ls;
        try {
            ls = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        for (String s : ls) {
            int blankPos = s.indexOf(" ");
            map.put(s.substring(0, blankPos), s.substring(blankPos + 1));
        }
    }

    public static void read(Container container, String path) {
        try {
            container.rawLines = Files.readAllLines(Paths.get(path));

            for (int i = 0; i < container.rawLines.size(); ++i) {
                container.rawLines.set(i, container.rawLines.get(i).trim());
            }
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
    private static List<String> readNameList(String basePath) {
        List<String> nameList = new ArrayList<>();
        String[] array = new File(basePath).list();
        for (int i = 0; i < array.length; ++i) {
            nameList.add(array[i]);
        }
        return nameList;
    }
}
