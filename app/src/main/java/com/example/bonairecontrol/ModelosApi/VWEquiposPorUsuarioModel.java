package com.example.bonairecontrol.ModelosApi;

public class VWEquiposPorUsuarioModel {

    private String NombreEquipo;
    private String Mac;
    private boolean Estado;
    private String Email;
    private String NombreUsuario;
    private String ApellidoUsuario;
    private int IdLugarInstalacion;
    private String NombreLugarInstalacion;
    private String DireccionInstalacion;
    private String FechaHoraUltimoReporte;
    private int IdTipoEquipo;

    public VWEquiposPorUsuarioModel(String nombreEquipo, String mac, boolean estado, String email, String nombreUsuario, String apellidoUsuario, int idLugarInstalacion, String nombreLugarInstalacion, String direccionInstalacion, String fechaHoraUltimoReporte, int idTipoEquipo) {
        NombreEquipo = nombreEquipo;
        Mac = mac;
        Estado = estado;
        Email = email;
        NombreUsuario = nombreUsuario;
        ApellidoUsuario = apellidoUsuario;
        IdLugarInstalacion = idLugarInstalacion;
        NombreLugarInstalacion = nombreLugarInstalacion;
        DireccionInstalacion = direccionInstalacion;
        FechaHoraUltimoReporte = fechaHoraUltimoReporte;
        IdTipoEquipo = idTipoEquipo;
    }

    public String getNombreEquipo() {
        return NombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        NombreEquipo = nombreEquipo;
    }

    public String getMac() {
        return Mac;
    }

    public void setMac(String mac) {
        Mac = mac;
    }

    public boolean isEstado() {
        return Estado;
    }

    public void setEstado(boolean estado) {
        Estado = estado;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getNombreUsuario() {
        return NombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        NombreUsuario = nombreUsuario;
    }

    public String getApellidoUsuario() {
        return ApellidoUsuario;
    }

    public void setApellidoUsuario(String apellidoUsuario) {
        ApellidoUsuario = apellidoUsuario;
    }

    public int getIdLugarInstalacion() {
        return IdLugarInstalacion;
    }

    public void setIdLugarInstalacion(int idLugarInstalacion) {
        IdLugarInstalacion = idLugarInstalacion;
    }

    public String getNombreLugarInstalacion() {
        return NombreLugarInstalacion;
    }

    public void setNombreLugarInstalacion(String nombreLugarInstalacion) {
        NombreLugarInstalacion = nombreLugarInstalacion;
    }

    public String getDireccionInstalacion() {
        return DireccionInstalacion;
    }

    public void setDireccionInstalacion(String direccionInstalacion) {
        DireccionInstalacion = direccionInstalacion;
    }

    public String getFechaHoraUltimoReporte() {
        return FechaHoraUltimoReporte;
    }

    public void setFechaHoraUltimoReporte(String fechaHoraUltimoReporte) {
        FechaHoraUltimoReporte = fechaHoraUltimoReporte;
    }

    public int getIdTipoEquipo() {
        return IdTipoEquipo;
    }

    public void setIdTipoEquipo(int idTipoEquipo) {
        IdTipoEquipo = idTipoEquipo;
    }
}
