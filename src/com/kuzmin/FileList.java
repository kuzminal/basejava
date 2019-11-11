package com.kuzmin;

import java.io.File;

public class FileList {
    public static void main(String[] args) {
        File file = new File("./src");
        printFilesFromDirectory(file);
    }

    private static void printFilesFromDirectory(File file) {
        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                printFilesFromDirectory(f);
            } else {
                System.out.println(f.getName());
            }
        }
    }
}
