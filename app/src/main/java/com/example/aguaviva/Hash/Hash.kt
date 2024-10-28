package com.example.aguaviva

import java.security.MessageDigest

class Hash {

    companion object
    {

        fun SHA256(texto: String): String {
            // Obtenemos una instancia del algoritmo SHA256.
            val digest = MessageDigest.getInstance("SHA256")

            // Convertimos el string a un arreglo de bytes y calculamos el hash
            val byteArray = digest.digest(texto.toByteArray())

            // Convertimos el arreglo de bytes a una cadena hexadecimal
            val sb = StringBuilder()
            for (byte in byteArray) {
                sb.append(String.format("%02x", byte))
            }
            return sb.toString()
        }

    }

}