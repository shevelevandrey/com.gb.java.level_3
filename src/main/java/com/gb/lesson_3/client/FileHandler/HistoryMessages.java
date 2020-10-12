package com.gb.lesson_3.client.FileHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryMessages implements FileHandler {
    private File file;

    public HistoryMessages(String fileName) {
        try {
            file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeMessage(String line) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(file, true);
            fw.write(line);
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<String> getData(int lineCount) {
        List<String> fileData = new ArrayList();
        List<String> outData = new ArrayList();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(file));

            String line;
            while((line = br.readLine()) != null) {
                fileData.add(line);
            }

            if (fileData.size() <= lineCount) {
                outData = fileData;
            } else {
                for (int i = fileData.size() - lineCount; i < fileData.size(); i++) {
                    outData.add(fileData.get(i));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return outData;
    }
}
