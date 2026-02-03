package org.example;

public class PasswordEntry {
    private int id;
    private String password;
    private String description;

    public PasswordEntry(String description, int id, String password) {
        this.description = description;
        this.id = id;
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
