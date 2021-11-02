package com.example.bonairecontrol.ModelosApi;

public class SensorModel {

    private int Id;
    private String Nombre;
    private String UndMedicion;
    private String DescripcionMedicion;
    private String Fabricante;
    private String ParametroMQTT;
    private String IdParametroUi;
    private Float TiempoReporte;
    private String Observaciones;

    public SensorModel() {
    }

    public SensorModel(int id, String nombre, String undMedicion, String descripcionMedicion,
                       String fabricante, String parametroMQTT, String idParametroUi,
                       Float tiempoReporte, String observaciones) {
        Id = id;
        Nombre = nombre;
        UndMedicion = undMedicion;
        DescripcionMedicion = descripcionMedicion;
        Fabricante = fabricante;
        ParametroMQTT = parametroMQTT;
        IdParametroUi = idParametroUi;
        TiempoReporte = tiempoReporte;
        Observaciones = observaciones;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
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

    public String getFabricante() {
        return Fabricante;
    }

    public void setFabricante(String fabricante) {
        Fabricante = fabricante;
    }

    public String getParametroMQTT() {
        return ParametroMQTT;
    }

    public void setParametroMQTT(String parametroMQTT) {
        ParametroMQTT = parametroMQTT;
    }

    public String getIdParametroUi() {
        return IdParametroUi;
    }

    public void setIdParametroUi(String idParametroUi) {
        IdParametroUi = idParametroUi;
    }

    public Float getTiempoReporte() {
        return TiempoReporte;
    }

    public void setTiempoReporte(Float tiempoReporte) {
        TiempoReporte = tiempoReporte;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String observaciones) {
        Observaciones = observaciones;
    }
}
