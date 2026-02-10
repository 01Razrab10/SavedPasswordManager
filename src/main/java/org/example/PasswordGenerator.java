package org.example;

import org.example.PasswordGenerate.*;
import org.example.PasswordConfigBuilder.*;
//import Generator_password.CliArgumentsParser.*;

import java.util.Scanner;

public class PasswordGenerator {
    public void start() {
        Scanner scanner = new Scanner(System.in);
        PasswordConfigBuilder configBuilder = new PasswordConfigBuilder();

        System.out.println("-> Введите длину пароля(минимальная длина 8): ");
        int length = scanner.nextInt();
        configBuilder.setLength(length);

        configBuilder.withUpperCase(askYesNo(scanner, "Добавлять заглавные буквы? (y/n): "));
        configBuilder.withLowerCase(askYesNo(scanner, "Добавлять строчные буквы? (y/n): "));
        configBuilder.withNumbers(askYesNo(scanner, "Добавлять цифры? (y/n): "));
        configBuilder.withSymbols(askYesNo(scanner, "Добавлять заглавные символы? (y/n): "));

        if (askYesNo(scanner, "Исключить похожие символы (|, i, l, 1, o, 0)? (y/n): ")) {
            configBuilder.withoutSimilarCharacters();
        }

        PasswordGenerate passwordGenerator = configBuilder.build();
        CopyToClipboard copyToClipboard = new CopyToClipboard();

        try {
            String generatePassword = passwordGenerator.generate();
            System.out.println("Сгенерированный пароль: " + generatePassword);
            System.out.println("---------------------------------");
            copyToClipboard.passwordIntoClipboard(generatePassword);
            System.out.println("\nНажмите Enter чтобы перейти в главное меню...");
            new java.util.Scanner(System.in).nextLine();
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    private static boolean askYesNo(Scanner scanner, String message) {
        System.out.println(message);
        String input = scanner.next().trim().toLowerCase();
        return input.equals("y") || input.equals("yes");
    }
}
