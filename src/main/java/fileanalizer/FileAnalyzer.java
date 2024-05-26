package fileanalizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class FileAnalyzer {
    private static final Pattern SENTENCE_PATTERN = Pattern.compile("((?<=[.!?]))");

    public static NeededContent fileAnalyzer() throws IOException {
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();
        String word = scanner.nextLine();
        return fileAnalyzer(path, word);
    }

    static NeededContent fileAnalyzer(String path, String word) throws IOException {
        validatePathCorrectness(path);
        try (FileInputStream stream = new FileInputStream(path)) {
            byte[] allBytesFromFile = stream.readAllBytes();
            ArrayList<String> neededSentences = divineOnNeededSentences(allBytesFromFile, word);
            int countSpecifiedWord = countSpecifiedWord(allBytesFromFile, word);
            return new NeededContent(neededSentences, countSpecifiedWord);
        }
    }

    static int countSpecifiedWord(byte[] allBytes, String word) {
        int index = 0;
        int countWord = 0;
        byte[] wordBytes = word.getBytes();
        for (byte oneByte : allBytes) {
            if (oneByte == wordBytes[index]) {
                index++;
                if (index == wordBytes.length) {
                    countWord++;
                    index = 0;
                }
            } else {
                index = 0;
            }
        }
        return countWord;
    }

    static ArrayList<String> divineOnNeededSentences(byte[] allBytes, String word) {
        String content = new String(allBytes, StandardCharsets.UTF_8);
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
