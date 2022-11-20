package com.example.digitalsignmanagement.unterschriften;

import java.util.ArrayList;

public class Unterschriften {


    private String name;
    private String datum;
    private String ersteller;
    private boolean unterschrieben;

    public Unterschriften(String name, String datum, String ersteller, boolean unterschrieben) {
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

    private ArrayList<Unterschriften> initUnterschriften(){
        ArrayList<Unterschriften> list = new ArrayList<>();
        list.add(new Unterschriften("Urlaubsantrag","20.11.22","Yanik",false));
        list.add(new Unterschriften("Urlaubsantrag","20.11.22","Yanik",true));
        list.add(new Unterschriften("Urlaubsantrag","20.11.22","Yanik",false));
        list.add(new Unterschriften("Urlaubsantrag","20.11.22","Yanik",true));
        return list;
    }
}
