package com.univalle.javiermurguia.proyectoteleferico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.univalle.javiermurguia.proyectoteleferico.Models.Marcador;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Marcador> marcadores;
    private TextView cargando;
    private Thread hiloJson;
    private Thread hiloTexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.marcadores = new ArrayList<Marcador>();
        this.cargando = findViewById(R.id.textViewCargando);
        // este Thread es para poder cargar el Json sin Colgar la app
        this.hiloJson = new Thread(() -> {
            cargarLista();
        });
        //Este es para mostrar una animación de carga sin colgar la aplicación
        this.hiloTexto = new Thread(() -> {
            for(int i = 100; i < Double.POSITIVE_INFINITY; i+= 300){
                //esta parte nos permite escuchar cuando se interrumpa el hilo para poder cortar el ciclo
                if(Thread.interrupted()){
                    break;
                }
                correrHandlerTexto(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.hiloTexto.start();
        this.hiloJson.start();
    }

    //esta animación solo crea al handler para que se pueda loopear la animación de carga
    private void correrHandlerTexto(int i){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cambiarTexto();
            }
        },i);
    }

    //esta funcion crea la animación de carga
    private void cambiarTexto(){
        Log.d("flag", "estoy cambiando el texto");
        int estado = MainActivity.this.cargando.getText().toString().length();
        if(estado < 9){
            MainActivity.this.cargando.setText("Cargando .");
            return;
        }else if(estado < 11){
            MainActivity.this.cargando.setText("Cargando . .");
            return;
        }else if(estado < 13){
            MainActivity.this.cargando.setText("Cargando . . .");
            return;
        }else{
            MainActivity.this.cargando.setText("Cargando");
            return;
        }
    }

    //esta funcion nos envia a la actividad de los mapas
    private void enviarAMaps(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d("flag", "creando el intentd para ir a map");
                Intent intento = new Intent(MainActivity.this, MapsActivity.class);
                intento.putExtra("Marcadores", (Serializable) MainActivity.this.marcadores);
                Log.d("flag", "yendo a map");
                MainActivity.this.hiloTexto.interrupt();
                startActivity(intento);
                MainActivity.this.finish();
            }
        });
    }

    //Con esta funcion accedemos al JSON con la información de los marcadores
    private void cargarLista(){
        Log.d("flag","creando handler de cargar lista");
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        //De Momento con un servidor para Mock ups, es un proyecto de Programación Movil, no Web
        String url ="https://run.mocky.io/v3/a29d1188-e516-4340-b47a-81edf2806b4d";
        if (MainActivity.this.marcadores.size() <= 0) {
            JSONObject content = new JSONObject();
            Log.d("flag", "haciendo request de JSON");
            JsonObjectRequest jSonRequest = new JsonObjectRequest(Request.Method.POST, url,content, object -> fillApiContent(object), error -> Log.d("aviso","Ooops, hubo un error"));
            queue.add(jSonRequest);

        }else if(MainActivity.this.marcadores.size() > 0){
            Log.d("aviso","ya descargue las ubicaciones");
        }
    }

    //Con esta funcion se carga la lista de marcadores que se enviara a la Activity con el mapa
    private void fillApiContent(JSONObject object){
        JSONObject content;
        try {
            JSONArray array = object.getJSONArray("results");
            Log.d("flag", "Llenando lista");
            for (int i = 0;i < array.length(); i++) {
                content = array.getJSONObject(i);
                MainActivity.this.marcadores.add(new Marcador(
                        content.getString("name"),
                        content.getString("description"),
                        content.getBoolean("dragable"),
                        content.getDouble("latitude"),
                        content.getDouble("longitude")));
            }
        }catch (JSONException ex){
            Log.d("aviso","Hubo un error al extraer datos del Json, Ooops!");
        }
        enviarAMaps();
    }
}
