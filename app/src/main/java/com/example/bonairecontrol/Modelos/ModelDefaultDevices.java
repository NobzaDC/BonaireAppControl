package com.example.bonairecontrol.Modelos;

public class ModelDefaultDevices {

    private String mac;
    private String tipo;
    private String nombre;

    public ModelDefaultDevices() {
    }

    public ModelDefaultDevices(String mac, String tipo, String nombre) {
        this.mac = mac;
        this.tipo = tipo;
        this.nombre = nombre;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
