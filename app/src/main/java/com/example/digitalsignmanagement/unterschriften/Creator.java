package com.example.digitalsignmanagement.unterschriften;

public class Creator {

    private long personId;
    private String name;
    private String email;
    private String status;
    private String lastSignature;

    public Creator(){}

    public Creator(long id, String name, String email, String status, String lastSignature) {
        this.personId = id;
        this.name = name;
        this.email = email;
        this.status = status;
        this.lastSignature = lastSignature;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastSignature() {
        return lastSignature;
    }

    public void setLastSignature(String lastSignature) {
        this.lastSignature = lastSignature;
    }
}
