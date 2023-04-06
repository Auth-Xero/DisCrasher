package com.authxero.discrash.storage;

import com.authxero.discrash.helpers.UtilHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    public static Path rootLocation = null;
    public static Path outputLocation = null;
    public static Path wwmLocation = null;
    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        rootLocation = Paths.get(properties.getLocation());
        outputLocation = Paths.get(properties.getOutputDir());
        wwmLocation = Paths.get(properties.getWwmDir());
    }

    @Override
    public String putFile(MultipartFile file, boolean useRandomFileName) {
        String filePath = null;
        try {
            if (file.isEmpty()) {
                throw new StorageException("Cannot store empty file!");
            }
            String fileName = useRandomFileName ? UtilHelper.generateFileName(32)+".mp4" : file.getOriginalFilename();
            Path destinationFile = rootLocation.resolve(Paths.get(fileName)).normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
                throw new StorageException("Cannot store file outside of set directory!");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            filePath = fileName;
        } catch (IOException e) {
            throw new StorageException("Failed to store file!", e);
        }
        return filePath;
    }

    @Override
    public Stream<Path> getAll() {
        try {
            return Files.walk(outputLocation, 1)
                    .filter(path -> !path.equals(outputLocation))
                    .map(outputLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read all files", e);
        }
    }

    @Override
    public Path getFile(String filename) {
        return outputLocation.resolve(filename);
    }

    @Override
    public Resource getFileAsResource(String filename) {
        try {
            Path file = getFile(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                        "Could not read selected file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read selected file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
        FileSystemUtils.deleteRecursively(outputLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
            Files.createDirectories(outputLocation);
            Resource resource = new ClassPathResource("black.webm");
            InputStream resourceInputStream = resource.getInputStream();
            Files.copy(resourceInputStream,outputLocation.resolve("black.webm"));
            Resource resource2 = new ClassPathResource("1.mp4");
            InputStream resourceInputStream2 = resource2.getInputStream();
            Files.copy(resourceInputStream2,outputLocation.resolve("1.mp4"));
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
