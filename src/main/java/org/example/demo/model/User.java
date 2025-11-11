package org.example.demo.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class User implements Serializable {
    private Integer id;
    private String fullName;
    private String address;
    private LocalDateTime dob;
    private String email;
    private String phoneNumber;
    private String role;
    private String notes;

    public User() {
    }

    public User(Integer id, String fullName, String address, LocalDateTime dob, String email, String phoneNumber,
            String role, String notes) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.dob = dob;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.notes = notes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getDob() {
        return dob;
    }

    public void setDob(LocalDateTime dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
