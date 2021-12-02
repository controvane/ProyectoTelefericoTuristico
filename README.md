# Proyecto Teleferico Turistico

## 1 Introducción

El Teleferico de la ciudad de La Paz es una extraña y funcional mezcla entre en sistema de transporte masivo y un atractivo turistico que permite ver la ciudad desde un angulo que otros medios de transporte no permiten: Desde las Alturas. Es por esto que al ver por la ventana de una cabina, uno a veces se pregunta que es lo que tiene debajo, y eso es lo que esta aplicación desea abordar, y tal vez ayudar a calmar esa curiosidad. La Aplicación de este proyecto es una app que marca y da información sobre los distintos lugares que uno ve al estar en los telefericos de La Paz.

## 2 Contenido

### 2.1 Equipo

Carlos Javier Murguia

### 2.2 Modulos Abordados

El Modulo principal de la aplicación es un Fragmento de map que permite seguir al usuario por su viaje en las lineas del  teleferico, mostrandole puntos de  interes en el camino.

### 2.3 Componentes de desarrollo en Android

El proyecto en cuestión utiliza fragments para poder mostrar la información de cada uno de los marcadores. Esta información se jala de un servicio Web que nos entrega un JSON con la información de los puntos de interes. Esta información se consigue en la primera actividad que, para no quedar colgada y poder mostrar una animación de carga, realiza la carga y la animación en hilos distintos.

Una vez en el Mapa, la aplicación pide permisos para conocer la ubicación del usuario, puesto que lo seguira en su viaje por las lineas del teleferico. Con el permiso concedido crea un Hilo para poder seguir la ubicación del Usuario. Al Hacer Click en cualquiera de los marcadores representando a un punto de interes, La actividad principal envia la información de el marcador a una fragmento para que este muestre su información.

### 2.4 Librerias empleadas

* Volley: Una libreria de google que permite la conexión y consumo de servicios web. Curiosamente no esta incluida en el Android SDK.
* Maps: Una libreria que da las herramientas principales para poder controlar y usar un map de Google.
* Location: Una libreria complementaria a la anterior para poder conseguir la ubicación del usuario.
* Json-Java: Libreria para poder leer e interpretar archivos JSON.
  
## 3 Desarrollo

### 3.1 Detalle

Como se explicó previamente en el punto 2.3, La aplicación hace el request y carga el JSON en una lista de objetos serializables en la primera actividad. Mientras este proceso se esta ejecutando, se muestra una pequeña animación de carga.

Una vez realizada esta operación se envia la lista de marcadores a la actividad del Mapa, que se inicializara en la Univalle, para luego conseguir la ubicación del usuario y seguirlo, de esta forma podra escoger puntos interesantes sobre los que esta pasando. Esa información se mostrara en un fragmento que esta normalmente oculto debajo del mapa y que se mostrara para esta operación.

### 3.2 Flujo de Trabajo de la Aplicación

