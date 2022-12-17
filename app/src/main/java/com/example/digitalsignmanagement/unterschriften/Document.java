package com.example.digitalsignmanagement.unterschriften;

public class Document {

    private long documentId;
    private Date creationDate;
    private String status;
    private String name;
    private String creatorName;

    public Document(){}

    public Document(long documentId, Date creationDate, String status, String documentName, String creator) {
        this.documentId = documentId;
        this.creationDate = creationDate;
        this.status = status;
        this.name = documentName;
        this.creatorName = creator;

    }

    public long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(long documentId) {
        this.documentId = documentId;
    }

    public Date getCreationDate() {return creationDate;}

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
