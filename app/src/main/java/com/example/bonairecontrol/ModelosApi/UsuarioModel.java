package com.example.bonairecontrol.ModelosApi;

public class UsuarioModel {

    private String IdUsuario;
    private String Email;
    private String Telefono;
    private String Password;
    private String Nombres;
    private String Apellidos;
    private int IdGrupo;
    private String Estado;
    private String CodigoPais;
    private String Token;

    public UsuarioModel(){}

    public UsuarioModel(String idUsuario, String email, String telefono, String password, String nombres, String apellidos, int idGrupo, String estado, String codigoPais, String token) {
        IdUsuario = idUsuario;
        Email = email;
        Telefono = telefono;
        Password = password;
        Nombres = nombres;
        Apellidos = apellidos;
        IdGrupo = idGrupo;
        Estado = estado;
        CodigoPais = codigoPais;
        Token = token;
    }

    public String getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String nombres) {
        Nombres = nombres;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }

    public int getIdGrupo() {
        return IdGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        IdGrupo = idGrupo;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getCodigoPais() {
        return CodigoPais;
    }

    public void setCodigoPais(String codigoPais) {
        CodigoPais = codigoPais;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
