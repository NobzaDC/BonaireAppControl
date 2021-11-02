package com.example.bonairecontrol.Modelos;

public class ModelJsonTechCE {

    private String T;
    private String H;
    private String O3;
    private String CO;
    private String CO2;
    private String VOC;
    private String PM1;
    private String PM25;
    private String PM10;
    private String IP;
    private String MAC;
    private String Fecha;
    private String Hora;

    public ModelJsonTechCE(){}

    public ModelJsonTechCE(String t, String h, String o3, String CO,
                           String CO2, String VOC, String PM1, String PM25,
                           String PM10, String IP, String MAC) {
        this.T = t;
        this.H = h;
        this.O3 = o3;
        this.CO = CO;
        this.CO2 = CO2;
        this.VOC = VOC;
        this.PM1 = PM1;
        this.PM25 = PM25;
        this.PM10 = PM10;
        this.IP = IP;
        this.MAC = MAC;
    }

    public ModelJsonTechCE(String t, String h, String o3, String CO,
                           String CO2, String VOC, String PM1, String PM25,
                           String PM10, String IP, String MAC, String fecha, String hora) {
        T = t;
        H = h;
        O3 = o3;
        this.CO = CO;
        this.CO2 = CO2;
        this.VOC = VOC;
        this.PM1 = PM1;
        this.PM25 = PM25;
        this.PM10 = PM10;
        this.IP = IP;
        this.MAC = MAC;
        Fecha = fecha;
        Hora = hora;
    }

    public String getT() {
        return T;
    }

    public void setT(String t) {
        T = t;
    }

    public String getH() {
        return H;
    }

    public void setH(String h) {
        H = h;
    }

    public String getO3() {
        return O3;
    }

    public void setO3(String o3) {
        O3 = o3;
    }

    public String getCO() {
        return CO;
    }

    public void setCO(String CO) {
        this.CO = CO;
    }

    public String getCO2() {
        return CO2;
    }

    public void setCO2(String CO2) {
        this.CO2 = CO2;
    }

    public String getVOC() {
        return VOC;
    }

    public void setVOC(String VOC) {
        this.VOC = VOC;
    }

    public String getPM1() {
        return PM1;
    }

    public void setPM1(String PM1) {
        this.PM1 = PM1;
    }

    public String getPM25() {
        return PM25;
    }

    public void setPM25(String PM25) {
        this.PM25 = PM25;
    }

    public String getPM10() {
        return PM10;
    }

    public void setPM10(String PM10) {
        this.PM10 = PM10;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }
}
