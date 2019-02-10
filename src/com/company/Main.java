package com.company;

public class Main {

    public static void main(String[] args) {

        String path = "C:\\Users\\Andrzej\\Desktop\\aaa\\pobrane.gif";

        FileExtensionHandler FEH = new FileExtensionHandler(path);
       FEH.recognizeExtension();

    }
}
