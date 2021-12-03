package com.univalle.javiermurguia.proyectoteleferico.Models;

import androidx.lifecycle.ViewModel;

import java.io.Serializable;

public class Marcador implements Serializable {
    String nombre;
    String descripcion;
    String urlImage;
    boolean dragable;
    double latitud;
    double longitud;

    public Marcador(String nombre, String descripcion, String urlImage, boolean dragable, double latitud, double longitud) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.urlImage = urlImage;
        this.dragable = dragable;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Marcador(String nombre, String descripcion, boolean dragable, double latitud, double longitud) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.urlImage = "";
        this.dragable = dragable;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Marcador() {
        this.nombre = "";
        this.descripcion = "";
        this.urlImage = "";
        this.dragable = false;
        this.latitud = 0;
        this.longitud = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public boolean isDragable() {
        return dragable;
    }

    public void setDragable(boolean dragable) {
        this.dragable = dragable;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
