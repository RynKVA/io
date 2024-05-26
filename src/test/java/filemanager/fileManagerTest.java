package filemanager;

import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
class FileManagerITest {
    private File textFile;
    private File textFile2;
    private File textFile3;
    private File textFile4;
    private File textFile5;
    private File textFile6;
    private File textFile7;
    private File textFile8;

    @BeforeEach
    void before() throws IOException {
        File mainDirectory = new File("src", "testiolab");
        mainDirectory.mkdir();
        File secondDirectory = new File("src\\testiolab", "TESTLAB");
        secondDirectory.mkdir();

        textFile = new File(secondDirectory.getPath(), "TestTextFile");
        textFile2 = new File(secondDirectory.getPath(), "TestTextFile2");
        textFile3 = new File(secondDirectory.getPath(), "TestTextFile3");
        try (FileOutputStream fileOutputStream = new FileOutputStream(textFile);
             FileOutputStream fileOutputStream1 = new FileOutputStream(textFile2);
             FileOutputStream fileOutputStream2 = new FileOutputStream(textFile3)) {
            fileOutputStream.write("Hello dear reader from textFile!".getBytes());
            fileOutputStream1.write("Hello dear reader from textFile2!".getBytes());
            fileOutputStream2.write("Hello dear reader from textFile3!".getBytes());
        }
        File testDirectory = new File(secondDirectory.getPath(), "Dir1");
        testDirectory.mkdir();

        textFile4 = new File(testDirectory.getPath(), "TestTextFile4");
        textFile4.createNewFile();
        textFile5 = new File(testDirectory.getPath(), "TestTextFile5");
        textFile5.createNewFile();
        textFile6 = new File(testDirectory.getPath(), "TestTextFile6");
        textFile6.createNewFile();
        textFile7 = new File(testDirectory.getPath(), "TestTextFile7");
        textFile7.createNewFile();

        File testDirectory2 = new File(secondDirectory.getPath(), "Dir2");
        testDirectory2.mkdir();
        File testInDirectory2 = new File(testDirectory2.getPath(), "InDir2");
        testInDirectory2.mkdir();
        textFile8 = new File(testInDirectory2.getPath(), "TestTextFile8");
        textFile8.createNewFile();
        File testDirectory3 = new File(secondDirectory.getPath(), "Dir3");
        testDirectory3.mkdir();
    }

    @Test
    @DisplayName("When used method countFiles in directory with seven files then return seven")
    void testMethodCountFilesInDirectoryWithSevenFilesReturnSeven() throws IOException {
        assertEquals(8, FileManager.countFiles("src\\testiolab"));
    }

    @Test
    @DisplayName("When used method countFiles and path lead to file then return one")
    void testMethodCountFilesWhenPathIsFileReturnOne() throws IOException {
        assertEquals(1, FileManager.countFiles("src\\testiolab\\TESTLAB\\TestTextFile"));
    }

    @Test
    @DisplayName("When used method countFiles in directory without files then return zero")
    void testMethodCountFilesInDirectoryWithoutFilesReturnZero() throws IOException {
        assertEquals(0, FileManager.countFiles("src\\testiolab\\TESTLAB\\Dir3"));
    }

    @Test
    @DisplayName("When used method countFiles with not exist path then expect IOException")
    void testMethodCountFilesWithNotExistPathExpectIOException() {
        IOException exception = assertThrows(IOException.class,
                () -> FileManager.countFiles("e: labs"));
        assertEquals("File on specified path:e: labs is not exist.", exception.getMessage());
    }

    @Test
    @DisplayName("When used method countDirs in directory with four directories then return four")
    void testMethodCountDirsInDirectoryWithFourDirectoriesReturnFour() throws IOException {
        assertEquals(5, FileManager.countDirs("src\\testiolab"));
    }

    @Test
    @DisplayName("When used method countDirs in directory without directories then return zero")
    void testMethodCountDirsInDirectoryWithoutDirectoriesReturnZero() throws IOException {
        assertEquals(0, FileManager.countDirs("src\\testiolab\\TESTLAB\\Dir3"));
    }

    @Test
    @DisplayName("When used method countDirs with not exist path then expect IOException")
    void testMethodCountDirsWithNotExistPathExpectIOException() {
        IOException exception = assertThrows(IOException.class,
                () -> FileManager.countDirs("e: labs"));
        assertEquals("File on specified path:e: labs is not exist.", exception.getMessage());
    }

