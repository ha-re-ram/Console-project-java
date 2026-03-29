package utils;

import java.util.Scanner;

public class InputHelper {
    private static Scanner scanner = new Scanner(System.in);

    public static String getString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static int getInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            scanner.nextLine();
            System.out.print("Enter a valid number: ");
        }
        int val = scanner.nextInt();
        scanner.nextLine();
        return val;
    }

    public static double getDouble(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            scanner.nextLine();
            System.out.print("Enter a valid amount: ");
        }
        double val = scanner.nextDouble();
        scanner.nextLine();
        return val;
    }
}