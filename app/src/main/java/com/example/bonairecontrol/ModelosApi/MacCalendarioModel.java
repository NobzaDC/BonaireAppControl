package com.example.bonairecontrol.ModelosApi;

public class MacCalendarioModel {

    private String Mac;
    private String Funcion;
    private String FechaInicio;
    private int HoraInicio;
    private int MinutoInicio;
    private int HorasDuracion;
    private int MinutosDuracion;
    private boolean RepetirSU;
    private boolean RepetirLV;
    private boolean RepetirTD;
    private String RepetirPD;
    private boolean Habilitado;
    private String Observaciones;

    public MacCalendarioModel() {
    }

    public MacCalendarioModel(String mac, String funcion, String fechaInicio, int horaInicio,
                              int minutoInicio, int horasDuracion, int minutosDuracion,
                              boolean repetirSU, boolean repetirLV, boolean repetirTD,
                              String repetirPD, boolean habilitado, String observaciones) {
        Mac = mac;
        Funcion = funcion;
        FechaInicio = fechaInicio;
        HoraInicio = horaInicio;
        MinutoInicio = minutoInicio;
        HorasDuracion = horasDuracion;
        MinutosDuracion = minutosDuracion;
        RepetirSU = repetirSU;
        RepetirLV = repetirLV;
        RepetirTD = repetirTD;
        RepetirPD = repetirPD;
        Habilitado = habilitado;
        Observaciones = observaciones;
    }

    public String getMac() {
        return Mac;
    }

    public void setMac(String mac) {
        Mac = mac;
    }

    public String getFuncion() {
        return Funcion;
    }

    public void setFuncion(String funcion) {
        Funcion = funcion;
    }

    public String getFechaInicio() {
        return FechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        FechaInicio = fechaInicio;
    }

    public int getHoraInicio() {
        return HoraInicio;
    }

    public void setHoraInicio(int horaInicio) {
        HoraInicio = horaInicio;
    }

    public int getMinutoInicio() {
        return MinutoInicio;
    }

    public void setMinutoInicio(int minutoInicio) {
        MinutoInicio = minutoInicio;
    }

    public int getHorasDuracion() {
        return HorasDuracion;
    }

    public void setHorasDuracion(int horasDuracion) {
        HorasDuracion = horasDuracion;
    }

    public int getMinutosDuracion() {
        return MinutosDuracion;
    }

    public void setMinutosDuracion(int minutosDuracion) {
        MinutosDuracion = minutosDuracion;
    }

    public boolean isRepetirSU() {
        return RepetirSU;
    }

    public void setRepetirSU(boolean repetirSU) {
        RepetirSU = repetirSU;
    }

    public boolean isRepetirLV() {
        return RepetirLV;
    }

    public void setRepetirLV(boolean repetirLV) {
        RepetirLV = repetirLV;
    }

    public boolean isRepetirTD() {
        return RepetirTD;
    }

    public void setRepetirTD(boolean repetirTD) {
        RepetirTD = repetirTD;
    }

    public String getRepetirPD() {
        return RepetirPD;
    }

    public void setRepetirPD(String repetirPD) {
        RepetirPD = repetirPD;
    }

    public boolean isHabilitado() {
        return Habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        Habilitado = habilitado;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String observaciones) {
        Observaciones = observaciones;
    }
}
