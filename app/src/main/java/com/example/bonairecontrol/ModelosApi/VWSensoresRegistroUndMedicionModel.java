package com.example.bonairecontrol.ModelosApi;

public class VWSensoresRegistroUndMedicionModel {

    private int Id;
    private String Mac;
    private String IdParametroUi;
    private String FechaReporte;
    private String FechaRecibido;
    private String ValorReportado;
    private String Ip;
    private String MensajeMQTT;
    private String UndMedicion;
    private String DescripcionMedicion;
    private String Nombre;
    private Float Buena;
    private Float Regular;
    private Float Mala;
    private Float Alta;
    private Float Critica;

    public VWSensoresRegistroUndMedicionModel() {
    }

    public VWSensoresRegistroUndMedicionModel(int id, String mac, String idParametroUi, String fechaReporte, String fechaRecibido, String valorReportado, String ip, String mensajeMQTT, String undMedicion, String descripcionMedicion, String nombre, Float buena, Float regular, Float mala, Float alta, Float critica) {
        Id = id;
        Mac = mac;
        IdParametroUi = idParametroUi;
        FechaReporte = fechaReporte;
        FechaRecibido = fechaRecibido;
        ValorReportado = valorReportado;
        Ip = ip;
        MensajeMQTT = mensajeMQTT;
        UndMedicion = undMedicion;
        DescripcionMedicion = descripcionMedicion;
        Nombre = nombre;
        Buena = buena;
        Regular = regular;
        Mala = mala;
        Alta = alta;
        Critica = critica;
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

    public String getUndMedicion() {
        return UndMedicion;
    }

    public void setUndMedicion(String undMedicion) {
        UndMedicion = undMedicion;
    }

    public String getDescripcionMedicion() {
        return DescripcionMedicion;
    }

    public void setDescripcionMedicion(String descripcionMedicion) {
        DescripcionMedicion = descripcionMedicion;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public Float getBuena() {
        return Buena;
    }

    public void setBuena(Float buena) {
        Buena = buena;
    }

    public Float getRegular() {
        return Regular;
    }

    public void setRegular(Float regular) {
        Regular = regular;
    }

    public Float getMala() {
        return Mala;
    }

    public void setMala(Float mala) {
        Mala = mala;
    }

    public Float getAlta() {
        return Alta;
    }

    public void setAlta(Float alta) {
        Alta = alta;
    }

    public Float getCritica() {
        return Critica;
    }

    public void setCritica(Float critica) {
        Critica = critica;
    }
}
