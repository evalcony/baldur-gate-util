package com.eval.util.file;

import com.eval.Container;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileProcessor {
    public void read(Map<String,String> map, String path) {
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

    public void read(Container container, String path) {
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

    public List<String> readNameList(String path) {
        List<String> nameList = new ArrayList<>();
        String[] array = new File(path).list();
        for (int i = 0; i < array.length; ++i) {
            nameList.add(array[i]);
        }
        return nameList;
    }

    public void write(Container container, String path) {
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

}
