package com.example.aguaviva.BCV

import kotlin.math.roundToInt

class Divisa{

    companion object
    {

        var dolar: Float = 40.0F

        fun convertirDolares(monto: Float): Float
        {

            return (((monto * dolar) * 100).roundToInt().toFloat() / 100)

        }


    }

}
