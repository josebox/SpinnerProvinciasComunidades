package com.example.jose.tiempos;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {
    WebView wb;
    Spinner SPprovincias,SPmunicipios;
    //me creo otra base de datos
    SQLiteDatabase bbdd;
    //creamos dos variable que usaremos para obtener los codigos de provincia y munucipio
    //para posteriormente usarlos para enlazar la pagina web donde obtendremos los datos climatologicos
    int codMunicipio = 0;
    int codProvincia = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wb = (WebView)findViewById(R.id.wb);//donde monstaremos el html
        //creamos dos spinner que estran relacionados para poder mostrar pro y muni
        SPprovincias = (Spinner)findViewById(R.id.SPprovincias);
        SPmunicipios = (Spinner)findViewById(R.id.SPmunicipios);
        //-------------------BASE DE DATOS------------------------------//
        //creamos base de datos
        SQLiteOpenHelper sq = new SQLiteOpenHelper(this,"pYm",null,1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                //creamos las tablas
                String provincia="CREATE TABLE `provincias` (\n" +
                        "  `id_provincia` INTEGER,\n" +
                        "  `provincia` TEXT\n" +
                        ");";
                //cargamos la tabla en la bbdd
                db.execSQL(provincia);
                //cargamos todos los datos (insert)con un array con un arraylist
                ArrayList<String> sqlProvincia = DatosInsert.insertProvincia();
                for (String linea : sqlProvincia){
                    db.execSQL(linea);

                }
                String municipio="CREATE TABLE `municipios` (\n" +
                        "  `id_municipio` INTEGER,\n" +
                        "  `id_provincia` INTEGER,\n" +
                        "  `cod_municipio` INTEGER,\n" +
                        "  `DC` INTEGER,\n" +
                        "  `nombre` TEXT ,\n" +
                        "  PRIMARY KEY (`id_municipio`)\n" +
                        ");\n";
                db.execSQL(municipio);

                ArrayList<String> sqlMunicipio = DatosInsert.listaMunicipios();
                for (String linea : sqlMunicipio){
                    db.execSQL(linea);
                }
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };

        //cargamos finalmente la base de datos
        bbdd = sq.getWritableDatabase();
        //realizamos una select sobre la bbdd y le decimos que datos queremos que se carguen
        // en nuestro cursor para depues poder trabajar sobre el
        String selectProvincia = "Select * from provincias";
        Cursor cProvincias = bbdd.rawQuery(selectProvincia, null);
        TreeMap<Integer, String> tProvincia = new TreeMap<>();
        while (cProvincias.moveToNext()) {
            tProvincia.put(cProvincias.getInt(0), cProvincias.getString(1));
        }
        //---------------------------------SPINNER....................................//
      //cargamos el spinner con los datos de tProvincia
        String[] provinciasSpinner = tProvincia.values().toArray(new String[tProvincia.size()]);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, provinciasSpinner);
        SPprovincias.setAdapter(adaptador);
        //creamos el metodo con el cual cargaremos el segundo espiner dependiendo de la
        //opción que elijamos en spinner principal
        SPprovincias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //trabajaremos con el primer metodo que como su nombre id¡ndica es cuando hayamos selecionado algo actuara
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                codProvincia = position + 1;
                //Log.v("POSICION MUNICIPIO", String.valueOf(codProvincia));
                //mediante una select elegimos que datos queremos cojer de nuestra bbdd
                String selectMunicipios = "Select * from municipios WHERE id_provincia =(SELECT id_provincia FROM provincias WHERE provincia='"+adapterView.getSelectedItem()+"')";
               //cargamos el cursor para poder manejarlo despues
                Cursor cMunicipios = bbdd.rawQuery(selectMunicipios, null);
                TreeMap<Integer, String> tMunicipio = new TreeMap<>();
                while (cMunicipios.moveToNext()) {
                    tMunicipio.put(cMunicipios.getInt(0), cMunicipios.getString(4));
                }
                String[] municipioSpinner = tMunicipio.values().toArray(new String[tMunicipio.size()]);
                ArrayAdapter<String> adaptador = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, municipioSpinner);
                SPmunicipios.setAdapter(adaptador);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //creamos este metodo para obtener el valor de la opción que tu tocas en el spinner
        SPmunicipios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //trabajaremos con el primer metodo que como su nombre id¡ndica es cuando hayamos selecionado algo actuara
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                codMunicipio = position + 1;
                //Log.v("POSICION MUNICIPIO", String.valueOf(codMunicipio));
                //mediante una select elegimos que datos queremos cojer de nuestra bbdd

                lanzarVista(codProvincia,codMunicipio);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //------------------------------FIN SPINNER--------------------------------------//

        //-------------------codigo para mostrar el tiempo-------------------------------//
        //lanzarVista(codProvincia,codMunicipio);
    }

    private void lanzarVista(int codProvincia, int codMunicipio) {
        RequestQueue rq= Volley.newRequestQueue(this);
        String url ="http://www.aemet.es/xml/municipios/localidad_"+generarcodigoURL(codProvincia,codMunicipio) +".xml";
        Log.v("codMUNICIPIO", String.valueOf(codMunicipio));
        Log.v("codPROVINCIA", String.valueOf(codProvincia));
        Response.Listener oyente=new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                String xml_respuesta = (String) o;
                String titulo = "Tabla Prediciones";
                ArrayList<Prediccion> predicciones = ParsearXml.parsear(xml_respuesta);
                String encabezado[] = {"Fecha", "Maxima", "Minima"};
                ArrayList<String[]> filas = new ArrayList<>();
                for (Prediccion p : predicciones) {
                    String[] fila = new String[3];
                    fila[0] = p.getFecha();
                    fila[1] = p.gettMaxima();
                    fila[2] = p.gettMinima();
                    filas.add(fila);
                }
                String tablaHtml = TablaHtml.generarHtml(titulo, encabezado, filas);
                //-----------------------------------------------------//
                if (Build.VERSION.SDK_INT < 18) {
                    wb.clearView();
                } else {
                    wb.loadUrl("about:blank");
                }
                //-----------------------------------------------------//
                wb.loadData(tablaHtml, "text/html", null);
            }
        };
        Response.ErrorListener oyente_fallo=new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        };
        StringRequest respuesta=new StringRequest(url, oyente, oyente_fallo);
        rq.add(respuesta);
    }

    private String generarcodigoURL(int codProvincia, int codMunicipio) {
        String auxp= String.valueOf(codProvincia);
        String auxm = String.valueOf(codMunicipio);
        Log.d("codigoProv",auxp);
        Log.d("codigoMun",auxm);
        String codigoURL;
        if (codProvincia<10){
            auxp=0+auxp;
        }
        if (codMunicipio<10){
            auxm=00+auxm;
        }if(codMunicipio<100){
            auxm=0+auxm;
        }
        codigoURL= auxp+auxm;
        Log.d("codigoGenerado",codigoURL);
        return codigoURL;
    }
    //-----------------------------------------------------------------------------//
}
