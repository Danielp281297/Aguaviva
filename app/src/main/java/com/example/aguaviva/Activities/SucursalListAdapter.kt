package com.example.aguaviva.Activities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aguaviva.InyeccionDependencia
import com.example.aguaviva.R

class SucursalListAdapter(private var context: Context, private var inyeccionDependencia: InyeccionDependencia, private val dataSet: List<Map<String, String>>)
    : RecyclerView.Adapter<SucursalListAdapter.ViewHolder>() {

    // Se crea la una clase interna como controlador de las vistas, en los elementos de la lista
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val sucursalText: TextView = view.findViewById(R.id.sucursalText)
        val ubicacionText: TextView = view.findViewById(R.id.ubicacionText)

        // Metodo que permite capturar el evento cuando
        fun bind(listener: InyeccionDependencia, position: Int)
        {

            // Se captura el evento cuando el usuario pulsa un elemento de la lista
            itemView.setOnClickListener {

                // Se devuelve al MainActivity, el indice de la lista
                listener.obtenerIndiceLista(position + 1)
            }

        }

    }

    // Metodo que "Inserta" el formato en la lista personalizada en la recycleView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_lista_sucursal_template, parent, false)

        return ViewHolder(view)

    }

    override fun getItemCount() = dataSet.size

    // Metodo que imprime los datos de la lista en los textos
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.sucursalText.text = dataSet[position]["nombre"]
        holder.ubicacionText.text = dataSet[position]["ubicacion"]

        // Se captura el evento
        holder.bind(inyeccionDependencia, position)

    }

}
