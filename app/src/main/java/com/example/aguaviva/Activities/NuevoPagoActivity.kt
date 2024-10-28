package com.example.aguaviva.Activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aguaviva.BCV.Divisa
import com.example.aguaviva.DB.SQLiteManager
import com.example.aguaviva.Dialogo.Dialogo
import com.example.aguaviva.InyeccionDependencia
import com.example.aguaviva.R
import com.example.aguaviva.Fragments.ToolbarFragment
import com.example.aguaviva.MainActivity
import com.example.aguaviva.Network.Network
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.ByteArrayOutputStream
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class NuevoPagoActivity : AppCompatActivity(), InyeccionDependencia{

    val sqLiteManager = SQLiteManager(this)
    var selectedImage: Bitmap? = null
    lateinit var pagoSpinner: Spinner

    val datosPago: MutableMap<String, Any?> = mutableMapOf("datePago" to null,
                                                          "idPersona" to 1,
                                                          "idSucursal" to null,
                                                          "idRecarga" to null,
                                                          "comprobantePago" to false,
                                                          )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_pago)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        nuevoPago()
    }

    private fun nuevoPago()
    {

        // Widgets
        val siguienteButton: Button = findViewById(R.id.siguienteButton)
        val cargarComprobanteButton: Button = findViewById(R.id.cargarComprobanteButton)
        val estadoSpinner: Spinner = findViewById(R.id.estadoSpinner)
        val ciudadSpinner: Spinner = findViewById(R.id.ciudadSpinner)
        val municipioSpinner: Spinner = findViewById(R.id.municipioSpinner)
        val sucursalSpinner: Spinner = findViewById(R.id.sucursalSpinner)
        pagoSpinner = findViewById(R.id.pagoSpinner)

        // Se carga el toolbar desde el fragment
        ToolbarFragment.newInstance(this, "Nuevo Pago", false)

        //Se obtiene el indice de la sucursal para de la MainActivity
        val intent: Intent = intent
        val sucursalInicial = intent.getStringExtra("sucursalMain")!!.toInt()

        // Se cargan los datos en los spinner
        cargarSpinner(estadoSpinner, arrayOf("Dtto. Capital"))
        cargarSpinner(ciudadSpinner, arrayOf("Caracas"))
        cargarSpinner(municipioSpinner, arrayOf("Libertador"))
        cargarSpinner(sucursalSpinner, arrayOf("La Yaguara", "Baruta", "Sabana Grande", "Caricuao", "La Candelaria"))
        cargarSpinner(pagoSpinner, arrayOf("Seleccione", "Pago Móvil"))

        // Se muestra la sucursal enviada desde el MainActivity
        sucursalSpinner.setSelection(sucursalInicial - 1)

        // Se cargan los elementos en la RecicleView
        insertRecargaLista()

        //Se captura el evento cuando el usuario cambia la opcion del sucursalSpinner
        sucursalSpinnerListener(sucursalSpinner)

        // Se captura el evento cuando el usuario pulsa el boton "Cargar comprobante"
        cargarComprobanteListener(cargarComprobanteButton)

        // Se captura el evento cuando el usuario pulsa un elemento del pagoSpinner
        pagoSpinnerListener(pagoSpinner)

        // Se captura el evento cuando el usuario pulsa el boton Siguiente
        SiguienteListener(siguienteButton)

        // Se comprueba de que haya una conexion a internet por medio de corrutinas
        Network.verificarConexion(this, this)

        // Metodo que captura cuando el usuario pulsa el boton atras del dispositivo
        callbackBackButton()

    }

    // Metodo que regresa a la MainActivity cuando el usuario pulsa el boton atras del dispositivo
    private fun callbackBackButton()
    {
        onBackPressedDispatcher.addCallback {

            startActivity(Intent(this@NuevoPagoActivity, MainActivity::class.java))

        }
    }

    private fun SiguienteListener(button: Button)
    {

        //Se captura el evento cuando el usuario pulsa el boton siguiente
        button.setOnClickListener {

            // Se comprueba que todos los campos se hayan sido llenados
            if (datosPago["idSucursal"] != null &&
                datosPago["idRecarga"] != null &&
                datosPago["comprobantePago"] == true &&
                pagoSpinner.selectedItemPosition > 0)
                    showBottomSheetDialog()
            else
                Toast.makeText(this, "Ingrese los campos correspondientes, por favor", Toast.LENGTH_SHORT).show()

        }

    }

    private fun showBottomSheetDialog()
    {

        // Se crea BottomSheetDialog
        val bottonSheetDialog: BottomSheetDialog = BottomSheetDialog(this)

        // Se almacena el contenido del dialog
        val dialogView = LayoutInflater.from(this).inflate(R.layout.datos_pago_bottom_sheet_dialog_template, null)

        // Widgets

        val fechaText: TextView = dialogView.findViewById(R.id.fechaText)
        val nombreSucursalText: TextView = dialogView.findViewById(R.id.nombreSucursalText)
        val litrosText: TextView = dialogView.findViewById(R.id.litrosText)
        val precioText: TextView = dialogView.findViewById(R.id.precioText)
        val tipoPagoText: TextView = dialogView.findViewById(R.id.tipoPagoText)
        val pagarHoyButton: Button = dialogView.findViewById(R.id.pagarHoyButton)

        // Se muestran los datos en pantalla
        fechaText.text = "Fecha: " + SimpleDateFormat("dd-MM-YYYY").format(Date())
        nombreSucursalText.text = "Sucursal: " + sqLiteManager.seleccionarNombreSucursal(datosPago["idSucursal"] as Int)
        litrosText.text = "Recarga: " + sqLiteManager.seleccionarLitrosRecarga(datosPago["idRecarga"] as Int) + " L"
        precioText.text = "Precio: " + findViewById<TextView>(R.id.precioBolivaresText).text
        tipoPagoText.text = "Tipo de pago: Pago Móvil"

        // Se captura el evento cuando el usuario pulsa el boton "Pagar Hoy"
        pagarHoyListener(pagarHoyButton)

        // Se muestra el dialogView en el bottonSheetDialog
        bottonSheetDialog.setContentView(dialogView)
        bottonSheetDialog.setCancelable(true)
        bottonSheetDialog.setCanceledOnTouchOutside(true)
        bottonSheetDialog.show()

    }

    private fun sucursalSpinnerListener(spinner: Spinner)
    {

        spinner.onItemSelectedListener =
            object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                datosPago["idSucursal"] = p2 + 1

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }


        }

    }

    private fun pagoSpinnerListener(spinner: Spinner)
    {
        // Widgets
        val datosPagoFragmentContainerView: FragmentContainerView = findViewById(R.id.datosPagoFragmentContainerView)

        // Se captura el evento
        spinner.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                // Se crea el manager del FragmentConteiner
                if (p2 > 0)
                    datosPagoFragmentContainerView.visibility = 1
                else datosPagoFragmentContainerView.visibility = -1


            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }


        }

    }

    @SuppressLint("IntentReset")
    private fun cargarComprobanteListener(button: Button)
    {

        // Se captura el evento...
        button.setOnClickListener {

            // Se accede a la galeria para encontrar el comprobante de pago
            // Se cambia de actividad de forma habitual, solo que accedemos a una actividad externa a la aplicacion
            openGallery()

        }

    }

    private fun datosPagoNull(map: Map<String, Any?>): Boolean {

        var b =  false
        for ((_, value) in map) {
            if (value == null)
            {
                b = true
                break
            }
        }

        return b
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Widgets

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            val imageUri: Uri? = data?.data

            // Obtener el Bitmap a partir del Uri
            selectedImage = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)

            if (selectedImage != null) {

                val byteArrayOutputStream = ByteArrayOutputStream()
                selectedImage?.compress(Bitmap.CompressFormat.PNG, 100, ByteArrayOutputStream())
                val byteArray = byteArrayOutputStream.toByteArray()

                // Se cambia el vallor del ComprobantePago en los datosPago
                datosPago["comprobantePago"] = selectedImage.toString()

                mostrarComprobanteAviso()

            }

        }

    }

    private fun mostrarComprobanteAviso()
    {

        // Widgets
        val comprobanteAvisoTextView: TextView = findViewById(R.id.comprobanteAvisoTextView)
        comprobanteAvisoTextView.text = "Comprobante cargado con éxito"

        // datosComprobante es verdadero
        datosPago["comprobantePago"] = true

    }

    private fun insertRecargaLista()
    {

        // Widgets
        val presentacionRecargaList: RecyclerView = findViewById(R.id.presentacionRecargaList)

        // Se almacenan los datos
        val datos: List<Map<String, Any>> = listOf(mutableMapOf("imagen" to R.drawable.lt5, "litros" to "5", "precio" to "4.5")
                                                   ,mutableMapOf("imagen" to R.drawable.lt20, "litros" to "20", "precio" to "15")
                                                   ,mutableMapOf("imagen" to R.drawable.lt30, "litros" to "30", "precio" to "25"))

        // Se crea el adaptador
        val adaptador = RecargaListAdapter(this, this, datos)

        // Se adapta la lista
        presentacionRecargaList.layoutManager = LinearLayoutManager(this)
        presentacionRecargaList.adapter = adaptador

        // Siendo un grid, se usa el manager para hacerlo horizontal
        presentacionRecargaList.layoutManager = GridLayoutManager(this, 3)

    }

    // Metodo para cargar los spinners con los valores iniciales
    private fun cargarSpinner(spinner: Spinner, dataset: Array<String>)
    {

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, dataset)

        spinner.adapter = adapter

    }

    private fun pagarHoyListener(button: Button)
    {

        // Se captura el evento cuando el boton pagar hoy es pulsado
        button.setOnClickListener {

            // Se captura la hora y la fecha del pago
            datosPago["datePago"] = (Date().time.toInt() + Date().date)

            // Se comprueba que el map no este vacio
            if (!datosPagoNull(datosPago))
            {

                // Se pasa el map por medio de un bundle
                val bundle = Bundle()
                bundle.putSerializable("datosPago", datosPago as Serializable)

                // Se cambia a la actividad
                startActivity(Intent(this, PagoRealizadoActivity::class.java).apply {

                    putExtras(bundle)

                })
                 finish()


            }
            else
                Dialogo(this).avisoDialog("AVISO", "Por favor, ingrese los campos correspondientes para continuar.")


        }
    }

    override fun actualizarWebView(latitud: String, longitud: String) {
        TODO("Not yet implemented")
    }

    override fun obtenerIndiceLista(posicionLista: Int) {
        TODO("Not yet implemented")
    }

    // Metodo heredado, que obtiene el indice en la lista, para mostrar el precio de la Recarga
    override fun obtenerIndicePrecioLista(posicionLista: Int, precio: Float) {

        // Widgets
        val precioDolaresText: TextView = findViewById(R.id.precioDolaresText)
        val precioBolivaresText: TextView = findViewById(R.id.precioBolivaresText)
        val totalSubtituloTextView: TextView = findViewById(R.id.totalSubtituloTextView)

        // Se muestran los precios en dolares y en Bolivares
        totalSubtituloTextView.text = "Paga hoy: "
        precioDolaresText.text = "${precio}${'$'}"
        precioBolivaresText.text = "${Divisa.convertirDolares(precio)}BsD"

        // Se cambia el idRecarga en los datos del pago
        datosPago["idRecarga"] = posicionLista

    }


}