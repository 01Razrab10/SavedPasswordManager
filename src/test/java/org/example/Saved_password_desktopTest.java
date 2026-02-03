package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Saved_password_desktopTest {
    @Test
    void shouldSavePasswordAndRetrieveItFromDatabase() throws Exception {
        Database db = new Database("jdbc:sqlite:tst.db");
        db.createTable();
        Saved_password_desktop desktop = new Saved_password_desktop(db);

        desktop.addPassword("1234567", "PornHub");
        List<PasswordEntry> list = desktop.getPasswordsList();
        assertEquals(1, list.size());
        PasswordEntry entry = list.get(0);
        assertEquals("1234567", entry.getPassword());
        assertEquals("PornHub", entry.getDescription());
    }
}