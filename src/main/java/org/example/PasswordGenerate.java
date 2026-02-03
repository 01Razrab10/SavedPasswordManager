package org.example;


import java.util.*;
import java.security.SecureRandom;
import java.util.stream.Collectors;

public class PasswordGenerate {
    // Длина нашего пароля
    private final int length;

    // Множество всех символов используемых в пароле
    private final Set<Character>characterSet;

    public PasswordGenerate(int length, Set<Character>characterSet) {
        this.length = length;
        this.characterSet = characterSet;
    }

    // генерируем пароль использованием криптографического генератора случайных чисел

    public String generate() {
        /*
           Если наше множество пустое, то в таком случае мы выводим ошибку,
           иначе создаётся массив символов, затем генерируемые случайные элементы
           добавляются через определённый нами метод в данный массив, а после
           возвращаем наш массив преобразованный в строку с помощью new String();
        */
        if (characterSet.isEmpty()) {
            throw new IllegalArgumentException("Нет доступных символов");
        }
        // Используем список так как в него проще добавлять элементы и перемешивать
        List<Character> resultList = new ArrayList<>();
        Random random = new SecureRandom();

        // --- Шаг 1: ГАРАНТИЯ (умный подбор) ---

        // Вытаскиваем все цифры из общего сета, если они там есть
        List<Character> digits = characterSet.stream().filter(Character::isDigit).collect(Collectors.toList());

        if (!digits.isEmpty()) {
            resultList.add(digits.get(random.nextInt(digits.size())));
        }

        // Вытаскиваем заглавные буквы если они там есть
        List<Character> upperCase = characterSet.stream().filter(Character::isUpperCase).collect(Collectors.toList());

        if (!upperCase.isEmpty()) {
            resultList.add(upperCase.get(random.nextInt(upperCase.size())));
        }

        // --- Шаг 2: ЗАПОЛНЕНИЕ ---

        // Добираем случайные символы через наш метод пока не будет достигнута нужная длина
        while (resultList.size() < length) {
            resultList.add(selectRandomCharacter(random));
        }

        // --- Шаг 3: ПЕРЕМЕШИВАНИЕ ---

        // Чтобы цифра и заглавная не всегда стояла в самом начале пароля
        Collections.shuffle(resultList);

        // --- Шаг 4: СБОРКА ---

        // Превращает строку обратно в строку
        StringBuilder sb = new StringBuilder();

        for (char c: resultList) {
            sb.append(c);
        }
        return sb.toString();
    }

    // Данный метод достаёт элементы из множества
    private char selectRandomCharacter(Random random) {
        List<Character> characterList = new ArrayList<>(characterSet);

        return characterList.get(random.nextInt(characterList.size()));
    }

}
