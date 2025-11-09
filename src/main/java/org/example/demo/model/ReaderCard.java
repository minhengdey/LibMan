package org.example.demo.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ReaderCard implements Serializable {
    private String cardId;
    private LocalDateTime issueDate;
    private LocalDateTime expirationDate;
    private String status;
    private Reader reader;

    public ReaderCard() {}

    public ReaderCard(String cardId, LocalDateTime issueDate, LocalDateTime expirationDate, String status, Reader reader) {
        this.cardId = cardId;
        this.issueDate = issueDate;
        this.expirationDate = expirationDate;
        this.status = status;
        this.reader = reader;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }
}
