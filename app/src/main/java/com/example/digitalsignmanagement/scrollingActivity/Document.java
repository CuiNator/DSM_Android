package com.example.digitalsignmanagement.scrollingActivity;

import java.util.Date;

public class Document {

    private long documentId;
    private Date creationDate;
    private int receivedSignatures;
    private int maxSigns;
    private String status;
    private String name;
    private String creatorName;
    private ExternalSigners externalSigners[];

    public Document(){}

    public Document(long documentId, Date creationDate, String status, String documentName, String creator, ExternalSigners externalSigners[],int receivedSignatures, int maxSigns) {
        this.documentId = documentId;
        this.creationDate = creationDate;
        this.status = status;
        this.name = documentName;
        this.creatorName = creator;
        this.externalSigners= externalSigners;
        this.receivedSignatures = receivedSignatures;
        this.maxSigns= maxSigns;


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

    public int getReceivedSignatures() {
        return receivedSignatures;
    }

    public void setReceivedSignatures(int receivedSignatures) {
        this.receivedSignatures = receivedSignatures;
    }

    public int getMaxSigns() {
        return maxSigns;
    }

    public void setMaxSigns(int maxSigns) {
        this.maxSigns = maxSigns;
    }

    public ExternalSigners[] getExternalSigners() {
        return externalSigners;
    }

    public void setExternalSigners(ExternalSigners[] externalSigners) {
        this.externalSigners = externalSigners;
    }
}