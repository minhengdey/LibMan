package org.example.demo.model;

import java.io.Serializable;

public class DocumentCopy extends Document implements Serializable {
    private String barcode;
    private String status;

    public DocumentCopy() {}

    public DocumentCopy(String barcode, String status) {
        this.barcode = barcode;
        this.status = status;
    }

    public DocumentCopy(Integer id, String ISBN, String name, String author, String publisher, Integer yearOfPublication, String genre, String description, String barcode, String status) {
        super(id, ISBN, name, author, publisher, yearOfPublication, genre, description);
        this.barcode = barcode;
        this.status = status;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
