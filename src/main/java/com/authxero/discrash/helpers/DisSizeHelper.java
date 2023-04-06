package com.authxero.discrash.helpers;

import com.authxero.discrash.storage.FileSystemStorageService;

import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

public class DisSizeHelper extends Helper {
    public static DisSizeHelper instance = null;

    public DisSizeHelper() {
        super();
        instance = this;
        getWWM();
    }

    public String generateVideo(String fileNameInput, String type) throws Exception {
        try {
            System.out.println("Beginning to generate resized " + type + " video " + fileNameInput);
            String tempFileName2 = UtilHelper.generateFileName(32);
            Files.move(FileSystemStorageService.rootLocation.resolve(fileNameInput), FileSystemStorageService.outputLocation.resolve(fileNameInput));
            String cmd = "node ./wwm/wackywebm.js -b 400000 --output \"./temp-out/" +tempFileName2 + ".webm\" " + type + " \"" + FileSystemStorageService.outputLocation.resolve(fileNameInput) + "\"";
            System.out.println(cmd);
            Process proc2 = ces.fireAndReturnProcess(cmd);
            proc2.waitFor(5, TimeUnit.MINUTES);
            UtilHelper.deleteFile(FileSystemStorageService.rootLocation.resolve(fileNameInput).toAbsolutePath().toString());
            FireAndForgetRunnable forgetRunnableHelper = new FireAndForgetRunnable(() -> {
                TimeUnit.MINUTES.sleep(10);
                UtilHelper.deleteFile(tempFileName2 + ".webm");
            });
            ffe.exec(forgetRunnableHelper);
            System.out.println(tempFileName2 + " generated! File will be deleted in 10 minutes.");
            return tempFileName2 + ".webm";
        } catch (Exception ex) {
            throw new Exception(ex.getMessage(), ex);
        }
    }

    private void getWWM() {
        try {
            if (!FileSystemStorageService.wwmLocation.toFile().exists()) {
                Process proc = ces.fireAndReturnProcess("git clone https://github.com/Auth-Xero/WackyWebM.git wwm");
                proc.waitFor(2, TimeUnit.MINUTES);
                Process proc2 = ces.fireAndReturnProcess("cd wwm && npm install && cd ..");
                proc2.waitFor(3, TimeUnit.MINUTES);
            }
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
        }
    }
}
