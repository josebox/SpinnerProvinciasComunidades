package com.example.jose.tiempos;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by jose on 06/11/2017.
 */

public class ParsearXml {
    public static ArrayList parsear(String xml){
        ArrayList <Prediccion> listaPrediccion= new ArrayList<>();
        DocumentBuilderFactory  dbf = DocumentBuilderFactory.newInstance();
        String fecha ="", maxima="",minima="";
        try {
            DocumentBuilder db= dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(xml)));
            NodeList prediccion =doc.getElementsByTagName("prediccion");
            NodeList dias = prediccion.item(0).getChildNodes();
            System.out.print(dias);
          for (int i = 0; i < dias.getLength();i++){
              Node dia = dias.item(i);

              if (dia.getNodeType() == Node.ELEMENT_NODE) {
                  fecha = dia.getAttributes().item(0).getNodeValue();
                  NodeList hijosDia = dia.getChildNodes();

                  for (int j = 0; j < hijosDia.getLength(); j++) {
                      String nombre=hijosDia.item(j).getNodeName();
                      if (nombre.equals("temperatura")) {
                          Node temperatura = hijosDia.item(j);

                          NodeList hijosTemperatura = temperatura.getChildNodes();
                          for (int k = 0; k < hijosTemperatura.getLength(); k++) {
                             // Node hijo = hijosTemperatura.item(k);
                              if (hijosTemperatura.item(k).getNodeName().equals("maxima")) {
                                  maxima = hijosTemperatura.item(k).getTextContent();
                                  System.out.print(maxima);
                              }
                              if (hijosTemperatura.item(k).getNodeName().equals("minima")) {
                                  minima = hijosTemperatura.item(k).getTextContent();
                              }
                          }
                      }
                  }
                  Prediccion p = new Prediccion(maxima,minima,fecha);
                  listaPrediccion.add(p);
              }

                  }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaPrediccion;
    }
}
