package com.authxero.discrash.controller;

import com.authxero.discrash.executor.CommandExecutorService;
import com.authxero.discrash.helpers.FFMPEGHelper;
import com.authxero.discrash.helpers.UtilHelper;
import com.authxero.discrash.storage.StorageFileNotFoundException;
import com.authxero.discrash.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class DiscrashController {

    private final StorageService storageService;
    private FFMPEGHelper ffmpegHelper;
    public static int[][] MP4_HEADERS = new int[][]{
            {0x66, 0x74, 0x79, 0x70},
            {0x6d, 0x64, 0x61, 0x74},
            {0x6d, 0x6f, 0x6f, 0x76},
            {0x70, 0x6e, 0x6f, 0x74},
            {0x75, 0x64, 0x74, 0x61},
            {0x75, 0x75, 0x69, 0x64},
            {0x6d, 0x6f, 0x6f, 0x66},
            {0x66, 0x72, 0x65, 0x65},
            {0x73, 0x6b, 0x69, 0x70},
            {0x6a, 0x50, 0x32, 0x20},
            {0x77, 0x69, 0x64, 0x65},
            {0x6c, 0x6f, 0x61, 0x64},
            {0x63, 0x74, 0x61, 0x62},
            {0x69, 0x6d, 0x61, 0x70},
            {0x6d, 0x61, 0x74, 0x74},
            {0x6b, 0x6d, 0x61, 0x70},
            {0x63, 0x6c, 0x69, 0x70},
            {0x63, 0x72, 0x67, 0x6e},
            {0x73, 0x79, 0x6e, 0x63},
            {0x63, 0x68, 0x61, 0x70},
            {0x74, 0x6d, 0x63, 0x64},
            {0x73, 0x63, 0x70, 0x74},
            {0x73, 0x73, 0x72, 0x63},
            {0x50, 0x49, 0x43, 0x54},
    };

    @Autowired
    public DiscrashController(StorageService storageService) {
        this.storageService = storageService;
        this.ffmpegHelper = new FFMPEGHelper();
    }

    //DO NOT TURN ON THE 'debug' FLAG UNLESS YOU WANT FILE LISTING
    @GetMapping("/files")
    public String listUploadedFiles(Model model) throws IOException {
        if (CommandExecutorService.isDebug) {
            StringBuilder response = new StringBuilder();
            List<Path> respList = storageService.getAll().collect(Collectors.toList());
            for (Path pathEntry : respList) {
                response.append("<a href=\"./files/").append(pathEntry.getFileName()).append("\">").append(pathEntry.getFileName()).append("</a><br>");
            }
            return response.toString();
        } else {
            return "";
        }
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        if (filename.equals("black.webm")) return ResponseEntity.notFound().build();
        Resource file = storageService.getFileAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


    @PostMapping("/process")
    @ResponseBody
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            if (file.getOriginalFilename().endsWith(".mp4")) {
                if (UtilHelper.verifyFileHeader(file, MP4_HEADERS)) {
                    String fileName = storageService.putFile(file, true);
                    String outputFile = this.ffmpegHelper.generateCrashVideo(fileName);
                    return ResponseEntity.ok().body(outputFile);
                } else {
                    return ResponseEntity.badRequest().body("Please only upload mp4 files!");
                }
            } else {
                return ResponseEntity.badRequest().body("Please only upload mp4 files!");
            }
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }

    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}