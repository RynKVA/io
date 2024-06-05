package com.rynkovoy.ioservices.fileanalizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class FileAnalyzer {
    private static final Pattern SENTENCE_PATTERN = Pattern.compile("((?<=[.!?]))");

    public static NeededContent fileAnalyzer() throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            String path = scanner.nextLine();
            String word = scanner.nextLine();
            return fileAnalyzer(path, word);
        }
    }

    static NeededContent fileAnalyzer(String path, String word) throws IOException {
        validatePathCorrectness(path);
        try (FileInputStream stream = new FileInputStream(path)) {
            byte[] allBytesFromFile = stream.readAllBytes();
            ArrayList<String> neededSentences = divineOnNeededSentences(allBytesFromFile, word);
            int countSpecifiedWord = countSpecifiedWord(neededSentences, word);
            return new NeededContent(neededSentences, countSpecifiedWord);
        }
    }

    static int countSpecifiedWord(List<String> neededSentences, String word) {
        int countWord = 0;
        for (String neededSentence : neededSentences) {
            int index = 0;
            while (index != -1) {
                index = neededSentence.indexOf(word, index);
                if (index == -1) {
                    continue;
                }
                countWord++;
                index++;
            }
        }
        return countWord;
    }

    static ArrayList<String> divineOnNeededSentences(byte[] allBytes, String word) {
        String content = new String(allBytes);
        String[] splitSentences = SENTENCE_PATTERN.split(content);
        ArrayList<String> contentWithNeededWord = new ArrayList<>();
        for (String splitSentence : splitSentences) {
            if (splitSentence.contains(word)) {
                contentWithNeededWord.add(splitSentence);
            }
        }
        return contentWithNeededWord;
    }

    static void validatePathCorrectness(String path) {
        if (!new File(path).exists()) {
            throw new NoSuchElementException("File on this path: " + path + " is not exist.");
        }
    }
}
