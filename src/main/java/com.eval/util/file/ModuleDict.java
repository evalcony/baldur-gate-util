package com.eval.util.file;

import com.eval.Container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleDict implements Dict {

    private static String path;
    private static List<String> moduleFileNames = new ArrayList<>();
    private static Map<String, Container> moduleDict = new HashMap<>();

    public ModuleDict(String path) {
        this.path = path;
    }

    @Override
    public void loadSource() {
        // 读取目标脚本文件名
        moduleFileNames = new FileProcessor().readNameList(path);

        System.out.println("加载模板文件...");
        for (String fileName : moduleFileNames) {
            String realFileName = realFileName(fileName);
            System.out.println(path + fileName);
            moduleDict.put(realFileName, new Container());
            new FileProcessor().read(moduleDict.get(realFileName), path + fileName);
        }
    }

    @Override
    public Map getSource() {
        return moduleDict;
    }

    private String realFileName(String name) {
        if (!name.contains(".")) {
            return name;
        }
        int pos = name.lastIndexOf(".");
        return name.substring(0, pos);
    }
}
