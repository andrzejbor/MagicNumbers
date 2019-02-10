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

    String filePath;

    byte[] fileContent;

    String supposedFileExtension;

    Map<String, ArrayList<ExtensionRulesContainer>> mapOfExtRules = new HashMap<>();

    String correctExtension;

    public FileExtensionHandler(String filePath) {
        correctExtension = "Invalid";
        this.filePath = filePath;
        storeSupposedExt();
        try {
            fileContent = getFileContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setExtensionRulesForGifJpgDoc();
        setExtensionRulesForOtherFiles();
    }


    public String getSupposedFileExtension() {
        return supposedFileExtension;
    }

    void storeSupposedExt() {
        String[] parts = filePath.split("\\.");
        String fileExtension = parts[parts.length - 1];
        supposedFileExtension = fileExtension.toUpperCase();
    }

    byte[] getFileContent() throws IOException {
        File file = new File(filePath);
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

    void setExtensionRulesForGifJpgDoc() {
        addNewRule("GIF", new int[]{0, 1, 2, 3, 4, 5}, new int[]{0x47, 0x49, 0x46, 0x38, 0x39, 0x61});
        addNewRule("GIF", new int[]{0, 1, 2, 3, 4, 5}, new int[]{0x47, 0x49, 0x46, 0x38, 0x37, 0x61});
        addNewRule("JPG", new int[]{0, 1, -2, -1}, new int[]{0xFF, 0xD8, 0xFF, 0xD9});
        addNewRule("JPEG", new int[]{0, 1, -2, -1}, new int[]{0xFF, 0xD8, 0xFF, 0xD9});
        addNewRule("DOC", new int[]{0, 1, 2, 3, 4, 5, 6, 7}, new int[]{0xD0, 0xCF, 0x11, 0xE0, 0xA1, 0xB1, 0x1A, 0xE1});
    }

    void setExtensionRulesForOtherFiles() {
        addNewRule("PDF", new int[]{0, 1, 2, 3}, new int[]{0x25, 0x50, 0x44, 0x46});
        addNewRule("EXE", new int[]{0, 1}, new int[]{0x4D, 0x5A});
        addNewRule("BIN", new int[]{0, 1, 2, 3}, new int[]{0x53, 0x50, 0x30, 0x31});
    }


    public void recognizeExtension() {
        if (mapOfExtRules.containsKey(supposedFileExtension) && isItSupposedExtension(supposedFileExtension)) {
            System.out.println("This is expected extension of file: " + supposedFileExtension);
        }else if (mapOfExtRules.containsKey(supposedFileExtension) && !isItSupposedExtension(supposedFileExtension)) {
            findCorrectExtension();
            System.out.println("Extension is " + supposedFileExtension + ", while actually it is a " + correctExtension);
        }else if (!mapOfExtRules.containsKey(supposedFileExtension)) {
            System.out.println("This file extension is not supported in this application");
        }
    }

    boolean isItSupposedExtension(String supposedExtension) {
        boolean result = false;

        for (ExtensionRulesContainer rulesContainer : mapOfExtRules.get(supposedExtension)) {
            int index = 0;
            result = true;

            for (int byteIndex : rulesContainer.getRuleByteIndex()) {
                if (byteIndex >= 0 && (byte)rulesContainer.getRuleByteValue()[index] != fileContent[byteIndex]) {
                    result = false;
                    break;
                } else if (byteIndex < 0 && (byte)rulesContainer.getRuleByteValue()[index] != fileContent[fileContent.length + byteIndex]) {
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
            if (entry.getKey() == supposedFileExtension){
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