    @Test
    @DisplayName("When directory or file which used is not exist then expect IOException")
    void whenDirectoryOrFileWhichUsedIsNotExistExpectIOException() {
        IOException exception = assertThrows(IOException.class,
                () -> FileManager.checkPathsOnSatisfactory(new File("e: labs"), new File("src\\testiolab\\TESTLAB\\Dir3")));
        assertEquals("File on specified path:e: labs is not exist.", exception.getMessage());
    }

    @Test
    @DisplayName("When directory or file to which moving or copying is not exist then expect IOException")
    void whenDirectoryToWhichMovingOrCopyingIsNotExistExpectIOException() {
        IOException exception = assertThrows(IOException.class,
                () -> FileManager.checkPathsOnSatisfactory(textFile, new File("e: labs")));
        assertEquals("File on specified path:e: labs is not exist.", exception.getMessage());
    }

    @Test
    @DisplayName("When path to which moving or copying is not directory then expect IOException")
    void whenPathToWhichMovingOrCopyingIsNotDirectoryExpectIOException() {
        IOException exception = assertThrows(IOException.class,
                () -> FileManager.checkPathsOnSatisfactory(new File("src\\testiolab\\TESTLAB\\Dir3"), textFile));
        assertEquals("Path: src\\testiolab\\TESTLAB\\TestTextFile, where you moving or copying, is not Directory.", exception.getMessage());
    }

    @Test
    @DisplayName("When path to which moving or copying is same as path from where then expect IOException")
    void whenPathToWhichMovingOrCopyingIsSameAsPathFromWhereExpectIOException() {
        File sameDirectory = new File("src\\testiolab\\TESTLAB\\Dir3");
        IOException exception = assertThrows(IOException.class,
                () -> FileManager.checkPathsOnSatisfactory(sameDirectory, sameDirectory));
        assertEquals("Path: src\\testiolab\\TESTLAB\\Dir3, where you moving or copying, is the same as path destination.", exception.getMessage());
    }

    @Test
    @DisplayName("When move file then file delete in directory from where moving, appear in destination directory and return true")
    void whenMoveFileThenFileDeleteInDirectoryFromWhereMovingAndAppearInTargetDirectoryReturnTrue() throws IOException {
        // return true as confirmation moved targetFile
        assertTrue(FileManager.move(textFile.getPath(), "src\\testiolab\\TESTLAB\\Dir2"));

        // checking exist new moving file's path
        assertTrue(new File("src\\testiolab\\TESTLAB\\Dir2\\TestTextFile").exists());
        File targetFile = new File("src\\testiolab\\TESTLAB\\Dir2\\TestTextFile");

        //checking not exist file with specified name in directory from where moved
        File directoryFrom = new File("src\\testiolab\\TESTLAB");
        File[] files = directoryFrom.listFiles();
        assert files != null;
        for (File file : files) {
            assertNotEquals("TestTextFile", file.getName());
        }

        // delete moving file
        targetFile.delete();
    }

    @Test
    @DisplayName("When move target directory then target in directory from where moving, appear in destination directory with all files within and return true")
    void whenMoveTargetDirectoryThenTargetDeleteInDirectoryFromWhereMovingAndAppearInDestinationDirectoryWithAllFilesWithinReturnTrue() throws IOException {
        // quantity files in targetDirectory before moving
        File[] filesBeforeMove = new File("src\\testiolab\\TESTLAB\\Dir1").listFiles();
        assert filesBeforeMove != null;
        assertEquals(4, filesBeforeMove.length);

        // return true as confirmation moved targetDirectory
        assertTrue(FileManager.move("src\\testiolab\\TESTLAB\\Dir1", "src\\testiolab\\TESTLAB\\Dir3"));

        File targetDirectory = new File("src\\testiolab\\TESTLAB\\Dir3\\Dir1");
        assertEquals("src\\testiolab\\TESTLAB\\Dir3\\Dir1", targetDirectory.getPath());

        // iterating in directoryFrom to know that targetDirectory was deleted
        File directoryFrom = new File("src\\testiolab\\TESTLAB");
        File[] files = directoryFrom.listFiles();
        assert files != null;
        for (File file : files) {
            assertNotEquals("Dir1", file.getName());
        }

        // quantity files in targetDirectory after moving
        File[] filesAfterMove = new File("src\\testiolab\\TESTLAB\\Dir3\\Dir1").listFiles();
        assert filesAfterMove != null;
        assertEquals(4, filesAfterMove.length);

        // delete all files
        for (File file : filesAfterMove) {
            file.delete();
        }
        targetDirectory.delete();
    }

