package org.example.demo.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class LoanSlip implements Serializable {
    private Integer id;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private String notes;
    private Reader reader;

    public LoanSlip() {}

    public LoanSlip(Integer id, LocalDateTime borrowDate, LocalDateTime dueDate, String notes, Reader reader) {
        this.id = id;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.notes = notes;
        this.reader = reader;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }
}

