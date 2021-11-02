package com.example.bonairecontrol.ModelosApi;

public class UsuarioMacModel {

    private String Mac;
    private String Email;

    public UsuarioMacModel() {
    }

    public UsuarioMacModel(String mac, String email) {
        Mac = mac;
        Email = email;
    }

    public String getMac() {
        return Mac;
    }

    public void setMac(String mac) {
        Mac = mac;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
