package org.example.demo.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Librarian extends User implements Serializable {
    private String username;
    private String password;

    public Librarian() {}

    public Librarian(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Librarian(Integer id, String fullName, String address, LocalDateTime dob, String email, String phoneNumber, String role, String notes, String username, String password) {
        super(id, fullName, address, dob, email, phoneNumber, role, notes);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

