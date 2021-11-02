package com.example.bonairecontrol.ModelosApi;

public class MacModel {

    private String Mac;
    private int IdEquipo;
    private boolean Estado;
    private int IdLugarInstalacion;
    private String PrefijoMQTT;

    public MacModel() {
    }

    public MacModel(String mac, int idEquipo, boolean estado, int idLugarInstalacion, String prefijoMQTT) {
        Mac = mac;
        IdEquipo = idEquipo;
        Estado = estado;
        IdLugarInstalacion = idLugarInstalacion;
        PrefijoMQTT = prefijoMQTT;
    }


    public String getMac() {
        return Mac;
    }

    public void setMac(String mac) {
        Mac = mac;
    }

    public int getIdEquipo() {
        return IdEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        IdEquipo = idEquipo;
    }

    public boolean isEstado() {
        return Estado;
    }

    public void setEstado(boolean estado) {
        Estado = estado;
    }

    public int getIdLugarInstalacion() {
        return IdLugarInstalacion;
    }

    public void setIdLugarInstalacion(int idLugarInstalacion) {
        IdLugarInstalacion = idLugarInstalacion;
    }

    public String getPrefijoMQTT() {
        return PrefijoMQTT;
    }

    public void setPrefijoMQTT(String prefijoMQTT) {
        PrefijoMQTT = prefijoMQTT;
    }
}
