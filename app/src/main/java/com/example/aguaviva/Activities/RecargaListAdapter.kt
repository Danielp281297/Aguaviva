package com.example.aguaviva.Activities

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aguaviva.InyeccionDependencia
import com.example.aguaviva.R

class RecargaListAdapter(private var context: Context, var inyeccionDependencia: InyeccionDependencia, var dataset: List<Map<String, Any>>)
    : RecyclerView.Adapter<RecargaListAdapter.ViewHolder>() {

    // Clase interna usado como el controlador del template
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view)
    {

        // Widgets
        val litrosImageView: ImageView = view.findViewById(R.id.litrosImageView)
        val litrosTextView: TextView = view.findViewById(R.id.litrosTextView)
        val precioTextView: TextView = view.findViewById(R.id.precioTextView)

        // Metodo que captura el evento, cuando el cliente pulsa un elmento de la lista
        fun bindListener(listener: InyeccionDependencia, position: Int, precio: Float)
        {

            // Se captura el evento cuando el usuario pulsa un elemento de la lista
            itemView.setOnClickListener {

                // Se devuelve al MainActivity, el indice de la lista
                listener.obtenerIndicePrecioLista(position + 1, precio)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.activity_grid_presentaciones_recarga_template, parent, false)

        return ViewHolder(view)

    }

    override fun getItemCount() = dataset.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Se carga la imagen svg
        var drawable = context.getDrawable(dataset[position]["imagen"] as Int)
        holder.litrosImageView.setImageDrawable(drawable)

        // Se carga el resto
        holder.litrosTextView.text = "${dataset[position]["litros"] as String}L"
        holder.precioTextView.text = "${dataset[position]["precio"] as String}${'$'}"

        // Se captura el evento cuando se pulsa el elemento de la lista
        holder.bindListener(inyeccionDependencia, position, (dataset[position]["precio"] as String).toFloat())

    }

}