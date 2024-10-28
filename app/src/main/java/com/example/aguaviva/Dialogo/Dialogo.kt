package com.example.aguaviva.Dialogo

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.example.aguaviva.Activities.RegistroPagosActivity
import com.example.aguaviva.Activities.LoginActivity
import kotlinx.coroutines.delay as delay1

class Dialogo(private var activity: Activity) {

    fun avisoDialog(titulo: String, mensaje: String)
    {

        val alertDialog = AlertDialog.Builder(activity)


        alertDialog.setTitle(titulo)
        alertDialog.setMessage(mensaje)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Aceptar"){ _,_ ->}
        alertDialog.show()

    }

    fun salirDialog(titulo: String, mensaje: String)
    {

        val alertDialog = AlertDialog.Builder(activity)


        alertDialog.setTitle(titulo)
        alertDialog.setMessage(mensaje)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Si"){ _,_ ->

            // Se pasa a la actividad del login
            activity.startActivity(Intent(activity, LoginActivity::class.java))

            // Se borran las actividades en espera en la pila
            activity.finishAffinity()

        }
        alertDialog.setNegativeButton("No"){_,_->}
        alertDialog.create()
        alertDialog.show()

    }

    fun avisoSinConexionDialog(titulo: String, mensaje: String)
    {

        val alertDialog = AlertDialog.Builder(activity)


        alertDialog.setTitle(titulo)
        alertDialog.setMessage(mensaje)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Aceptar"){ _,_ ->

            // Se retorna al login
            activity.startActivity(Intent(activity, LoginActivity::class.java))
            activity.finishAffinity()

        }
        alertDialog.show()

    }

}