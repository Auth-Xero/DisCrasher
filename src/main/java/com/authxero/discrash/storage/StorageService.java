package com.authxero.discrash.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

	void init();

	String putFile(MultipartFile file, boolean useRandomFileName);

	Stream<Path> getAll();

	Path getFile(String filename);

	Resource getFileAsResource(String filename);

	void deleteAll();

}
