@file:Suppress("UNCHECKED_CAST")

package com.example.aguaviva.Activities

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aguaviva.DB.SQLiteManager
import com.example.aguaviva.Hash
import com.example.aguaviva.R
import com.example.aguaviva.Fragments.ToolbarFragment
import com.example.aguaviva.Network.Network

class PagoRealizadoActivity : AppCompatActivity() {

    var datosPago: MutableMap<String, Any?> = mutableMapOf()

    val sqLiteManager = SQLiteManager(this)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago_realizado)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Se obtiene los datos del map
        datosPago = obtenerDatosPago()

        pagoRealizado()

    }

    private fun obtenerDatosPago(): MutableMap<String, Any?>
    {

        // Se carga el toolbar desde el fragment
        ToolbarFragment.newInstance(this, "Nuevo Pago Realizado", true)

        // Se obtiene el map con los datos de la compra
        val bundle: Bundle? = intent.extras
        val aux: MutableMap<String, Any?> = bundle?.getSerializable("datosPago") as MutableMap<String, Any?>

        return aux

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun pagoRealizado()
    {

        // Widgets
        val volverButton: Button = findViewById(R.id.volverButton)

        // Se procesa la compra
        procesarPago()

        // Se captura el evento cuando el usuario pulsa el boton VOLVER
        volverButtonListener(volverButton)

        // Se muestra el numero de la operacion
        imprimirNumeroPago()

        // Se comprueba de que haya una conexion a internet por medio de corrutinas
        Network.verificarConexion(this, this)

    }

    private fun imprimirNumeroPago()
    {

        val numeroOperacionText: TextView = findViewById(R.id.numeroOperacionText)

        numeroOperacionText.text = " Número de la operación: ${sqLiteManager.seleccionarNumeroPago(1)}"

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun procesarPago()
    {

        // Widgets
        val webView: WebView = findViewById(R.id.qrWebView)

        sqLiteManager.insertarDatosPago(datosPago)

        // Se genera el hash
        val sha: String = Hash.SHA256(datosPago.toString())

        // Se muestra el Qr de la compra
        cargarUrlHash(webView, sha)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    private fun cargarUrlHash(webView: WebView, hash: String)
    {

        webView.loadUrl("https://api.qrserver.com/v1/create-qr-code/?size=360x360&data=$hash")
        webView.settings.javaScriptEnabled = true
        webView.settings.safeBrowsingEnabled = true

    }

    private fun volverButtonListener(button: Button)
    {
        // Se captura el evento cuando el boton pagar hoy es pulsado
        button.setOnClickListener {

            finish()

        }
    }

}
