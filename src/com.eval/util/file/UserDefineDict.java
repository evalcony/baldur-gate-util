package com.eval.util.file;

import java.util.HashMap;
import java.util.Map;

public class UserDefineDict implements Dict {

    private static String path;
    private static Map<String, String> dict = null;

    public UserDefineDict(String path) {
        this.path = path;
    }

    @Override
    public void loadSource() {
        System.out.println("加载dict文件...");
        readDict(path);
    }

    @Override
    public Map<String, String> getSource() {
        return dict;
    }

    private void readDict(String path) {
        if (dict == null) {
            dict = new HashMap<>();
        }
        new FileProcessor().read(dict, path);
    }

}
