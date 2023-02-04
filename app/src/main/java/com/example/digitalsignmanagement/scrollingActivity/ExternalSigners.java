package com.example.digitalsignmanagement.scrollingActivity;

//POJO-class to safe the external signers retried from the backend and to be displayed in the scrolling view
public class ExternalSigners {
    private long personId;
    private String name;
    private boolean signed;

    public ExternalSigners(){}

    public ExternalSigners(long personId, String name, boolean signed){
        this.personId=personId;
        this.name = name;
        this.signed = signed;
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

    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }
}
