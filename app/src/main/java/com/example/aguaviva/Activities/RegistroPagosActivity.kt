package com.example.aguaviva.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aguaviva.DB.SQLiteManager
import com.example.aguaviva.Dialogo.Dialogo
import com.example.aguaviva.R
import com.example.aguaviva.Fragments.ToolbarFragment
import com.example.aguaviva.MainActivity
import com.example.aguaviva.Network.Network

class RegistroPagosActivity : AppCompatActivity() {

    val sqLiteManager = SQLiteManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_pagos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Se captura el evento cuando el usuario pulsa el boton atras, no como un evento, sino como un callback
        callbackBackButton()

        registroPagos()


    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun registroPagos()
    {

        // Widgets
        val registroTableLayout: TableLayout = findViewById(R.id.registroTableLayout)

        // Se carga el toolbar desde el fragment
        ToolbarFragment.newInstance(this, "Registro de pagos", false)

        // Se almacenan los datos de la consutla en una lista
        val registroPagos: MutableList<Map<String, String>> = sqLiteManager.seleccionarRegistroPago(1)

        // Se cargan los datos de la tabla uno por uno
        for (elemento in registroPagos)
        {

            // Se carga la view por medio del inflate
            val tableRow = LayoutInflater.from(this).inflate(R.layout.fila_tabla_template, null, false) as TableRow

            tableRow.findViewById<TextView>(R.id.numero).text =         elemento["idPago"]
            tableRow.findViewById<TextView>(R.id.fechaPago).text =      elemento["fechaPago"]
            tableRow.findViewById<TextView>(R.id.sucursalPago).text =   elemento["sucursalPago"]
            tableRow.findViewById<TextView>(R.id.litrosPago).text =     elemento["litrosPago"]
            tableRow.findViewById<TextView>(R.id.precioPago).text =     elemento["precioPago"]

            // Se insertan en la tablelayout
            registroTableLayout.addView(tableRow)

        }

        // Se comprueba de que haya una conexion a internet por medio de corrutinas
        Network.verificarConexion(this, this)

    }

    // Metodo que cambia la actividad hacia la mainActivity, cuando el usuario pulsa el boton atras
    private fun callbackBackButton()
    {
        onBackPressedDispatcher.addCallback {

            startActivity(Intent(this@RegistroPagosActivity, MainActivity::class.java))
            finishAffinity()

        }
    }

}

