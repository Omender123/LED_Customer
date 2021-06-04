package com.lightning.master.ledbulb.Model;

public class ModelPriceList {

    private String TITLE;
    private float CHARGES;
    private String PIECES;
    private String PERWATT;
    private String WATT;

    public String getWATT() {
        return WATT;
    }

    public void setWATT(String WATT) {
        this.WATT = WATT;
    }


    // Getter Methods

    public String getTITLE() {
        return TITLE;
    }

    public float getCHARGES() {
        return CHARGES;
    }

    public String getPIECES() {
        return PIECES;
    }

    public String getPERWATT() {
        return PERWATT;
    }

    // Setter Methods

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public void setCHARGES(float CHARGES) {
        this.CHARGES = CHARGES;
    }

    public void setPIECES(String PIECES) {
        this.PIECES = PIECES;
    }

    public void setPERWATT(String PERWATT) {
        this.PERWATT = PERWATT;
    }
}

