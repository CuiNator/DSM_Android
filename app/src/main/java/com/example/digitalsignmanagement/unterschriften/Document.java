package com.example.digitalsignmanagement.unterschriften;

public class Document {

    private long documentId;
    private String uploadDate;
    private String completionDate;
    private String status;
    private String pdf;
    private String documentName;
    private Creator creator;
    private boolean active;

    public String[] getSignatures() {
        return signatures;
    }

    public void setSignatures(String[] signatures) {
        this.signatures = signatures;
    }

    private String[] signatures;




    public Document(){}

    public Document(long documentId, String uploadDate, String completionDate, String status, String pdf, String documentName, Creator creator, boolean active, String[] signatures) {
        this.documentId = documentId;
        this.uploadDate = uploadDate;
        this.completionDate = completionDate;
        this.status = status;
        this.pdf = pdf;
        this.documentName = documentName;
        this.creator = creator;
        this.active = active;
        this.signatures = signatures;
    }

    public long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(long documentId) {
        this.documentId = documentId;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
