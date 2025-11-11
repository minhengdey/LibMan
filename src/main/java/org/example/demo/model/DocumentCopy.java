package org.example.demo.model;

import java.io.Serializable;

public class DocumentCopy  implements Serializable {
    private String barcode;
    private String status;
    private Document document;

    public DocumentCopy() {
    }

    public DocumentCopy(String barcode, String status, Document document) {
        this.barcode = barcode;
        this.status = status;
        this.document = document;
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

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
