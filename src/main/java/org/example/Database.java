package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private final String connectionUrl;

    public Database(String connectionUrl) {
        // URL подключения к локальному файлу SQLite
        // Файл passwords.db будет создан автоматически в директории запуска
        this.connectionUrl = connectionUrl;
    }
    // private static final String connectionUrl = "jdbc:sqlite:passwords.db";

    // Метод, через который осуществляется соединение с нашей БД
    public Connection connect() {
        // В данной переменной мы будем хранить наше соединение
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(connectionUrl);

        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
        return connection;
    }

    public void createTable() {
        // Скрипт для создания нашей таблицы
        String sql = """ 
            CREATE TABLE IF NOT EXISTS saved_passwords (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                password text NOT NULL,
                description text NOT NULL
            ); 
        """;

        // connect() теперь подключается к SQLite и создаёт файл .db, если его нет
       // Database database = new Database("jdbc:sqlite:passwords.db");
        try (Connection connection = this.connect();
             java.sql.Statement stmt = connection.createStatement()) {

            // Эта строка выполняет скрипт и создаёт нашу таблицу внутри файла
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
