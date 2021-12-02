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

### Rubros por integrante de equipo

Carlos Javier Murguia  desarrollo el proyecto por su cuenta.