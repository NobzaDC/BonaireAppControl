package com.example.bonairecontrol.ModelosApi;

public class BitacoraWebModel {

    private int Id;
    private String FechaHora;
    private String IdUsuario;
    private String Password;
    private String Ip;
    private String Evento;

    public BitacoraWebModel(){}

    public BitacoraWebModel(int id, String fechaHora, String idUsuario, String password, String ip, String evento) {
        Id = id;
        FechaHora = fechaHora;
        IdUsuario = idUsuario;
        Password = password;
        Ip = ip;
        Evento = evento;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getFechaHora() {
        return FechaHora;
    }

    public void setFechaHora(String fechaHora) {
        FechaHora = fechaHora;
    }

    public String getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getIp() {
        return Ip;
    }

    public void setIp(String ip) {
        Ip = ip;
    }

    public String getEvento() {
        return Evento;
    }

    public void setEvento(String evento) {
        Evento = evento;
    }
}
