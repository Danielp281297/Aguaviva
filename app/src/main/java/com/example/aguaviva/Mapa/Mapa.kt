package com.example.ubicacioncelular.Mapa

class Mapa {

    companion object
    {

        fun mostrarMapa(latitud: String, longitud: String): String
        {

            return """
                <!DOCTYPE html>
                <html lang="en">

                <head>
                  <meta charset="UTF-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1.0">
                  <meta http-equiv="X-UA-Compatible" content="ie=edge">
                  <title>Document</title>
                  <link rel="stylesheet" href="https://unpkg.com/leaflet@1.5.1/dist/leaflet.css"
                    integrity="sha512-xwE/Az9zrjBIphAcBb3F6JVqxf46+CDLwfLMHloNu6KEQCAWi6HcDUbeOfBIptF7tcCzusKFjFw2yuvEpDL9wQ=="
                    crossorigin="" />
                </head>

                <body>

                  <div id="myMap" style="height:100vh; width: 100vw;"></div>

                  <script src="https://unpkg.com/leaflet@1.5.1/dist/leaflet.js"
                   integrity="sha512-GffPMF3RvMeYyc1LWMHtK8EbPv0iNZ8/oTtHPx9/cc2ILxQ+u905qIwdpULaqDkyBKgOaB57QTMg7ztg8Jm2Og=="
                   crossorigin=""></script>
                  <script src="map.js"></script>
                </body>

                <style>

                    *
                    { margin: 0px;}

                </style>

                <script>
                    
                    // Se almacenan las coordendas
                    var coordenadas = [${latitud}, ${longitud}]
                    
                    // Se carga el mapa dentro de la etiqueta
                    let myMap = L.map('myMap').setView(coordenadas, 15)
                    
                    // Se aplican los datos referentes al mapa
                    const urlOpenLayers = 'https://a.tile.openstreetmap.org/{z}/{x}/{y}.png'
                    L.tileLayer(urlOpenLayers, {
                    maxZoom: 20,
                    }).addTo(myMap) 
                    
                    // Se inserta el marcador en la coordenada 
                    let marker = L.marker(coordenadas).addTo(myMap)

                    updateMap()

                </script>
                </html>
            """.trimIndent()

        }


    }

}