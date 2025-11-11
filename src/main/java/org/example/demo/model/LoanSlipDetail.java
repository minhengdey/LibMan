package org.example.demo.model;

import java.io.Serializable;

public class LoanSlipDetail implements Serializable {
    private Integer id;
    private LoanSlip loanSlip;
    private DocumentCopy documentCopy;

    public LoanSlipDetail() {}

    public LoanSlipDetail(Integer id, LoanSlip loanSlip, DocumentCopy documentCopy) {
        this.id = id;
        this.loanSlip = loanSlip;
        this.documentCopy = documentCopy;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LoanSlip getLoanSlip() {
        return loanSlip;
    }

    public void setLoanSlip(LoanSlip loanSlip) {
        this.loanSlip = loanSlip;
    }

    public DocumentCopy getDocumentCopy() {
        return documentCopy;
    }

    public void setDocumentCopy(DocumentCopy documentCopy) {
        this.documentCopy = documentCopy;
    }
}
