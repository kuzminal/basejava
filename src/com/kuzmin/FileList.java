package com.kuzmin;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FileList {
    private static String tab = "";
    public static void main(String[] args) {
        File file = new File("./src");
        printFilesFromDirectory(file);
    }

    private static void printFilesFromDirectory(File file) {
        if (file.listFiles() != null) {
            List<File> fileList = Arrays.asList(file.listFiles());
            fileList.sort(Comparator.naturalOrder());
            for (File f : fileList) {
                if (f.isDirectory()) {
                    System.out.println(tab + f.getName());
                    tab += "\t";
                    printFilesFromDirectory(f);
                } else {
                    System.out.println(tab + f.getName());
                }
            }
        }
    }
}
