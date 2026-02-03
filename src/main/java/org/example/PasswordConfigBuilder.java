package org.example;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/*
    Данный класс строит конфигурацию пароля, используя принцип конструктора объектов
*/

public class PasswordConfigBuilder {

    private static final Set<Character> DEFAULT_CHARS = createDefaultCharacters();
    protected boolean includeUpperCase = true;
    protected boolean includeLowerCase = true;
    protected boolean includeNumbers = true;
    protected boolean includeSymbols = false;
    protected boolean excludeSimilar = false;
    protected int length = 12;

    public PasswordConfigBuilder setLength(int length) {
        this.length = Math.max(length, 8); // Минимум 8 символов

        return this;
    }

    public PasswordConfigBuilder withUpperCase(boolean enabled) {
        this.includeUpperCase = enabled;

        return this;
    }

    public PasswordConfigBuilder withLowerCase(boolean enabled) {
        this.includeLowerCase = enabled;

        return this;
    }

    public PasswordConfigBuilder withNumbers(boolean enabled) {
        this.includeNumbers = enabled;

        return this;
    }

    public PasswordConfigBuilder withSymbols(boolean enabled) {
        this.includeSymbols = enabled;

        return this;
    }

    public PasswordConfigBuilder withoutSimilarCharacters() {
        this.excludeSimilar = true;

        return this;
    }

    public PasswordGenerate build() {
        Set<Character> availableChars = collectAvailableCharacters();

        if (this.length < 8) {
            throw new IllegalArgumentException("Пароль слишком короткий");
        }
        return new PasswordGenerate(this.length, availableChars);
    }

    private Set<Character> collectAvailableCharacters() {
        Set<Character> characters = new HashSet<>();
        if (includeUpperCase) {
            characters.addAll(getUpperCaseLetters());
        }
        if (includeLowerCase) {
            characters.addAll(getLowerCaseLetters());
        }
        if (includeNumbers) {
            characters.addAll(getDigits());
        }
        if (includeSymbols) {
            characters.addAll(getSpecialCharacters());
        }
        if (this.excludeSimilar) {
            removeSimilarCharacters(characters);
        }

        return characters;
    }

    private void removeSimilarCharacters(Set<Character> chars) {
        Set<Character> similarChars = new HashSet<>(Arrays.asList('|', 'L', 'l', '1', 'o', 'O', '0', 'i', 'I'));
        chars.removeAll(similarChars);
    }

    private static Set<Character> createDefaultCharacters() {
        Set<Character> defaultChars = new HashSet<>();
        defaultChars.addAll(getUpperCaseLetters());
        defaultChars.addAll(getLowerCaseLetters());
        defaultChars.addAll(getDigits());

        return defaultChars;
    }

    protected static Set<Character> getUpperCaseLetters() {
        return toCharRange('A', 'Z');
    }

    protected static Set<Character> getLowerCaseLetters() {
        return toCharRange('a', 'z');
    }

    protected static Set<Character> getDigits() {
        return toCharRange('0', '9');
    }

    protected static Set<Character> getSpecialCharacters() {
        return new HashSet<>(Arrays.asList('!', '@', '#', '$', '%', '^', '&', '*'));
    }

    private static Set<Character> toCharRange(char start, char end) {
        Set<Character> range = new HashSet<>();

        for(char ch = start; ch <= end; ++ch) {
            range.add(ch);
        }
        return range;
    }
}

