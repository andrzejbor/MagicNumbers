package com.company;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Andrzej on 10.02.2019.
 */
public class FileExtensionHandler {

    String FilePath;

    byte[] FileContent;

    String SupposedFileExtension;

    Map<String, Vector<ExtensionRulesContainer>> mapOfExtRules;

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

    String getSupposedExt() {
        String[] parts = FilePath.split("\\.");
        String SupposedFileExtension = parts[parts.length - 1];
        return SupposedFileExtension;
    }

    byte[] getFileContent() throws IOException {
        File file = new File(FilePath);
        byte[] FileContent = Files.readAllBytes(file.toPath());
        return FileContent;
    }

    public void addNewRule(String extension, int byteIndexes[], int byteValues[]) {
        if (!mapOfExtRules.containsKey(extension)) {
            mapOfExtRules.put(extension, new Vector<ExtensionRulesContainer>());
        }
        try {
            ExtensionRulesContainer rulesContainer = new ExtensionRulesContainer(byteIndexes, byteValues);
            mapOfExtRules.get(extension).add(rulesContainer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setExtensionRulesForGifJpg() {
        addNewRule("GIF", new int[]{0, 1, 2, 3, 4, 5}, new int[]{0x47, 0x49, 0x46, 0x38, 0x39, 0x61});
        addNewRule("GIF", new int[]{0, 1, 2, 3, 4, 5}, new int[]{0x47, 0x49, 0x46, 0x38, 0x37, 0x61});
        addNewRule("JPG", new int[]{0, 1, -2, -1}, new int[]{0xFF, 0xD8, 0xFF, 0xD9});
        addNewRule("JPGE", new int[]{0, 1, -2, -1}, new int[]{0xFF, 0xD8, 0xFF, 0xD9});
    }
}
