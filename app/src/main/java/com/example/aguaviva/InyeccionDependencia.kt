package com.example.aguaviva

interface InyeccionDependencia {

    fun actualizarWebView(latitud: String, longitud: String)

    fun obtenerIndiceLista(posicionLista: Int)

    fun obtenerIndicePrecioLista(posicionLista: Int, precio: Float)

}