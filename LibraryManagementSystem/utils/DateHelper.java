package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class DateHelper {
    private static final DateTimeFormatter FORMAT =
        DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Parse a date string in DD/MM/YYYY format
    public static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, FORMAT);
        } catch (DateTimeParseException e) {
            return null; // invalid format
        }
    }

    // Get date input from user safely
    public static LocalDate getDateInput(String prompt) {
        while (true) {
            System.out.print(prompt + " (DD/MM/YYYY): ");
            String input = new java.util.Scanner(System.in).nextLine().trim();
            LocalDate date = parseDate(input);
            if (date != null) return date;
            System.out.println("Invalid date format. Please use DD/MM/YYYY.");
        }
    }

    // Calculate number of days between two dates
    public static long daysBetween(LocalDate from, LocalDate to) {
        return ChronoUnit.DAYS.between(from, to);
    }

    // Format a date back to DD/MM/YYYY string for display
    public static String format(LocalDate date) {
        if (date == null) return "N/A";
        return date.format(FORMAT);
    }
}