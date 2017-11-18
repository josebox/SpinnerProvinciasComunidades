package com.example.jose.tiempos;

/**
 * Created by jose on 06/11/2017.
 */

public class Prediccion {
    private String tMaxima;
    private String tMinima;
    private String fecha;

    public Prediccion(String tMaxima, String tMinima, String fecha) {
        this.tMaxima = tMaxima;
        this.tMinima = tMinima;
        this.fecha = fecha;
    }

    public String gettMaxima() {
        return tMaxima;
    }

    public String gettMinima() {
        return tMinima;
    }

    public String getFecha() {
        return fecha;
    }
}
