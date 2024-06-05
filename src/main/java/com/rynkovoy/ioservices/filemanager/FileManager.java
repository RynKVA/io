package com.rynkovoy.ioservices.filemanager;

import java.io.*;

public class FileManager {
    public static int countFiles(String path) throws IOException {
        int countFiles = 0;
        File fileOrDirectory = new File(path);
        checkExistFile(fileOrDirectory);
        if (fileOrDirectory.isFile()){
            return 1;
        }
        File[] files = fileOrDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    String dirPath = file.getPath();
                    countFiles += countFiles(dirPath);
                } else {
                    countFiles++;
                }
            }
        }
        return countFiles;
    }

    public static int countDirs(String path) throws IOException {
        int countDirs = 0;
        File fileOrDirectory = new File(path);
        checkExistFile(fileOrDirectory);
        File[] files = fileOrDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    String dirPath = file.getPath();
                    countDirs += countDirs(dirPath);
                    countDirs++;
                }
            }
        }
        return countDirs;
    }

    public static boolean move(String pathFrom, String pathTo) throws IOException {
        File fileOrDirectory = new File(pathFrom);
        File targetDirectory = new File(pathTo);
        checkPathsOnSatisfactory(fileOrDirectory, targetDirectory);
        return fileOrDirectory.renameTo(new File(pathTo, fileOrDirectory.getName()));
    }

    public static void copy(String pathFrom, String pathTo) throws IOException {
        File fileOrDirectory = new File(pathFrom);
        File destinationDirectory = new File(pathTo);
        checkPathsOnSatisfactory(fileOrDirectory, destinationDirectory);
        if (fileOrDirectory.isDirectory()) {
            File copyDirectory = new File(destinationDirectory, fileOrDirectory.getName());
            copyDir(fileOrDirectory, copyDirectory);
        } else {
            String pathCopyFile = new File(destinationDirectory, fileOrDirectory.getName()).getPath();
            copyFile(pathFrom, pathCopyFile);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void copyDir(File directoryFrom, File pathTo) throws IOException {
        pathTo.mkdir();
        File[] files = directoryFrom.listFiles();
        assert files != null;
        for (File file : files) {
            copy(file.getPath(), pathTo.getPath());
        }
    }

    private static void copyFile(String pathFrom, String pathTo) throws IOException {
        FileInputStream source = new FileInputStream(pathFrom);
        FileOutputStream destination = new FileOutputStream(pathTo);
        try (source; destination) {
            byte[] allBytes = source.readAllBytes();
            destination.write(allBytes);
        }
    }

    public static void checkPathsOnSatisfactory(File fileOrDirectory, File targetDirectory) throws IOException {
        checkExistFile(fileOrDirectory);
        checkExistFile(targetDirectory);
        if (fileOrDirectory == targetDirectory) {
            throw new IOException("Path: " + targetDirectory.getPath() + ", where you moving or copying, is the same as path destination.");
        } else if (!targetDirectory.isDirectory()) {
            throw new IOException("Path: " + targetDirectory.getPath() + ", where you moving or copying, is not Directory.");
        }
    }

    private static void checkExistFile(File file) throws IOException {
        if (!file.exists()) {
            throw new IOException("File on specified path:" + file.getPath() + " is not exist.");
        }
    }
}
