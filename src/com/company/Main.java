package com.company;

public class Main {

    public static void main(String[] args) {

        String path = "C:\\Users\\Andrzej\\Desktop\\aaa\\jotpeg.jpg";

        FileExtensionHandler FEH = new FileExtensionHandler(path);

        System.out.println(FEH.getSupposedFileExtension());
    }
}
