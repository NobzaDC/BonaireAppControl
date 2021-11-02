package com.example.bonairecontrol.ModelosApi;

public class SensorReporteModel {

    private String Mac;
    private String IdParametroUi;
    private String FechaReporte;
    private String ValorReportado;

    public SensorReporteModel(String mac, String idParametroUi, String fechaReporte, String valorReportado) {
        Mac = mac;
        IdParametroUi = idParametroUi;
        FechaReporte = fechaReporte;
        ValorReportado = valorReportado;
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
}
