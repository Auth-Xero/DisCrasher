package com.authxero.discrash.helpers;
import com.authxero.discrash.storage.FileSystemStorageService;

import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

public class GifHelper extends Helper{
    public static GifHelper instance =  null;

    public GifHelper() {
        super();
        instance = this;
    }

    public String generateCrashVideo(String fileNameInput) throws Exception {
        try{
            System.out.println("Beginning to generate crash gif "+ fileNameInput);
            String tempFileName = UtilHelper.generateFileName(32);
            String tempFileName2 = UtilHelper.generateFileName(32);
            Files.move(FileSystemStorageService.rootLocation.resolve(fileNameInput),FileSystemStorageService.outputLocation.resolve(fileNameInput));
            UtilHelper.createFileAndWrite(FileSystemStorageService.outputLocation.resolve(tempFileName+".txt").toAbsolutePath().toString(),"file "+fileNameInput+"\nfile 1.mp4");
            Process proc2 = ces.fireAndReturnProcess("ffmpeg -f concat -i "+FileSystemStorageService.outputLocation.resolve(tempFileName+".txt").toAbsolutePath()+" -codec copy "+FileSystemStorageService.outputLocation.resolve(tempFileName2+".mp4"));
            proc2.waitFor(5, TimeUnit.MINUTES);
            UtilHelper.deleteFile(tempFileName+".txt");
            UtilHelper.deleteFile(FileSystemStorageService.outputLocation.resolve(fileNameInput).toAbsolutePath().toString());
            UtilHelper.deleteFile(fileNameInput);
            FireAndForgetRunnable forgetRunnableHelper = new FireAndForgetRunnable(() -> {
                TimeUnit.MINUTES.sleep(2);
                UtilHelper.deleteFile(tempFileName2+".mp4");
            });
            ffe.exec(forgetRunnableHelper);
            System.out.println(tempFileName2+" generated! File will be deleted in 2 minutes.");
            return GfycatHelper.uploadVideo(FileSystemStorageService.outputLocation.resolve(tempFileName2+".mp4"));
        }
        catch (Exception ex){
            throw new Exception(ex.getMessage(),ex);
        }
    }

}
