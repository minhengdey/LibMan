package org.example.demo.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Reader extends User implements Serializable {

    public Reader() {}

    public Reader(Integer id, String fullName, String address, LocalDateTime dob, String email, String phoneNumber, String role, String notes) {
        super(id, fullName, address, dob, email, phoneNumber, role, notes);
    }
}


