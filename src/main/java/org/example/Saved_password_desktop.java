package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import javax.crypto.spec.*;
import java.util.*;

import javax.crypto.SecretKey;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Saved_password_desktop {
    private static final Scanner scanner = new Scanner(System.in);
    private final Map<Integer, Integer> displayedIdMap = new HashMap<>();
    private final Database database;
    private final Encryption encryption = new Encryption();

    public  Saved_password_desktop(Database database) {
        this.database = database;
    }
    public void start() throws Exception {
        System.out.println(" ________________________________");
        System.out.println("|           Хранилище            |");
        System.out.println("|--------------------------------|");
        System.out.println("|--------------------------------|");
        System.out.println("| 1: Добавить пароль             |");
        System.out.println("| 2: Удалить пароль              |");
        System.out.println("| 3: Вывести сохранённые пароли  |");
        System.out.println("| 4: Вернуться в главное меню    |");
        System.out.println("|________________________________|\n");
        System.out.print("-> Выберите номер операции:\n");
        String numberOperation = scanner.nextLine().trim();

        switch (numberOperation) {
            case "1":
                System.out.println("-> Введите пароль:");
                String password = scanner.nextLine();

                System.out.println("-> Для чего этот пароль:");
                String description = scanner.nextLine();
                addPassword(password, description);
                System.out.println("\nНажмите Enter чтобы перейти в главное меню...");
                new java.util.Scanner(System.in).nextLine();
                return;

            case "2" :
                getAllPasswords();

                System.out.println("-> Введите id пароля, который нужно удалить: ");
                String idString = scanner.nextLine().trim();
                int idPassword = Integer.parseInt(idString);

                deletePassword(idPassword);
                System.out.println("\nНажмите Enter чтобы перейти в главное меню...");
                new java.util.Scanner(System.in).nextLine();
                return;

            case "3":
                getAllPasswords();
                System.out.println("\nНажмите Enter чтобы перейти в главное меню...");
                new java.util.Scanner(System.in).nextLine();
                return;

            case "4":
                return;

            default:
                System.out.println("Неправильный номер операции");
                System.out.println("\nНажмите Enter чтобы перейти в главное меню...");
                new java.util.Scanner(System.in).nextLine();
        }
    }
    // Метод добавления пароля
    void addPassword(String password, String description) throws Exception {
        byte[] hashedPassword = encryption.encryptPassword(password, getOrCreateSecretKey());

        // Наша SQL команда
        String sql = "INSERT INTO saved_passwords (password, description) VALUES (?, ?)";

        // Автоматически закроет наши ресурсы(соединение, statement)
        try (Connection connection = database.connect()) {
            // Готовим нашу SQl команду
            PreparedStatement ps = connection.prepareStatement(sql);

            // Вставляем вместо вопросительного знака наш пароль
            ps.setString(1, Base64.getEncoder().encodeToString(hashedPassword));
            // Вставляем вместо вопросительного знака наше описание(для чего этот пароль нужен)
            ps.setString(2, description);
            // Выполняем вставку
            ps.executeUpdate();

            System.out.println("Пароль успешно добавлен!");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public List<PasswordEntry> getPasswordsList() {
        List<PasswordEntry> list = new ArrayList<>();
        String sql = "SELECT * FROM saved_passwords ORDER BY id";
        displayedIdMap.clear(); // Очищаем карту перед новым заполнением

        try (Connection connection = database.connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            int displayId = 1; // Фейковый ID

            while (rs.next()) {
                int realId = rs.getInt("id");
                String password = encryption.decryptPassword(rs.getString("password"), getOrCreateSecretKey());
                String description = rs.getString("description");

                // Сохраняем соответствие: фейковый ID -> реальный ID из БД
                displayedIdMap.put(displayId, realId);

                // Выводим фейковый ID | пароль | описание
                list.add(new PasswordEntry(description, displayId, password));

                displayId++;

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    void getAllPasswords() {
        List<PasswordEntry> entries = getPasswordsList();

        System.out.println(" __________________________");
        System.out.println("|  Формат хранения пароля  |");
        System.out.println("|--------------------------|");
        System.out.println("| ID | Пароль | Описание   |");
        System.out.println("|__________________________|");

        if (entries.isEmpty()) {
            System.out.println("\nВы не добавляли никаких паролей =(");
        }

        for (PasswordEntry entry: entries) {
            System.out.println(entry.getId() + "|" + entry.getPassword() + "|" + entry.getDescription());
        }
    }


    // Метод реализующий удаления наших паролей
    void deletePassword(int passwordId) {
        // Находим реальный ID по "фейковому" введённому пользователем
        Integer realDbId = displayedIdMap.get(passwordId);

        if (realDbId == null) {
            System.out.println("Ошибка: пароль с таким порядковым номером не существует");
            return;
        }

        // Используем реальный ID для выполнения SQL-запросов
        String sql = "DELETE FROM saved_passwords WHERE id = ?";


        try (Connection connection = database.connect()) {
            if (!connection.getAutoCommit()) {
                connection.setAutoCommit(true);
            }
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, realDbId);
           int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Пароль с ID: " + passwordId + " успешно удалён");
                getAllPasswords();
            }
            else {
                System.out.println("Пароль с ID: " + passwordId + " не найден, проверьте ID");
            }
        }
        catch (SQLException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
        }
    }
    // Данный метод определяет операционную систему конечного пользователя исходя из чего после
    // создаёт файл с паролями
    private Path getKeyFilePath() {
        String os = System.getProperty("os.name").toLowerCase();
        Path appData;

        if(os.contains("win")) {
            appData = Paths.get(System.getenv("APPDATA"), "SavedPasswordApp");
        }
        else if (os.contains("mac") || os.contains("linux")) {
            appData = Paths.get(System.getProperty("user.home"), ".SavedPasswordApp");
        }
        else {
            appData = Paths.get(System.getProperty("user.home"), ".SavedPasswordApp");
        }
        return appData.resolve("secret.key");
    }
    // Данный метод сохраняет пароль в файл
    private void saveKeyToFile(SecretKey key) throws IOException {
        // Получаем доступ к файлу с паролями
        Path keyPath = getKeyFilePath();
        Files.createDirectories(keyPath.getParent());
        // Записываем пароль в файл
        Files.write(keyPath, key.getEncoded());
    }
    // Этот метод загружает пароль из файла
    private SecretKey loadKeyFromFile() throws Exception {
        Path keyPath = getKeyFilePath();
        // Получаем доступ к нашему паролю
        byte[] keyBytes = Files.readAllBytes(keyPath);
        // Вернёт наш пароль
        return new SecretKeySpec(keyBytes, "AES");
    }
    // Этот метод генерирует ключ при первом запуске
    private SecretKey getOrCreateSecretKey() throws Exception {
        //
        Path keyPath = getKeyFilePath();
        if (Files.exists(keyPath)) {
            return loadKeyFromFile();
        }
        else {
            SecretKey newKey = encryption.setSecretKey();
            saveKeyToFile(newKey);
            return newKey;
        }
    }
}
