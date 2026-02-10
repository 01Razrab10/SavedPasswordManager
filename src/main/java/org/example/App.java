package org.example;

import java.util.Scanner;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Database database = new Database("jdbc:sqlite:passwords.db");
    private static final Saved_password_desktop desktop_part1 = new Saved_password_desktop(database);
    private static final PasswordGenerator desktop_part2 = new PasswordGenerator();

    public static void main( String[] args ) throws Exception {
        mainMenu();
    }
    public static void mainMenu() throws Exception {
        while (true) {
            database.createTable();

            System.out.println(" ________________________________ ");
            System.out.println("|  [KEY] Менеджер паролей [KEY]  |");
            System.out.println("|--------------------------------|");
            System.out.println("|--------------------------------|");
            System.out.println("| 1: Перейти в хранилище         |");
            System.out.println("| 2: Создать новый пароль        |");
            System.out.println("| 3: Выйти из приложения         |");
            System.out.println("|________________________________|");
            System.out.print("-> Выберите номер операции: ");

            String numberOperation = scanner.nextLine().trim();

            switch (numberOperation) {
                case "1":
                    desktop_part1.start();
                    break;

                case "2":
                    desktop_part2.start();
                    break;

                case "3":
                    System.exit(0);

                default:
                    System.out.println("Неправильный номер операции");
                    System.out.println("\nНажмите Enter чтобы закрыть...");
                    new Scanner(System.in).nextLine();
            }
        }
    }
}

