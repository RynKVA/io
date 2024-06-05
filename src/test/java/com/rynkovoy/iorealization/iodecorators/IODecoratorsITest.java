package com.rynkovoy.iorealization.iodecorators;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;


@SuppressWarnings("ResultOfMethodCallIgnored")
public class IODecoratorsITest {
    private File inputTestFile;
    private File outputTestFile;
    private File directory;

    @BeforeEach
    void before() throws IOException {
        directory = new File("src/io");
        directory.mkdir();
        inputTestFile = new File(directory.getPath(), "inputtest");
        outputTestFile = new File(directory.getPath(), "outputtest");
        try (FileOutputStream out = new FileOutputStream(inputTestFile)) {
            out.write("hello".getBytes());
        }
    }

    @Test
    @DisplayName("When use method read then byte from inputStream write in buffer and return this byte")
    public void testReadWithoutParameters() throws IOException {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("src/io/inputtest"))) {
            assertEquals('h', (char) bufferedInputStream.read());
            assertEquals('e', (char) bufferedInputStream.read());
            assertEquals('l', (char) bufferedInputStream.read());
            assertEquals('l', (char) bufferedInputStream.read());
            assertEquals('o', (char) bufferedInputStream.read());
            assertEquals(-1, bufferedInputStream.read());
        }
    }

    @Test
    @DisplayName("When use method read with parameter array, then byte from inputStream write in buffer and filling target array, return quantity of read bytes")
    public void testReadByteArray() throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("src/io/inputtest"), 10);
        byte[] array = new byte[7];
        int count = bufferedInputStream.read(array);

        assertEquals(7, count);
        assertEquals('h', (char) array[0]);
        assertEquals('e', (char) array[1]);
        assertEquals('l', (char) array[2]);
        assertEquals('l', (char) array[3]);
        assertEquals('o', (char) array[4]);
        assertEquals(0, array[5]);
        assertEquals(0, array[6]);

        bufferedInputStream.close();
    }

    @Test
    @DisplayName("When use method read with parameter array,offSet and length then byte from inputStream write in buffer and filling target array from offSet position on lengths value bytes")
    public void testReadByteArrayWithOffSetAndLengthParameters() throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("src/io/inputtest"), 10);
        byte[] array = new byte[7];
        int count = bufferedInputStream.read(array, 1, 5);

        assertEquals(5, count);
        assertEquals(0, array[0]);
        assertEquals('h', (char) array[1]);
        assertEquals('e', (char) array[2]);
        assertEquals('l', (char) array[3]);
        assertEquals('l', (char) array[4]);
        assertEquals('o', (char) array[5]);
        assertEquals(0, array[6]);

        bufferedInputStream.close();
    }

    @Test
    @DisplayName("When use method write, then write byte in buffer, then method flush drop all bytes in outputStream")
    void testWriteFlushAndReadWithoutParameters() throws IOException {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("src/io/outputtest"));
             BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("src/io/outputtest"))) {

            bufferedOutputStream.write(104);
            bufferedOutputStream.write(101);
            bufferedOutputStream.write(108);
            bufferedOutputStream.write(108);
            bufferedOutputStream.write(111);
            bufferedOutputStream.flush();

            assertEquals('h', (char) bufferedInputStream.read());
            assertEquals('e', (char) bufferedInputStream.read());
            assertEquals('l', (char) bufferedInputStream.read());
            assertEquals('l', (char) bufferedInputStream.read());
            assertEquals('o', (char) bufferedInputStream.read());
            assertEquals(-1, bufferedInputStream.read());
        }
    }

    @Test
    @DisplayName("When use method write with parameter array, then bytes from target array filling buffer, then method flush drop all bytes in outputStream")
    void testWriteByteArrayAndFlush() throws IOException {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("src/io/outputtest"));
             BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("src/io/outputtest"))) {
            byte[] array = {104, 101, 108, 108, 111};
            bufferedOutputStream.write(array);
            bufferedOutputStream.flush();

            assertEquals('h', (char) bufferedInputStream.read());
            assertEquals('e', (char) bufferedInputStream.read());
            assertEquals('l', (char) bufferedInputStream.read());
            assertEquals('l', (char) bufferedInputStream.read());
            assertEquals('o', (char) bufferedInputStream.read());
            assertEquals(-1, bufferedInputStream.read());
        }
    }

    @Test
    @DisplayName("When use method write with parameter array, offSet and length, then from target array offSet position on lengths value bytes filling buffer, then method flush drop all bytes in outputStream")
    void testWriteByteArrayWithOffSetAndLengthParameters() throws IOException {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("src/io/outputtest"));
             BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("src/io/outputtest"))) {
            byte[] array = {104, 101, 108, 108, 111};
            bufferedOutputStream.write(array, 1, 4);
            bufferedOutputStream.flush();

            assertEquals('e', (char) bufferedInputStream.read());
            assertEquals('l', (char) bufferedInputStream.read());
            assertEquals('l', (char) bufferedInputStream.read());
            assertEquals('o', (char) bufferedInputStream.read());
            assertEquals(-1, bufferedInputStream.read());
        }
    }

    @AfterEach
    void after() {
        inputTestFile.delete();
        outputTestFile.delete();
        directory.delete();
    }
}
