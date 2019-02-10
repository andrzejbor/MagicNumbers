package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("Insert path to file: ");
        Scanner scanner = new Scanner(System.in);

        String path = scanner.nextLine();

        FileExtensionHandler FEH = new FileExtensionHandler(path);
       FEH.recognizeExtension();

    }
}
