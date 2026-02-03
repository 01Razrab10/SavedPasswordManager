package org.example;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class CopyToClipboard {
    // Получаем доступ к системному буферу обмена
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    // Метод, который будет копировать наш пароль в буфер обмена в Main классе
    public void passwordIntoClipboard(String password) {
        // Создаём объект для копирования текста в буфер обмена
        StringSelection stringSelection = new StringSelection(password);

        // Помещаем наш объект непосредственно в буфер обмена
        clipboard.setContents(stringSelection, null);

        System.out.println("Пароль был автоматически сохранён в буфер обмена");
    }
}
