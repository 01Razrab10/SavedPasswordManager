package org.example;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordGenerateTest {
    @Test
    void shouldGeneratePasswordWithCorrectLength() {
        PasswordGenerate passwordGenerate = new PasswordConfigBuilder()
                .setLength(12)
                .withNumbers(true)
                .build();
        String password = passwordGenerate.generate();
        assertEquals(12, password.length());
    }
    @RepeatedTest(10)
    void shouldInspectionAbsenceNumbersInPassword() {
        PasswordGenerate passwordGenerate = new PasswordConfigBuilder()
                .withNumbers(true)
                .setLength(12)
                .build();
        String password = passwordGenerate.generate();
        assertTrue(password.matches(".*\\d+.*"));
    }
    @Test
    void shouldFallbackToMinimumLengthWhenInputIsTooShort() {
        PasswordGenerate passwordGenerate = new PasswordConfigBuilder()
                .setLength(5)
                .build();
        String password = passwordGenerate.generate();

        assertEquals(8, password.length());
    }
    @Test
    void shouldInspectionAbsenceSimilarCharactersInPassword() {
        PasswordGenerate passwordGenerate = new PasswordConfigBuilder()
                .setLength(100)
                .withoutSimilarCharacters()
                .build();
        String password = passwordGenerate.generate();
        assertAll(() -> assertFalse(password.contains("l")),
                  () -> assertFalse(password.contains("1")),
                  () -> assertFalse(password.contains("0")),
                  () -> assertFalse(password.contains("o")),
                  () -> assertFalse(password.contains("i"))
        );
    }
}