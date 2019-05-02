package com.example.inventario;

import java.io.Serializable;

public class Item implements Serializable {
    private String Nombre;
    private String Descripcion;
    private Integer cantidad;
    private String URL;

    public Item(String nombre, String desc, int cant, String url){
        this.Nombre = nombre;
        this.Descripcion = desc;
        this.cantidad = cant;
        this.URL = url;

    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
