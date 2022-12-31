package com.eval.util.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdsDict implements Dict {

    private static String path;
    private static Map<String, String> ids = null;
    private static List<String> idsFileNames = new ArrayList<>();

    public IdsDict(String path) {
        this.path = path;
    }

    @Override
    public void loadSource() {
        idsFileNames = new FileProcessor().readNameList(path);
        System.out.println("加载ids文件...");
        for (String fileName : idsFileNames) {
            System.out.println(path+fileName);
            readIds(path+fileName);
        }
    }

    @Override
    public Map<String, String> getSource() {
        return ids;
    }


    private void readIds(String path) {
        if (ids == null) {
            ids = new HashMap<>();
        }
        new FileProcessor().read(ids, path);
    }

}