![Flujo de Funcionamiento del trabajo](https://github.com/controvane/ProyectoTelefericoTuristico/blob/5fc3522bcf82cd974baa7dc7bdb4cc8a88bdc8be/imagenes/funcionamientoApp.png)

### 3.3 Funcionalidad del Codigo

Al comenzar la aplicación se crean ambos Hilos, el de leer el JSON y el de la Animación:

```
//de la Main Activity
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
```

Luego el uso de Volley, pues con esta libreria estamos sacando la información del JSON. Este esta dentro de uno de los threads:

```
//la primera funcion se encarga de conseguir el json y ejecuta la segunda que llena la lista
//de marcadores

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
            //El objeto de tipo JSONArray nos entrega un array de los objetos JSON dentro de un objeto, con esto armamos nuestra lista.
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
```

La estructura de la clase Marcador es la siguiente:

```
public class Marcador implements Serializable {
    String nombre;
    String descripcion;
    //aunque actualmente no se usa, porque necesitaria un servidor web mas robusto,
    //este atributo nos permite extender la aplicación para que podamos poner imagenes en el InfoFragment
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
        this.dragable = dragable;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    //de aqui siguen getters y setters
}
```

La función "enviarAMaps" tiene el intent que coloca la lista dentro de un extra para enviarla a MapsActivity. Una vez en la otra clase, tenemos que iniciar el hilo que seguira al usuario. este seria el siguiente:

```
//Esto esta dentro de MapsActivity
//Esta función y la siguiente clase son para poder seguir al usuario mientras se mueve
    private void getMyLocation(){
        this.myLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        new Thread(new FollowerRunnable(this.myLocationProviderClient,this.mMap));
    }

    private class FollowerRunnable implements Runnable{

        FusedLocationProviderClient myLocation;
        GoogleMap mMap;

        public FollowerRunnable(FusedLocationProviderClient myLocation, GoogleMap mMap) {
            this.myLocation = myLocation;
            this.mMap = mMap;
        }

        @Override
        public void run() {
            Handler handler = new Handler(Looper.getMainLooper());
            while(!Thread.interrupted()){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        follower();
                    }
                });
            }
        }

        private void follower(){
            try{
                FollowerRunnable.this.myLocation.getLastLocation().addOnSuccessListener(location -> logicGetMyLocation(location));
            }
            catch(SecurityException ex){
                Log.d("ubicacion","hubo un error al conseguir la ubicacion actual");
            }
            catch(NullPointerException ex){
                Log.d("ubicacion","la ubicacion se consiguio, pero hubo un error al ir a la ubicación");
            }
        }

        private void logicGetMyLocation(Location location){
            FollowerRunnable.this.mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude())));
        }
    }
```

y por ultimo dentro del InfoFragment, como se muestra la información:

```
//en el onCreate creo el Listener
@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("infoMarcador", this, (requestKey, result) -> loadInfo(result));
    }

    //Asi muestra la información
    public void loadInfo(Bundle info){
        this.marker = (Marcador) info.getSerializable("marcador");
        this.nombreInfo.setText(this.marker.getNombre());
        this.descripcionInfo.setText(this.marker.getDescripcion());
        this.nombre.setVisibility(View.VISIBLE);
        this.nombreInfo.setVisibility(View.VISIBLE);
        this.descripcion.setVisibility(View.VISIBLE);
        this.descripcionInfo.setVisibility(View.VISIBLE);
        this.cerrar.setVisibility(View.VISIBLE);
    }

    //Y asi se oculta
    public void cerrarFragment(){
        this.nombre.setVisibility(View.INVISIBLE);
        this.nombreInfo.setVisibility(View.INVISIBLE);
        this.descripcion.setVisibility(View.INVISIBLE);
        this.descripcionInfo.setVisibility(View.INVISIBLE);
        this.cerrar.setVisibility(View.INVISIBLE);
    }
```

### 3.4 Rubros por integrante de equipo

Carlos Javier Murguia  desarrollo el proyecto por su cuenta.

## 4 Conclusiones

El funcionamiento de las distintas librerias y funciones permiten implementar varias cosas distintas. Pero para el caso especifico con el cual funciona este proyecto , entregan toda la funcionalidad que podria necesitar. Hay partes que en el futuro me gustaria agregar al proyecto:

1. Primero el uso de imagenes. Requeriria un servidor Web dedicado a la aplicación donde guardar las imagenes, y eso aumenta un paso mas al desarrolo de la arquitectura de la aplicación. Ademas habria que darse una vuelta por todas las lineas para sacarle fotos a los puntos de interes.
   
2. Que los usuario puedan agregar marcadores. Esto es un poco mas complicado, puesto que hay que poner las peticiones de los usuarios en una cola para ser aprobadas.
   
3. Como algo menos importante, tal vez colocar botones de zoom, para que el usuario pueda cambiar el rango al que puede escoger puntos de interes.

Estos cambios serian interesantes a futuro, pero la aplicación en su estado actual ya muestra la funcionalidad que pretende y demuestra la idea de lo que se desea hacer.