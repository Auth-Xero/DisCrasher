package com.authxero.discrash.helpers;

import com.authxero.discrash.storage.FileSystemStorageService;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
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

    public static void createFileAndWrite(String fileName, String textToWrite) throws IOException {
        PrintWriter writer = new PrintWriter(FileSystemStorageService.outputLocation.resolve(fileName).toAbsolutePath().toString(), StandardCharsets.UTF_8);
        writer.print(textToWrite);
        writer.close();
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
