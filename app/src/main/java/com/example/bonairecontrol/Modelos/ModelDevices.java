package com.example.bonairecontrol.Modelos;

public class ModelDevices {

    private int IdTipoEquipo;
    private String NombreEquipo;
    private String Estado;
    private String UltimoReporte;
    private String TipoConexion;
    private String Ubicacion;
    private String TipoEquipo;
    private int Imagen;
    private String mac;


    public ModelDevices() {

    }

    public ModelDevices(int idTipoEquipo, String nombreEquipo, String estado, String ultimoReporte, String tipoConexion, String ubicacion, String tipoEquipo, int imagen, String mac) {
        IdTipoEquipo = idTipoEquipo;
        NombreEquipo = nombreEquipo;
        Estado = estado;
        UltimoReporte = ultimoReporte;
        TipoConexion = tipoConexion;
        Ubicacion = ubicacion;
        TipoEquipo = tipoEquipo;
        Imagen = imagen;
        this.mac = mac;
    }

    public ModelDevices(String nombreEquipo, String estado, String ultimoReporte, String tipoConexion, String ubicacion, String tipoEquipo, int imagen, String mac) {
        NombreEquipo = nombreEquipo;
        Estado = estado;
        UltimoReporte = ultimoReporte;
        TipoConexion = tipoConexion;
        Ubicacion = ubicacion;
        TipoEquipo = tipoEquipo;
        Imagen = imagen;
        this.mac = mac;
    }

    public String getNombreEquipo() {
        return NombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        NombreEquipo = nombreEquipo;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getUltimoReporte() {
        return UltimoReporte;
    }

    public void setUltimoReporte(String ultimoReporte) {
        UltimoReporte = ultimoReporte;
    }

    public String getTipoConexion() {
        return TipoConexion;
    }

    public void setTipoConexion(String tipoConexion) {
        TipoConexion = tipoConexion;
    }

    public String getUbicacion() {
        return Ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        Ubicacion = ubicacion;
    }

    public String getTipoEquipo() {
        return TipoEquipo;
    }

    public void setTipoEquipo(String tipoEquipo) {
        TipoEquipo = tipoEquipo;
    }

    public int getImagen() {
        return Imagen;
    }

    public void setImagen(int imagen) {
        Imagen = imagen;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getIdTipoEquipo() {
        return IdTipoEquipo;
    }

    public void setIdTipoEquipo(int idTipoEquipo) {
        IdTipoEquipo = idTipoEquipo;
    }
}
