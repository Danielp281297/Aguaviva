package com.example.aguaviva.LocationClient

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.example.aguaviva.InyeccionDependencia
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationClient(private var context: Context, private var activity: Activity, var inyeccionDependencia: InyeccionDependencia?) {


    // Se crea el objeto que permita convertir la aplicacion en un cliente. Para esto, se debe implementar la
    //  Dependencia de implementation(libs.play.services.location)  en el build.grandle
    private var fusedLocationClient: FusedLocationProviderClient
                            = LocationServices.getFusedLocationProviderClient(activity)

    // Variable para la peticion de permiso de localizacion
    val LOCATION_PERMISSION_REQUEST_CODE: Int = 1

    // Variable para comprobar los permisos
    var permisosObtenidos: Boolean = false

    //Metodo que busca los permisos del dispositivo a la geolocalizacion
    fun obtenerPermisos()
    {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)

            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)

        else
            permisosObtenidos = true

    }

    @SuppressLint("MissingPermission")
    fun obtenerUltimaUbicacion(){

        fusedLocationClient.lastLocation.addOnSuccessListener {

            if (it != null)
            {
                // Se muestra la ubicacion del usuario, cuando ingresa a la MainActivity
                inyeccionDependencia?.actualizarWebView(it.latitude.toString(), it.longitude.toString())

            }

        }

    }

}