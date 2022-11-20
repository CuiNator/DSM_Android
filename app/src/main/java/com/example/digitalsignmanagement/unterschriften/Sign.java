package com.example.digitalsignmanagement.unterschriften;

import java.util.ArrayList;

public class Sign {


    private String name;
    private String datum;
    private String ersteller;
    private boolean unterschrieben;

    public Sign(String name, String datum, String ersteller, boolean unterschrieben) {
        this.name = name;
        this.datum = datum;
        this.ersteller = ersteller;
        this.unterschrieben = unterschrieben;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getErsteller() {
        return ersteller;
    }

    public void setErsteller(String ersteller) {
        this.ersteller = ersteller;
    }

    public boolean isUnterschrieben() {
        return unterschrieben;
    }

    public void setUnterschrieben(boolean unterschrieben) {
        this.unterschrieben = unterschrieben;
    }


}
