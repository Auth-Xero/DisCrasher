package com.authxero.discrash.helpers;

import com.authxero.discrash.storage.FileSystemStorageService;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class UtilHelper {
    public static String generateFileName(int length) {
        String availableChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) {
            int index = (int) (rnd.nextFloat() * availableChars.length());
            salt.append(availableChars.charAt(index));
        }
        return salt.toString();
    }

    public static boolean verifyFileHeader(MultipartFile file, int[][] headers) throws IOException {
        InputStream ins = file.getInputStream();
        try {
            ins.skip(4);
            for (int[] header : headers) {
                if(doesMatchHeader(ins, header)){
                    return true;
                }
            }
        return false;
        } finally {
            ins.close();
        }
    }

    private static boolean doesMatchHeader(InputStream inputStream,int[] header) throws IOException {
        for (int i = 0; i < header.length; ++i) {
            if (inputStream.read() != header[i]) {
                return false;
            }
        }
        return true;
    }


    public static String[] stringArayReplace(String[] a,String[] b,int replaceStart){
        String[] output = a;
        for (int i = 0; i < b.length; i++) {
            a[replaceStart+i] = b[i];
        }
        return output;
    }
    private final static String[] hexSymbols = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public final static int BITS_PER_HEX_DIGIT = 4;

    public static String toHexFromByte(final byte b)
    {
        byte leftSymbol = (byte)((b >>> BITS_PER_HEX_DIGIT) & 0x0f);
        byte rightSymbol = (byte)(b & 0x0f);

        return (hexSymbols[leftSymbol] + hexSymbols[rightSymbol]);
    }

    public static String toHexFromBytes(final byte[] bytes)
    {
        if(bytes == null || bytes.length == 0)
        {
            return ("");
        }

        // there are 2 hex digits per byte
        StringBuilder hexBuffer = new StringBuilder(bytes.length * 2);

        // for each byte, convert it to hex and append it to the buffer
        for(int i = 0; i < bytes.length; i++)
        {
            hexBuffer.append(toHexFromByte(bytes[i])+" ");
        }

        return (hexBuffer.toString());
    }

    static <T> T[] concatWithArrayCopy(T[] array1, T[] array2) {
        T[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    public static int indexOfStringArrayNotNull(String[] outerArray,int start) {
        for (int i = start; i < outerArray.length; i++) {
            if (!Objects.equals(outerArray[i], "00")) {
                return i;
            }
        }
        return -1;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static void createFileAndWrite(String fileName, String textToWrite) throws IOException {
        PrintWriter writer = new PrintWriter(FileSystemStorageService.outputLocation.resolve(fileName).toAbsolutePath().toString(), StandardCharsets.UTF_8);
        writer.print(textToWrite);
        writer.close();
    }
    public static void createFileAndWriteBytes(String fileName, byte[] bytesToWrite){
        File outputFile = FileSystemStorageService.outputLocation.resolve(fileName).toFile();
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(bytesToWrite);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFile(String fileName) {
        File fileToDelete = FileSystemStorageService.outputLocation.resolve(fileName).toFile();
        if (fileToDelete.delete()) {
            System.out.println("Deleted " + fileToDelete.getName());
        } else {
            System.out.println("Failed to delete " + fileToDelete.getName());
        }
    }
}
