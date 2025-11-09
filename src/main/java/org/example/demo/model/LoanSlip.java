package org.example.demo.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class LoanSlip implements Serializable {
    private Integer id;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private String status;
    private LocalDateTime returnDate;
    private Float fine;
    private String notes;
    private Reader reader;
    private Librarian librarian;

    public LoanSlip() {}

    public LoanSlip(Integer id, LocalDateTime borrowDate, LocalDateTime dueDate, String status, LocalDateTime returnDate, Float fine, String notes, Reader reader, Librarian librarian) {
        this.id = id;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = status;
        this.returnDate = returnDate;
        this.fine = fine;
        this.notes = notes;
        this.reader = reader;
        this.librarian = librarian;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public Float getFine() {
        return fine;
    }

    public void setFine(Float fine) {
        this.fine = fine;
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

    public Librarian getLibrarian() {
        return librarian;
    }

    public void setLibrarian(Librarian librarian) {
        this.librarian = librarian;
    }
}

