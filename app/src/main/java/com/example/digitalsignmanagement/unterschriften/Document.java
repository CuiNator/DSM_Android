package com.example.digitalsignmanagement.unterschriften;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

}
