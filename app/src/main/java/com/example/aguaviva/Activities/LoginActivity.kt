package com.example.aguaviva.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aguaviva.DB.SQLiteManager
import com.example.aguaviva.Dialogo.Dialogo
import com.example.aguaviva.LocationClient.LocationClient
import com.example.aguaviva.Network.Network
import com.example.aguaviva.R
import com.example.aguaviva.Hash
import com.example.aguaviva.MainActivity

class LoginActivity : AppCompatActivity() {

    val sqLiteManager = SQLiteManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Borrar la base de datos
        //this.deleteDatabase("Aguaviva.db")

        login()

    }

    // Metodo que controla los componentes de la vista activity-login.xml
    private fun login()
    {
        // Widgets
        val enviarButton: Button = findViewById(R.id.enviarButton)
        val usuarioText: EditText = findViewById(R.id.usuarioText)
        val contrasenaText: EditText = findViewById(R.id.contrasenaText)

        // Se obtienen los permisos de ubicacion para mostrar en la webview
        LocationClient(this, this, null).obtenerPermisos()

        // Se obtienen los permisos de acceso a los archivos del dispositivo

        // Se captura el evento cuando el usuario pulsa el boton ingresar
        //      Para obtener los datos de la tabla usuario con respecto al nombre
        enviarButton.setOnClickListener {

            // Se comprueba que haya conexion a internet
            if (Network.isConnect(this)) {

                // De ser asi, se almacenan las entradas en variables
                var usuario: String = usuarioText.text.toString()
                var contrasena: String = Hash.SHA256(contrasenaText.text.toString())

                // Se comprueba si las entradas no esten vacias
                if (usuario.isNotEmpty() && contrasena.isNotEmpty())
                {

                    // Se obtienen los datos de la tabla usuario
                    val usuarioSelect: Map<String, String>? = sqLiteManager.seleccionarDatosUsuario(usuario)

                    // Se comprueba de que los campos sea iguales a los datos obtenidos en la base de datos
                    if ((usuarioSelect != null) && (usuario == usuarioSelect["nombreUsuario"].toString()) &&
                        (contrasena == usuarioSelect["contrasenaUsuario"].toString()))
                    {

                        //De ser asi, se ingresa al MainActivity
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()

                    }
                    else
                    // Caso contrario, se muestra el aviso de error
                        Toast.makeText(this, "Datos incorrectos. Intente de nuevo", Toast.LENGTH_SHORT).show()

                }
                else
                // Caso contrario, se avisa del error
                    Dialogo(this).avisoDialog("AVISO", "No se admiten campos vacíos.")

            }
            else
            // En caso contrario, No se ingresa
                Dialogo(this).avisoDialog("AVISO", "Conéctese a una red para ingresar.")

        }

    }

}