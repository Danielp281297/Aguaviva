package com.example.aguaviva.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.aguaviva.Activities.LoginActivity
import com.example.aguaviva.Activities.RegistroPagosActivity
import com.example.aguaviva.Dialogo.Dialogo
import com.example.aguaviva.R

class ToolbarFragment : Fragment() {


    companion object {

        // TODO: Rename and change types of parameters
        private var tituloToolbar: String? = null
        private var visibleRegistroPago: Boolean = false
        private var activity: Activity? = null

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(activity: Activity, titulo: String, botonRegistroPagoVisible: Boolean) =
            ToolbarFragment().apply {
                arguments = Bundle().apply {
                    tituloToolbar = titulo
                    visibleRegistroPago = botonRegistroPagoVisible
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_toolbar, container, false)

        // Widgets
        val mainToolbar: Toolbar = view.findViewById(R.id.mainToolbar)

        // Si no implementa los items del menu despues de onCreate, usa esta sentencia
        mainToolbar.let {

            setHasOptionsMenu(true)

            mainToolbar.setNavigationOnClickListener {
                    // Acción al hacer clic en el botón de navegación (si lo tienes)
                }

        }

        // Se cambia el nombre del titulo
        mainToolbar.title = tituloToolbar

        // Se carga los cambios en el toolbar
        (activity as AppCompatActivity).setSupportActionBar(mainToolbar)

        return view
    }

    // Metodo para cargar los item en la toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate( R.menu.toolbar_menu, menu)

        // Se captura el item
        val registroItem = menu.findItem(R.id.registroBotton)

        // Si esta en la actividad Nuevo Pago, se oculta este boton
        if (visibleRegistroPago == false)
            registroItem.isVisible = false

        super.onCreateOptionsMenu(menu, inflater)
    }

    // Metodo para capturar los eventos en la toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){

            R.id.registroBotton -> {
                irRegistroPago()
                true }

            R.id.salirBotton -> {
                salir()
                true }

            else -> super.onOptionsItemSelected(item)

        }

    }

    // Metodo para salir de la aplicacion
    private fun salir()
    {

        Dialogo(activity!!).salirDialog("SALIR", "¿Deseas salir de la aplicación?")

    }

    // Metodo para ingresar a los resultados
    private fun irRegistroPago()
    {

        // Se cambia la actividad
        context!!.startActivity(Intent(context, RegistroPagosActivity::class.java))

    }


}