package com.example.bonairecontrol.ModelosApi;

public class NivelesAmbientexLugarModel {

    private String IdParametroUi;
    private int IdLugarInstalacion;
    private Float Buena;
    private Float Regular;
    private Float Mala;
    private Float Alta;
    private Float Critica;

    public NivelesAmbientexLugarModel() {
    }

    public NivelesAmbientexLugarModel(String idParametroUi, int idLugarInstalacion, Float buena,
                                      Float regular, Float mala, Float alta, Float critica) {
        IdParametroUi = idParametroUi;
        IdLugarInstalacion = idLugarInstalacion;
        Buena = buena;
        Regular = regular;
        Mala = mala;
        Alta = alta;
        Critica = critica;
    }

    public String getIdParametroUi() {
        return IdParametroUi;
    }

    public void setIdParametroUi(String idParametroUi) {
        IdParametroUi = idParametroUi;
    }

    public int getIdLugarInstalacion() {
        return IdLugarInstalacion;
    }

    public void setIdLugarInstalacion(int idLugarInstalacion) {
        IdLugarInstalacion = idLugarInstalacion;
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
