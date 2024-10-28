package com.example.aguaviva.Network

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import com.example.aguaviva.Dialogo.Dialogo
import kotlinx.coroutines.*

class Network {

    companion object
    {

        fun isConnect(context: Context): Boolean
        {

            //Se crea el objeto que permitira comprobar la conexion
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            var network = connectivityManager.activeNetworkInfo

            //Se retorna el estado de la conexion
            return network != null && network.isConnected

        }

        // Metodo que, por medio de un GlobalScope, compruebe que no haya conexion a internet durante la ejecucion de la
        // aplicacion al ingresar
        @OptIn(DelicateCoroutinesApi::class)
        fun verificarConexion(activity: Activity, context: Context)
        {

            GlobalScope.launch {

                do
                {
                    // En el caso de que non haya conexion a internet, se muestra el Dialog, y se devuelve al login
                    // Borrando las actividades de la BackStack
                    if (!Network.isConnect(context))
                    {
                        withContext(Dispatchers.Main)
                        {

                            Dialogo(activity).avisoSinConexionDialog("AVISO", "Sin conexión a internet. Se cierra la sesión.")


                        }
                        // Se termina el bucle, para evitar que se repita el mensaje
                        break
                    }
                }while (true)

            }

        }

    }

}