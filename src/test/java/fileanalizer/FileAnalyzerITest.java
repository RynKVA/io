package fileanalizer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

class FileAnalyzerITest {
    private File fileWithTextContainWord;
    private File fileWithNotContainWord;
    private File emptyFile;
    private final String word = "собака";

    @BeforeEach
    void before() throws IOException {
        File directory = new File("src", "testiolab");
        directory.mkdir();
        fileWithTextContainWord = new File("src\\testiolab", "TestTextFile");
        fileWithTextContainWord.createNewFile();
        fileWithNotContainWord = new File("src\\testiolab", "TestTextFile2");
        fileWithNotContainWord.createNewFile();
        emptyFile = new File("src\\testiolab", "TestTextFile3");
        emptyFile.createNewFile();
        try (FileOutputStream streamFileWithText = new FileOutputStream(fileWithTextContainWord);
             FileOutputStream streamFileNotContainWord = new FileOutputStream(fileWithNotContainWord);
             FileOutputStream streamFileWithoutText = new FileOutputStream(emptyFile)) {
            streamFileWithText.write("Сегодня утром я вышел на улицу и там была большая дружелюбная собака. Она мчалась по аллее, за ней громко лаял маленький щенок! Какое забавное зрелище! Я радовался каждому их движению и остановке. Подумалось мне, что жизнь, в которой есть собака, была бы настоящим приключением! Вдруг собака остановилась, посмотрела на меня внимательно и кивнула головой? Я не мог отказать такой милой просьбе и пошел бы на прогулку с новыми друзьями. Весь день мы бы шли по парку, играли, бегали и наслаждались природой.".getBytes());
            streamFileNotContainWord.write("Рыжий кот по имени Томас обожает лежать на солнышке и мурлыкать в такт пение птиц! Его мягкая шерсть блестит на солнце, словно огонь! Томас часто прячется в кустах, чтобы подкараулить прохожих и испугать их! Когда Томасу хочется веселиться, он начинает ловить бабочек и играть с ними до упаду!. А все ли соседи завидуют Томасу за его изящную осанку и яркий рыжий окрас?".getBytes());
            streamFileWithoutText.write("".getBytes());
        }
    }

    @Test
    @DisplayName("Analyzed file with text and found three touching word \"собака\", return 3")
    void countWordThree() throws IOException {
        try(FileInputStream stream = new FileInputStream(fileWithTextContainWord.getPath())) {
            assertEquals(3, FileAnalyzer.countSpecifiedWord(stream.readAllBytes(), word));
        }
    }

    @Test
    @DisplayName("Analyzed file without finding word, return 0")
    void countWordZero() throws IOException {
        try (FileInputStream stream = new FileInputStream(fileWithNotContainWord.getPath())){
            assertEquals(0, FileAnalyzer.countSpecifiedWord(stream.readAllBytes(), word));
        }
    }

    @Test
    @DisplayName("Analyzed empty file, return 0")
    void countWordInEmptyFileReturnZero() throws IOException {
        try (FileInputStream stream = new FileInputStream(emptyFile.getPath())) {
            assertEquals(0, FileAnalyzer.countSpecifiedWord(stream.readAllBytes(), word));
        }
    }

    @Test
    @DisplayName("When file on specified path is not exist expect NoSuchElementException")
    void fileNotExist() {
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> FileAnalyzer.validatePathCorrectness("testiolab\\testlab\\notexistpath"));
        assertEquals("File on this path: testiolab\\testlab\\notexistpath is not exist.", exception.getMessage());
    }

    @Test
    @DisplayName("When divine on sentences than return list of sentences contain word \"собака\" with length 3")
    void divineOnSentences() throws IOException {
        try (FileInputStream stream = new FileInputStream(fileWithTextContainWord.getPath())) {
            ArrayList<String> listOfNeededSentences = FileAnalyzer.divineOnNeededSentences(stream.readAllBytes(), word);
            assertEquals(3, listOfNeededSentences.size());

            assertEquals("Сегодня утром я вышел на улицу и там была большая дружелюбная собака.", listOfNeededSentences.get(0));
            assertEquals(" Подумалось мне, что жизнь, в которой есть собака, была бы настоящим приключением!", listOfNeededSentences.get(1));
            assertEquals(" Вдруг собака остановилась, посмотрела на меня внимательно и кивнула головой?", listOfNeededSentences.get(2));
        }
    }

    @Test
    @DisplayName("When divine on sentences which don't contain word \"собака\" than return empty list")
    void divineOnSentencesWithFileNotContainWord() throws IOException {
        try (FileInputStream stream = new FileInputStream(fileWithNotContainWord.getPath())) {
            ArrayList<String> targetList = FileAnalyzer.divineOnNeededSentences(stream.readAllBytes(), word);

            assertTrue(targetList.isEmpty());
        }
    }

    @Test
    @DisplayName("When divine on sentences in empty file than return empty list")
    void divineOnSentencesWithEmptyFile() throws IOException {
        try (FileInputStream stream = new FileInputStream(emptyFile.getPath())) {
            ArrayList<String> targetList = FileAnalyzer.divineOnNeededSentences(stream.readAllBytes(), word);

            assertTrue(targetList.isEmpty());
        }
    }

    @Test
    @DisplayName("When set in method testAnalyzer exist path and needed word then return exemplar of NeededContent which include information of neededSentences and countSpecifiedWord")
    void testFileAnalyzer() throws IOException {
        NeededContent neededContent = FileAnalyzer.fileAnalyzer("src\\testiolab\\TestTextFile", "собака");
        ArrayList<String> neededSentences = neededContent.getNeededSentences();

        assertEquals("Сегодня утром я вышел на улицу и там была большая дружелюбная собака.", neededSentences.get(0));
        assertEquals(" Подумалось мне, что жизнь, в которой есть собака, была бы настоящим приключением!", neededSentences.get(1));
        assertEquals(" Вдруг собака остановилась, посмотрела на меня внимательно и кивнула головой?", neededSentences.get(2));

        assertEquals(3, neededContent.getCountSpecifiedWord());
    }

    @AfterEach
    void after() {
        fileWithTextContainWord.delete();
        fileWithNotContainWord.delete();
        emptyFile.delete();
        File directory = new File("src", "testiolab");
        directory.delete();
    }
}

