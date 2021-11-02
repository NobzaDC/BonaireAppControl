package com.example.bonairecontrol.Modelos;

public class ModelTypeDevices {

    private int img;
    private String nameType;

    public ModelTypeDevices() {
    }

    public ModelTypeDevices(int img, String nameType) {
        this.img = img;
        this.nameType = nameType;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }
}
