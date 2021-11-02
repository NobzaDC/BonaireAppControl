package com.example.bonairecontrol.Modelos;

public class ModelJsonBq600 {

    private String Fecha;
    private String Hora;
    private String T;
    private String H;
    private String O3;
    private String PM1;
    private String PM25;
    private String PM10;
    private String R_1;
    private String R_2;
    private String R_3;
    private String R_4;
    private String PWM;
    private String Servo;
    private String IP;
    private String MAC;

    public ModelJsonBq600() {
    }

    public ModelJsonBq600(String fecha, String hora, String t,
                          String h, String o3, String r_1, String r_2,
                          String r_3, String r_4, String PWM, String servo,
                          String IP, String MAC) {
        Fecha = fecha;
        Hora = hora;
        T = t;
        H = h;
        O3 = o3;
        R_1 = r_1;
        R_2 = r_2;
        R_3 = r_3;
        R_4 = r_4;
        this.PWM = PWM;
        Servo = servo;
        this.IP = IP;
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

    public String getR_1() {
        return R_1;
    }

    public void setR_1(String r_1) {
        R_1 = r_1;
    }

    public String getR_2() {
        return R_2;
    }

    public void setR_2(String r_2) {
        R_2 = r_2;
    }

    public String getR_3() {
        return R_3;
    }

    public void setR_3(String r_3) {
        R_3 = r_3;
    }

    public String getR_4() {
        return R_4;
    }

    public void setR_4(String r_4) {
        R_4 = r_4;
    }

    public String getPWM() {
        return PWM;
    }

    public void setPWM(String PWM) {
        this.PWM = PWM;
    }

    public String getServo() {
        return Servo;
    }

    public void setServo(String servo) {
        Servo = servo;
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
}
