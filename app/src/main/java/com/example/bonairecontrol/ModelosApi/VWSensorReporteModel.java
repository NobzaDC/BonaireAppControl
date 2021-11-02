package com.example.bonairecontrol.ModelosApi;

public class VWSensorReporteModel {
    private String Mac;
    private String IdParametroUi;
    private String FechaReporte;
    private String ValorReportado;
    private String UndMedicion;
    private String Nombre;
    private int Ordenar;

    public VWSensorReporteModel() {
    }

    public VWSensorReporteModel(String mac, String idParametroUi, String fechaReporte, String valorReportado, String undMedicion, String nombre, int ordenar) {
        Mac = mac;
        IdParametroUi = idParametroUi;
        FechaReporte = fechaReporte;
        ValorReportado = valorReportado;
        UndMedicion = undMedicion;
        Nombre = nombre;
        Ordenar = ordenar;
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

    public String getValorReportado() {
        return ValorReportado;
    }

    public void setValorReportado(String valorReportado) {
        ValorReportado = valorReportado;
    }

    public String getUndMedicion() {
        return UndMedicion;
    }

    public void setUndMedicion(String undMedicion) {
        UndMedicion = undMedicion;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public int getOrdenar() {
        return Ordenar;
    }

    public void setOrdenar(int ordenar) {
        Ordenar = ordenar;
    }
}
