package com.example.controledad

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // Constantes de la clase (accesibles sin crear un objeto)
    companion object {
        private const val TAG = "CicloVida"              // tag para filtrar en Logcat: tag:CicloVida
        private const val KEY_CONTADOR = "contador"       // clave con la que guardamos el contador al girar
    }

    // El contador vive en una propiedad de la clase (no dentro de onCreate)
    // para poder usarlo y guardarlo desde varios métodos.
    private var contador = 0
    private lateinit var txtContador: TextView           // lateinit: se inicializa en onCreate, no al declarar

    // onCreate: se llama al crear la Activity (también al recrearla tras girar el móvil)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)           // carga el XML de la pantalla
        Log.d(TAG, "MainActivity onCreate")

        // Evita que la barra de estado o de navegación tapen el contenido (pantalla completa en Android 15)
        val raiz = findViewById<android.view.View>(R.id.main)
        val pIzq = raiz.paddingLeft; val pArr = raiz.paddingTop
        val pDer = raiz.paddingRight; val pAba = raiz.paddingBottom
        ViewCompat.setOnApplyWindowInsetsListener(raiz) { v, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(pIzq + barras.left, pArr + barras.top, pDer + barras.right, pAba + barras.bottom)
            insets
        }

        // Referencias a las vistas del XML (busca por el id)
        val editNombre = findViewById<EditText>(R.id.editNombre)
        val editEdad = findViewById<EditText>(R.id.editEdad)
        val btnComprobar = findViewById<Button>(R.id.btnComprobar)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiar)
        txtContador = findViewById(R.id.txtContador)

        // savedInstanceState trae lo guardado en onSaveInstanceState (es null si la app se abre por primera vez).
        // ?.getInt(...) solo se ejecuta si no es null; ?: 0 da el valor por defecto en ese caso.
        contador = savedInstanceState?.getInt(KEY_CONTADOR) ?: 0
        actualizarContador()

        btnComprobar.setOnClickListener {
            val nombre = editNombre.text.toString().trim()               // trim() quita espacios sobrantes
            // toIntOrNull() devuelve null en vez de lanzar excepción si no es un número (vacío, "abc"...).
            // Con toInt() la app se cerraría por NumberFormatException.
            val edad = editEdad.text.toString().trim().toIntOrNull()

            if (nombre.isEmpty()) {
                // Versión de Toast que recibe directamente el id del string
                Toast.makeText(this, R.string.toast_nombre_vacio, Toast.LENGTH_SHORT).show()
            } else if (edad == null || edad < 0 || edad > 120) {
                // Aquí se cumple "no es número" (null) o "fuera de rango". El || corta en cuanto
                // uno es true, así que nunca comparamos un null con un número.
                Toast.makeText(this, R.string.toast_edad_invalida, Toast.LENGTH_SHORT).show()
            } else {
                // Datos correctos: subimos el contador y abrimos la segunda pantalla
                contador++
                actualizarContador()

                // Intent explícito: "de esta Activity (this) a ResultadoActivity"
                val intent = Intent(this, ResultadoActivity::class.java)
                // putExtra(clave, valor): metemos los datos en el Intent usando las constantes del companion
                intent.putExtra(ResultadoActivity.EXTRA_NOMBRE, nombre)
                intent.putExtra(ResultadoActivity.EXTRA_EDAD, edad)
                startActivity(intent)                                     // abre la nueva pantalla
            }
        }

        // Limpiar: solo borra los dos campos; el contador NO se toca
        btnLimpiar.setOnClickListener {
            editNombre.text.clear()
            editEdad.text.clear()
        }
    }

    // Pone el texto "Comprobaciones: N" usando el string con hueco %1$d
    private fun actualizarContador() {
        txtContador.text = getString(R.string.contador, contador)
    }

    // Se llama ANTES de destruir la Activity (por ejemplo al girar). Aquí guardamos lo que no queremos perder.
    // Android devuelve este Bundle a onCreate cuando recrea la pantalla.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CONTADOR, contador)
    }

    // Resto de métodos del ciclo de vida: solo escriben una línea en Logcat
    override fun onStart() { super.onStart(); Log.d(TAG, "MainActivity onStart") }
    override fun onResume() { super.onResume(); Log.d(TAG, "MainActivity onResume") }
    override fun onPause() { super.onPause(); Log.d(TAG, "MainActivity onPause") }
    override fun onStop() { super.onStop(); Log.d(TAG, "MainActivity onStop") }
    override fun onRestart() { super.onRestart(); Log.d(TAG, "MainActivity onRestart") }
    override fun onDestroy() { super.onDestroy(); Log.d(TAG, "MainActivity onDestroy") }
}
