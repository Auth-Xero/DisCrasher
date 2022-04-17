package com.authxero.discrash.helpers;

import com.authxero.discrash.executor.FireAndForgetExecutor;
import com.authxero.discrash.storage.FileSystemStorageService;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class EndlessVideoHelper {
    public static EndlessVideoHelper instance;
    public static FireAndForgetExecutor ffe = FireAndForgetExecutor.instance;

    public EndlessVideoHelper() {
        instance = this;
    }


    //hacky, veryyyyyyyy hacky
    public String generateVideo(String fileNameInput, boolean isEndless) throws Exception {
        try {
            String type = isEndless ? "endless" : "negative";
            System.out.println("Beginning to generate " + type + " file " + fileNameInput);
            String tempFileName = UtilHelper.generateFileName(32);
            String[] ia = isEndless ? new String[]{"00", "01", "7f", "ff", "ff", "ff"} : new String[]{"00", "01", "ff", "ff", "ff", "f0"};
            String iaS = "6d 76 68 64";
            byte[] ba = Files.readAllBytes(FileSystemStorageService.rootLocation.resolve(fileNameInput));
            String hexArr = UtilHelper.toHexFromBytes(ba);
            int idx1 = hexArr.toLowerCase().indexOf(iaS);
            String[] thisThingy = hexArr.toLowerCase().substring(idx1,hexArr.length()).split(" ");
            int idx2 = UtilHelper.indexOfStringArrayNotNull(thisThingy,18);
            String[] replaced = UtilHelper.stringArayReplace(thisThingy, ia,idx2);
            String[] thisThingy2 = UtilHelper.concatWithArrayCopy(hexArr.toLowerCase().substring(0,idx1).split(" "),replaced);
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
