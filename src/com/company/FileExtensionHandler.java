package com.company;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by Andrzej on 10.02.2019.
 */
public class FileExtensionHandler {

    String FilePath;

    byte[] FileContent;

    String SupposedFileExtension;

    public FileExtensionHandler(String filePath) {
        FilePath = filePath;
        SupposedFileExtension = getSupposedExt();
        try {
            FileContent = getFileContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getSupposedFileExtension() {
        return SupposedFileExtension;
    }

    String getSupposedExt(){
        String[] parts = FilePath.split("\\.");
        String SupposedFileExtension = parts[parts.length - 1];
        return SupposedFileExtension;
    }

    byte[] getFileContent() throws IOException {
        File file = new File(FilePath);
        byte[] FileContent = Files.readAllBytes(file.toPath());
        return FileContent;
    }

}
