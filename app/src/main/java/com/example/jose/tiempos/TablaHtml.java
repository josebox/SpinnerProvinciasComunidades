package com.example.jose.tiempos;

import java.util.ArrayList;

/**
 * Created by jose on 06/11/2017.
 */

public class TablaHtml {
    public static String generarHtml(String titulo, String[] encabezado, ArrayList<String[]> filas) {
        String codigoHtml = "<html><head>\n<style type=\"text/css\">\r\n" +
                "		body{\r\n" +
                "		    background: rgba(10,10,10,1);\n" +
                "           background: -webkit-linear-gradient(top, rgba(10,10,10,1) 0%," +
                "               rgba(107,81,78,1) 37%," +
                "               rgba(168,74,62,1) 67%, rgba(250,30,10,1) 100%);\n" +
                "			color: white;\r\n" +
                "		}\r\n" +
                "		table{\r\n" +
                "			margin:auto;\r\n" +
                "			text-align: center;\r\n" +
                "			background-color:;\r\n" +
                "			border-collapse: collapse;\r\n" +
                "			border: red 5px solid;\r\n" +
                "		}\r\n" +
                "		th{\r\n" +
                "			font-size:20px;\r\n" +
                "			background-color:grey;\r\n" +
                "			border: red 5px solid;\r\n" +
                "			margin: 15px;\r\n" +
                "			padding: 15px;\r\n" +
                "			color: white;\r\n" +
                "		}\r\n" +
                "		tr{\r\n" +
                "			background-color:#F3EBF7;\r\n" +
                "			font-size:16px;\r\n" +
                "			border: red 5px solid;\r\n" +

                "		}\r\n" +
                "		td{\r\n" +
                "			background-image: url();\r\n" +
                "			font-size:16px;\r\n" +
                "			border: red 5px solid;\r\n" +
                "			margin: 15px;\r\n" +
                "			padding: 15px;\r\n" +
                "			color: black;\r\n" +
                "		}\r\n" +
                "		div{\r\n" +
                "			margin-top: 10px;\r\n" +
                "			height: 100px;\r\n" +
                "			width: auto;\r\n" +
                "			background-image: url(https://3.bp.blogspot.com/-5t8WDIBvaM8/WQiKfMo2JMI/AAAAAAAASKU/eg3RL2KoOfkrSK8LgnAnmV9660H_EdrxwCLcB/s1600/sol-con-nubes-420x236.gif);\r\n" +
                "	</style>";
        codigoHtml += generarTabla(encabezado, filas);
        codigoHtml += "<div></div></body></html>";
        return codigoHtml;
    }

    public static String generarTabla(String[] encabezado, ArrayList<String[]> filas) {
        String tablaHtml = "<table>";
        tablaHtml += generarEncabezado(encabezado);
        for (String[] fila : filas) {
            tablaHtml += generarFila(fila);
        }
        tablaHtml += "</table>";
        return tablaHtml;
    }

    public static String generarEncabezado(String[] encabezado) {
        String encabezadoHtml = "";
        for (String columna : encabezado) {
            encabezadoHtml += "<th>" + columna + "</th>";
        }
        return encabezadoHtml;
    }

    public static String generarFila(String[] fila) {
        String filasHtml = "<tr>";
        for (String columna : fila) {
            filasHtml += "<td>" + columna + "</td>";
        }
        filasHtml += "</tr>";
        return filasHtml;
    }

}