    @Test
    @DisplayName("When method copy used on file to the target directory then file copied with all text within")
    void whenMethodCopyUsedOnFileToTheTargetDirectoryThenFileCopiedWithAllTextWithin() throws IOException {
        FileManager.copy(textFile.getPath(), "src\\testiolab\\TESTLAB\\Dir3");

        // compare expected file's path and name with actual
        File targetFile = new File("src\\testiolab\\TESTLAB\\Dir3\\TestTextFile");
        assertEquals("src\\testiolab\\TESTLAB\\Dir3\\TestTextFile", targetFile.getPath());
        assertEquals(textFile.getName(), targetFile.getName());

        // compare text with text in file which copied
        FileInputStream fileInputStream = new FileInputStream(targetFile.getPath());
        try (fileInputStream) {
            byte[] bytesOfText = fileInputStream.readAllBytes();
            assertEquals("Hello dear reader from textFile!", new String(bytesOfText, StandardCharsets.UTF_8));
        }

        //delete copied file
        targetFile.delete();
    }

    @Test
    @DisplayName("When method copy using on directory to the destination directory then directory copied with all files and directories within")
    void whenMethodCopyUsingOnDirectoryToTheTargetDirectoryThenDirectoryCopiedWithAllFilesAndDirectoriesWithin() throws IOException {
        FileManager.copy("src\\testiolab\\TESTLAB\\Dir2", "src\\testiolab\\TESTLAB\\Dir3");

        // compare expected directory's path and name with actual
        assertTrue(new File("src\\testiolab\\TESTLAB\\Dir3\\Dir2").exists());
        assertEquals("Dir2", new File("testiolab\\TESTLAB\\Dir3\\Dir2").getName());

        // compare expected innerDirectory's path and name with actual
        assertTrue(new File("src\\testiolab\\TESTLAB\\Dir3\\Dir2\\InDir2").exists());
        assertEquals("InDir2", new File("testiolab\\TESTLAB\\Dir3\\Dir2\\InDir2").getName());

        // compare expected innerFile's path and name with actual
        assertTrue(new File("src\\testiolab\\TESTLAB\\Dir3\\Dir2\\InDir2\\TestTextFile8").exists());
        assertEquals("TestTextFile8", new File("testiolab\\TESTLAB\\Dir3\\Dir2\\InDir2\\TestTextFile8").getName());

        // delete copied files
        File innerFile = new File("src\\testiolab\\TESTLAB\\Dir3\\Dir2\\InDir2\\TestTextFile8");
        innerFile.delete();
        File innerInnerDirectory = new File("src\\testiolab\\TESTLAB\\Dir3\\Dir2\\InDir2");
        innerInnerDirectory.delete();
        File ousterDirectory = new File("src\\testiolab\\TESTLAB\\Dir3\\Dir2");
        ousterDirectory.delete();
    }

    @AfterEach
    void afterEachMethodDeleteAllFiles() {
        textFile4.delete();
        textFile5.delete();
        textFile6.delete();
        textFile7.delete();
        textFile8.delete();
        textFile.delete();
        textFile2.delete();
        textFile3.delete();
        File testDirectory = new File("src\\testiolab\\TESTLAB\\Dir1");
        testDirectory.delete();
        File testInDirectory2 = new File("src\\testiolab\\TESTLAB\\Dir2\\InDir2");
        testInDirectory2.delete();
        File testDirectory2 = new File("src\\testiolab\\TESTLAB\\Dir2");
        testDirectory2.delete();
        File testDirectory3 = new File("src\\testiolab\\TESTLAB\\Dir3");
        testDirectory3.delete();
        File mainDirectory = new File("src\\testiolab\\TESTLAB");
        mainDirectory.delete();
        File testIOLab = new File("src\\testiolab");
        testIOLab.delete();
    }
}