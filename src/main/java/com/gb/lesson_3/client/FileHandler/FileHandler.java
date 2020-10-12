package com.gb.lesson_3.client.FileHandler;

import java.util.List;

public interface FileHandler {
    void writeMessage(String line);
    List<String> getData(int lineCount);
}
