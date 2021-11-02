package com.example.bonairecontrol.ModelosApi;

public class MensajesMqttModel {

    private int IdRegistro;
    private String Mensaje;
    private boolean Estado;
    private String FechaRecibido;
    private String Origen;
    private String Topico;
    private String IpComando;
    private String Dispositivo;

    public MensajesMqttModel(){}

    public MensajesMqttModel(int idRegistro, String mensaje, boolean estado, String fechaRecibido, String origen, String topico, String ipComando, String dispositivo) {
        IdRegistro = idRegistro;
        Mensaje = mensaje;
        Estado = estado;
        FechaRecibido = fechaRecibido;
        Origen = origen;
        Topico = topico;
        IpComando = ipComando;
        Dispositivo = dispositivo;
    }

    public int getIdRegistro() {
        return IdRegistro;
    }

    public void setIdRegistro(int idRegistro) {
        IdRegistro = idRegistro;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String mensaje) {
        Mensaje = mensaje;
    }

    public boolean isEstado() {
        return Estado;
    }

    public void setEstado(boolean estado) {
        Estado = estado;
    }

    public String getFechaRecibido() {
        return FechaRecibido;
    }

    public void setFechaRecibido(String fechaRecibido) {
        FechaRecibido = fechaRecibido;
    }

    public String getOrigen() {
        return Origen;
    }

    public void setOrigen(String origen) {
        Origen = origen;
    }

    public String getTopico() {
        return Topico;
    }

    public void setTopico(String topico) {
        Topico = topico;
    }

    public String getIpComando() {
        return IpComando;
    }

    public void setIpComando(String ipComando) {
        IpComando = ipComando;
    }

    public String getDispositivo() {
        return Dispositivo;
    }

    public void setDispositivo(String dispositivo) {
        Dispositivo = dispositivo;
    }
}
