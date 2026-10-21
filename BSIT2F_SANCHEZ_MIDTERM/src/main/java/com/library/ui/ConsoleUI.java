package com.library.ui;

import com.library.dao.DaoException;
import com.library.dao.ScenarioDao;
import com.library.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** Console menu loop. Talks to the database only through ScenarioDao. */
public class ConsoleUI {
    private static final String LINE = "=".repeat(52);

    private final ScenarioDao dao;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleUI(ScenarioDao dao) {
        this.dao = dao;
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = readLine("Enter choice: ");
            switch (choice) {
                case "1" -> addBook();
                case "2" -> viewCatalog();
                case "3" -> {
                    System.out.println("\nExiting system... Thank you!");
                    running = false;
                }
                default -> System.out.println("\nERROR: Invalid choice. Please enter 1, 2, or 3.\n");
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println(LINE);
        System.out.println("              LIBRARY BOOK CATALOG");
        System.out.println(LINE);
        System.out.println("[1] Add New Book");
        System.out.println("[2] View Book Catalog");
        System.out.println("[3] Exit System");
        System.out.println(LINE);
    }

    private void addBook() {
        System.out.println("\n--- ADD NEW BOOK ---");
        String title = readRequired("Enter Title: ");
        String author = readRequired("Enter Author: ");
        String isbn = readIsbn("Enter ISBN: ");
        String status = readOption("Enter Status (AVAILABLE / BORROWED): ", "AVAILABLE", "BORROWED");

        try {
            dao.insert(new Book(title, author, isbn, status));
            System.out.println("\nSUCCESS: Book cataloged into database!\n");
        } catch (DaoException e) {
            System.out.println("\nERROR: " + e.getMessage() + "\n");
        }
    }

    private void viewCatalog() {
        List<Book> books;
        try {
            books = dao.findAll();
        } catch (DaoException e) {
            System.out.println("\nERROR: " + e.getMessage() + "\n");
            return;
        }

        if (books.isEmpty()) {
            System.out.println("\nNo records found.\n");
            return;
        }

        List<String[]> rows = new ArrayList<>();
        for (Book b : books) {
            rows.add(new String[] {String.valueOf(b.getId()), b.getTitle(), b.getAuthor(),
                    b.getIsbn(), b.getStatus()});
        }
        printTable("LIBRARY BOOK CATALOG",
                new String[] {"ID", "TITLE", "AUTHOR", "ISBN", "STATUS"},
                new int[] {2, 19, 18, 13, 14}, rows);
    }

    /** Prints an ASCII table whose columns grow to fit the data. */
    private void printTable(String title, String[] headers, int[] minWidths, List<String[]> rows) {
        int[] w = minWidths.clone();
        for (int i = 0; i < headers.length; i++) {
            w[i] = Math.max(w[i], headers[i].length());
            for (String[] r : rows) {
                w[i] = Math.max(w[i], r[i].length());
            }
        }

        StringBuilder border = new StringBuilder("+");
        StringBuilder format = new StringBuilder("|");
        for (int width : w) {
            border.append("-".repeat(width + 2)).append("+");
            format.append(" %-").append(width).append("s |");
        }
        format.append("%n");

        int total = border.length();
        String equals = "=".repeat(total);
        int pad = Math.max(0, (total - title.length()) / 2);

        System.out.println();
        System.out.println(equals);
        System.out.println(" ".repeat(pad) + title);
        System.out.println(equals);
        System.out.println(border);
        System.out.printf(format.toString(), (Object[]) headers);
        System.out.println(border);
        for (String[] r : rows) {
            System.out.printf(format.toString(), (Object[]) r);
        }
        System.out.println(border);
        System.out.println("Total Records Displayed: " + rows.size() + "\n");
    }

    /** Reads one full line (avoids the nextInt/nextLine newline problem). */
    private String readLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            return "3"; // input closed: exit cleanly
        }
        return scanner.nextLine().trim();
    }

    private String readRequired(String prompt) {
        while (true) {
            String value = readLine(prompt);
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("ERROR: This field cannot be empty.");
        }
    }

    private String readIsbn(String prompt) {
        while (true) {
            String value = readLine(prompt).replace("-", "");
            if (value.matches("\\d{13}")) {
                return value;
            }
            System.out.println("ERROR: ISBN must be exactly 13 digits.");
        }
    }

    private String readOption(String prompt, String first, String second) {
        while (true) {
            String value = readLine(prompt).toUpperCase();
            if (value.equals(first) || value.equals(second)) {
                return value;
            }
            System.out.println("ERROR: Enter " + first + " or " + second + ".");
        }
    }
}
