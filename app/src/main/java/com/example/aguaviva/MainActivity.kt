package com.example.aguaviva

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aguaviva.Activities.LoginActivity
import com.example.aguaviva.Activities.NuevoPagoActivity
import com.example.aguaviva.Activities.SucursalListAdapter
import com.example.aguaviva.DB.SQLiteManager
import com.example.aguaviva.Dialogo.Dialogo
import com.example.aguaviva.Fragments.ToolbarFragment
import com.example.aguaviva.LocationClient.LocationClient
import com.example.aguaviva.Network.Network
import com.example.ubicacioncelular.Mapa.Mapa
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), InyeccionDependencia {

    private val sqLiteManager = SQLiteManager(this)
    private var sucursal: Int = 0
    lateinit var nuevoPagoButton: ConstraintLayout

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Se cambia el color de la status bar, ya que por el archivo theme no se puede.
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrincipal)

        //this. deleteDatabase("Aguaviva.db")

        mainAguaviva()

    }

    private fun mainAguaviva()
    {

        // Widgets
        nuevoPagoButton = findViewById(R.id.nuevoPagoButton)

        // Se carga el fragment
        ToolbarFragment.newInstance(this, "Aguaviva", true)

        // Se cargan las coordenadas para el mapa en la webview
        LocationClient(this, this, this).obtenerUltimaUbicacion()

        // Se cargan los datos en la lista
        insertarSucursalLista()

        // Se captura el evento cuando el usuario presiona el boton de nuevo pago
        botonNuevoPagoButton(nuevoPagoButton)

        // Se captura el evento cuando el usuario pulsa el boton atras, no como un evento, sino como un callback
        callbackBackButton()

        // Se comprueba de que haya una conexion a internet por medio de corrutinas
        Network.verificarConexion(this, this)

    }

    private fun botonNuevoPagoButton(constraintLayout: ConstraintLayout)
    {

        // Widgets del boton
        val nuevoPagonuevoPagoButtonText: TextView = findViewById(R.id.nuevoPagonuevoPagoButtonText)
        val nuevoPagoButtonProgressBar: ProgressBar = findViewById(R.id.nuevoPagoButtonProgressBar)

        constraintLayout.setOnClickListener {


            //Se comprueba que la sucursal haya sido pulsada
            if (sucursal > 0)
            {

                // De ser un valor correcto, se crea la animacion de carga
                nuevoPagonuevoPagoButtonText.visibility = View.GONE
                nuevoPagoButtonProgressBar.visibility = View.VISIBLE

                // Se cambia de actividad
                startActivity(Intent(this, NuevoPagoActivity::class.java).apply {

                    putExtra("sucursalMain", sucursal.toString())

                })
                finish()



            }
            else {
                Toast.makeText(this, "Por favor ingrese una sucursal para continuar",Toast.LENGTH_LONG).show()
            }

        }

    }


    private fun insertarSucursalLista()
    {

        // Widgets
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        // Se almacenan los nombres y las ubicaciones de las sucursales en una lista
        val sucursales: MutableList<MutableMap<String, String>> = sqLiteManager.seleccionarDatosSucursales()

        // Se cargan los datos en la lista, junto con los eventos al pulsarlo. Primero, se ajusta la lista de forma lineal
        recyclerView.layoutManager = LinearLayoutManager(this)
        val sucursalListAdapter = SucursalListAdapter(this, this, sucursales)

        // Se carga el adaptador en la lista
        recyclerView.adapter = sucursalListAdapter

    }

    // Metodos que recibe los datos de las coordenadas, despues de habilitar los permisos
    override fun actualizarWebView(latitud: String, longitud: String)
    {
        // Widgets
        val webView: WebView = findViewById(R.id.webView)

        // Se cargan el webView
        webView.loadData(Mapa.mostrarMapa(latitud, longitud), "text/html", "UTF-8")
        webView.settings.javaScriptEnabled = true
        webView.settings.safeBrowsingEnabled = true


    }

    // Metodo que actualiza la WebView con la coordenadas de la sucursal seleccionada
    override fun obtenerIndiceLista(posicionLista: Int) {

        // Widgets
        val sucursalSeleccionadaText: TextView = findViewById(R.id.sucursalSeleccionadaText)

        // Se obtiene las coordenadas de la sucursal, y se almacenan en un mapa
        var coordenadas: MutableMap<String, String> = sqLiteManager.seleccionarLatitudLongitud(posicionLista)

        // Se actualiza la webView con las coordenadas de la sucursal
        actualizarWebView(coordenadas["latitud"].toString(), coordenadas["longitud"].toString())

        //Se imprime el nombre de la sucursal en la pantalla
        sucursalSeleccionadaText.text = sqLiteManager.seleccionarNombreSucursal(posicionLista)

        // Se habilita el boton para pagar
        habilitarBotonPagarHoy(posicionLista)

    }

    override fun obtenerIndicePrecioLista(posicionLista: Int, precio: Float) {
        TODO("Not yet implemented")
    }

    private fun habilitarBotonPagarHoy(indiceLista: Int)
    {
        // Si la sucursal es igual a 0, se habilita el boton de Nuevo Pago y se le cambia el color
        sucursal = indiceLista

    }

    // Metodo que muestra un dialog en el momento que el usuario pulsa el boton atras del dispositivo
    private fun callbackBackButton()
    {
        onBackPressedDispatcher.addCallback {

            Dialogo(this@MainActivity).salirDialog("SALIR", "¿Deseas salir de la aplicación?")

        }
    }


}