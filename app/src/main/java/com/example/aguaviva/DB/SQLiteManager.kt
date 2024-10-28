package com.example.aguaviva.DB

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class SQLiteManager(context: Context): SQLiteOpenHelper(context, nombre, null, 1) {

    companion object
    {

        var nombre = "Aguaviva.db"

    }



    override fun onCreate(p0: SQLiteDatabase?) {

        //Se crean las tablas
        p0?.execSQL(Consulta.crearTablaUsuario)
        p0?.execSQL(Consulta.crearTablaPersona)
        p0?.execSQL(Consulta.crearTablaSucursales)
        p0?.execSQL(Consulta.crearTablaRecarga)
        p0?.execSQL(Consulta.crearTablaPago)

        // Se insertan los datos iniciales
        p0?.execSQL(Consulta.insertarTablaSucursales)
        p0?.execSQL(Consulta.insertarTablaRecarga)
        p0?.execSQL(Consulta.insertarUsuarioInicial)
        p0?.execSQL(Consulta.insertarPersonaInicial)

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

        // Se borran las tablas en este MISMO ORDEN
        p0?.execSQL(Consulta.borrarTablaSucursales("Pago"))
        p0?.execSQL(Consulta.borrarTablaSucursales("Sucursal"))
        p0?.execSQL(Consulta.borrarTablaSucursales("Persona"))
        p0?.execSQL(Consulta.borrarTablaSucursales("Usuario"))

        // ...Y se vuelven a crear
        onCreate(p0)

    }

    fun seleccionarDatosUsuario(nombreUsuario: String): MutableMap<String, String>?
    {

        // Se abre la base de datos
        val db = this.writableDatabase

        // Se almacenan los datos en un Map
        var usuario: MutableMap<String, String>? = null

        // Se obtiene los datos de la consulta
        val resultado = db.rawQuery(Consulta.seleccionarUsuarioNombre(nombreUsuario), null)

        if (resultado != null && resultado.moveToFirst())
            usuario = mutableMapOf("nombreUsuario" to resultado.getString(0), "contrasenaUsuario" to resultado.getString(1))

        // Se cierra la base de datos
        db.close()

        // Se retorna los datos obtenidos
        return usuario

    }

    fun seleccionarDatosSucursales(): MutableList<MutableMap<String, String>>
    {

        //Se abre la base de datos
        val db = this.writableDatabase

        var lista: MutableList<MutableMap<String, String>> = mutableListOf()

        //Se extraen los datos en la base de datos
        val resultado = db.rawQuery(Consulta.seleccionarNombreUbicacionSucursales, null)

        // Se comprueba si se extrayeron los datos
        if (resultado != null && resultado.moveToFirst()) {
            // Se almacenan los datos en un mapa
            do {

                // Se crea y almacena el mapa en la lista
                var sucursal: MutableMap<String, String> = mutableMapOf("nombre" to resultado.getString(0),
                                                                        "ubicacion" to resultado.getString(1))

                lista.add(sucursal)

            }while (resultado.moveToNext())

        }

        // Se cierra la base de datos
        resultado.close()
        db.close()

        // Se retorna la lista
        return lista

    }

    fun seleccionarLatitudLongitud(id: Int): MutableMap<String, String>
    {

        var coordenadas: MutableMap<String, String> = mutableMapOf()

        // Se abre la base de datos
        val db = this.readableDatabase

        // Se almacena el contenido de la consulta
        val resultado = db.rawQuery(Consulta.seleccionarCoordenadas(id), null)

        if (resultado != null && resultado.moveToFirst())
        {
            //Se almacena en un mapa mutable
            coordenadas = mutableMapOf("latitud" to resultado.getString(0),
                "longitud" to resultado.getString(1))

        }

        // Se cierra la base de datos
        resultado.close()
        db.close()

        // Se devuelve el valor de la lista
        return coordenadas

    }

    fun insertarDatosPago(datosPago: Map<String, Any?>)
    {

        // Se abre la base de datos
        val db = this.writableDatabase

        // Se crea y ejecuta la consulta
        db.execSQL(Consulta.insertarPago(datosPago["idPersona"].toString().toLong(),
                                        datosPago["idSucursal"].toString().toInt(),
                                        datosPago["idRecarga"].toString().toInt()))

        // Se cierra la base de datos
        db.close()

    }

    fun seleccionarNumeroPago(idUsuario: Int): Long
    {

        var aux: Long = 0

        // Se abre la base de datos
        val db = this.readableDatabase

        // Se ejecuta la consutla
        val resultado = db.rawQuery(Consulta.seleccionarNumeroPago(idUsuario), null)

        // Se obtiene el dato si se encuentra
        if (resultado != null && resultado.moveToFirst())
            aux = resultado.getString(0).toLong()

        // Se cierra la base de datos
        resultado.close()
        db.close()

        // Se retorna el valor
        return aux
    }

    fun seleccionarRegistroPago(idUsuario: Int): MutableList<Map<String, String>>
    {
        // Se abre la base de datos
        var db = this.readableDatabase

        // Se almacenan las filas en una lista mutable
        var aux: MutableList<Map<String, String>> = mutableListOf()

        // Se ejecuta la consulta
        val resultados = db.rawQuery(Consulta.seleccionardatosPagoId(idUsuario), null)

        // Si los datos se obtuvieron, se almacenan en un map
        if (resultados != null && resultados.moveToFirst())
        {

            // Se almacenan las filas por medio de un bucle
            do {

                var datos: MutableMap<String, String> = mutableMapOf("idPago" to resultados.getString(0),
                    "fechaPago" to resultados.getString(1),
                    "sucursalPago" to resultados.getString(2),
                    "litrosPago" to resultados.getString(3),
                    "precioPago" to resultados.getString(4))

                // Se almacenan los resultados en la lista
                aux.add(datos)

            }while (resultados.moveToNext())

        }

        // Se cierra la base de datos
        resultados.close()
        db.close()

        // Se retorna el map
        return aux
    }

    fun seleccionarNombreSucursal(idSucursal: Int):String
    {
        var aux: String = " "

        // Se abre la base de datos
        val db = this.readableDatabase

        // Se ejecuta la consutla
        val resultado = db.rawQuery(Consulta.seleccionarNombreSucursal(idSucursal), null)

        // Se obtiene el dato si se encuentra
        if (resultado != null && resultado.moveToFirst())
            aux = resultado.getString(0)

        // Se cierra la base de datos
        resultado.close()
        db.close()

        // Se retorna el valor
        return aux
    }

    fun seleccionarLitrosRecarga(idRecarga: Int):String
    {
        var aux: String = " "

        // Se abre la base de datos
        val db = this.readableDatabase

        // Se ejecuta la consutla
        val resultado = db.rawQuery(Consulta.seleccionarLitrosRecarga(idRecarga), null)

        // Se obtiene el dato si se encuentra
        if (resultado != null && resultado.moveToFirst())
            aux = resultado.getString(0)

        // Se cierra la base de datos
        resultado.close()
        db.close()

        // Se retorna el valor
        return aux
    }

}