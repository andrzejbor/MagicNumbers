package com.company;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrzej on 10.02.2019.
 */
public class FileExtensionHandler {

    String FilePath;

    byte[] FileContent;

    String SupposedFileExtension;

    Map<String, ArrayList<ExtensionRulesContainer>> mapOfExtRules = new HashMap<>();

    String correctExtension;

    public FileExtensionHandler(String filePath) {
        correctExtension = "Invalid";
        FilePath = filePath;
        storeSupposedExt();
        try {
            FileContent = getFileContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setExtensionRulesForGifJpg();
    }


    public String getSupposedFileExtension() {
        return SupposedFileExtension;
    }

    void storeSupposedExt() {
        String[] parts = FilePath.split("\\.");
        String fileExtension = parts[parts.length - 1];
        SupposedFileExtension = fileExtension.toUpperCase();
    }

    byte[] getFileContent() throws IOException {
        File file = new File(FilePath);
        byte[] FileContent = Files.readAllBytes(file.toPath());
        return FileContent;
    }

    public void addNewRule(String extension, int byteIndexes[], int byteValues[]) {
        ExtensionRulesContainer erc = null;
        try {
            erc = new ExtensionRulesContainer(byteIndexes, byteValues);

        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<ExtensionRulesContainer> arrayOfRules = mapOfExtRules.get(extension);
        if (arrayOfRules == null) {
            arrayOfRules = new ArrayList<ExtensionRulesContainer>();
            arrayOfRules.add(erc);
            mapOfExtRules.put(extension, arrayOfRules);
        } else {
            if (!arrayOfRules.contains(erc)) {
                arrayOfRules.add(erc);
            }
        }
    }

    void setExtensionRulesForGifJpg() {
        addNewRule("GIF", new int[]{0, 1, 2, 3, 4, 5}, new int[]{0x47, 0x49, 0x46, 0x38, 0x39, 0x61});
        addNewRule("GIF", new int[]{0, 1, 2, 3, 4, 5}, new int[]{0x47, 0x49, 0x46, 0x38, 0x37, 0x61});
        addNewRule("JPG", new int[]{0, 1, -2, -1}, new int[]{0xFF, 0xD8, 0xFF, 0xD9});
        addNewRule("JPEG", new int[]{0, 1, -2, -1}, new int[]{0xFF, 0xD8, 0xFF, 0xD9});
    }


    public void recognizeExtension() {
        if (mapOfExtRules.containsKey(SupposedFileExtension) && isItSupposedExtension(SupposedFileExtension)) {
            System.out.println("This is expected extension of file: " + SupposedFileExtension);
        }else if (mapOfExtRules.containsKey(SupposedFileExtension) && !isItSupposedExtension(SupposedFileExtension)) {
            findCorrectExtension();
            System.out.println("Extension is " + SupposedFileExtension + ", while actually it is a " + correctExtension);
        }else if (!mapOfExtRules.containsKey(SupposedFileExtension)) {
            System.out.println("This file extension is not supported in this application");
        }
    }

    boolean isItSupposedExtension(String supposedExtension) {
        boolean result = false;

        for (ExtensionRulesContainer rulesContainer : mapOfExtRules.get(supposedExtension)) {
            int index = 0;
            result = true;

            for (int byteIndex : rulesContainer.getRuleByteIndex()) {
                if (byteIndex >= 0 && (byte)rulesContainer.getRuleByteValue()[index] != FileContent[byteIndex]) {
                    result = false;
                    break;
                } else if (byteIndex < 0 && (byte)rulesContainer.getRuleByteValue()[index] != FileContent[FileContent.length + byteIndex]) {
                    result = false;
                    break;
                }
                index++;
            }

            if (result == true) {
                correctExtension = supposedExtension;
                break;
            }

        }
        return result;
    }
    void findCorrectExtension() {
        for (Map.Entry<String, ArrayList<ExtensionRulesContainer>> entry: mapOfExtRules.entrySet()) {
            if (entry.getKey() == SupposedFileExtension){
                continue;
            }
            String extension = entry.getKey();
            if (isItSupposedExtension(extension)) {
                correctExtension = extension;
                break;
            }
        }
    }
}
