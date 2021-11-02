package com.example.bonairecontrol.ModelosApi;

public class SensorReporteDetModel {

    private int Id;
    private String Mac;
    private String IdParametroUi;
    private String FechaReporte;
    private String FechaRecibido;
    private String ValorReportado;
    private String Ip;
    private String MensajeMQTT;

    public SensorReporteDetModel() {
    }

    public SensorReporteDetModel(int id, String mac, String idParametroUi, String fechaReporte, String fechaRecibido, String valorReportado, String ip, String mensajeMQTT) {
        Id = id;
        Mac = mac;
        IdParametroUi = idParametroUi;
        FechaReporte = fechaReporte;
        FechaRecibido = fechaRecibido;
        ValorReportado = valorReportado;
        Ip = ip;
        MensajeMQTT = mensajeMQTT;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getMac() {
        return Mac;
    }

    public void setMac(String mac) {
        Mac = mac;
    }

    public String getIdParametroUi() {
        return IdParametroUi;
    }

    public void setIdParametroUi(String idParametroUi) {
        IdParametroUi = idParametroUi;
    }

    public String getFechaReporte() {
        return FechaReporte;
    }

    public void setFechaReporte(String fechaReporte) {
        FechaReporte = fechaReporte;
    }

    public String getFechaRecibido() {
        return FechaRecibido;
    }

    public void setFechaRecibido(String fechaRecibido) {
        FechaRecibido = fechaRecibido;
    }

    public String getValorReportado() {
        return ValorReportado;
    }

    public void setValorReportado(String valorReportado) {
        ValorReportado = valorReportado;
    }

    public String getIp() {
        return Ip;
    }

    public void setIp(String ip) {
        Ip = ip;
    }

    public String getMensajeMQTT() {
        return MensajeMQTT;
    }

    public void setMensajeMQTT(String mensajeMQTT) {
        MensajeMQTT = mensajeMQTT;
    }
}
