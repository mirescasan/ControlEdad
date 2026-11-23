package com.example.controledad

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ResultadoActivity : AppCompatActivity() {

    companion object {
        // Claves de los datos que viajan en el Intent. Son públicas (sin private) porque
        // MainActivity las necesita al hacer putExtra; así las dos pantallas usan EXACTAMENTE la misma clave.
        const val EXTRA_NOMBRE = "nombre"
        const val EXTRA_EDAD = "edad"
        private const val TAG = "CicloVida"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)
        Log.d(TAG, "ResultadoActivity onCreate")

        // Ajuste de márgenes del sistema (igual que en MainActivity)
        val raiz = findViewById<android.view.View>(R.id.main)
        val pIzq = raiz.paddingLeft; val pArr = raiz.paddingTop
        val pDer = raiz.paddingRight; val pAba = raiz.paddingBottom
        ViewCompat.setOnApplyWindowInsetsListener(raiz) { v, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(pIzq + barras.left, pArr + barras.top, pDer + barras.right, pAba + barras.bottom)
            insets
        }

        val txtBienvenida = findViewById<TextView>(R.id.txtBienvenida)
        val txtResultado = findViewById<TextView>(R.id.txtResultado)
        val btnCompartir = findViewById<Button>(R.id.btnCompartir)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        // "intent" es el Intent que abrió esta Activity. Leemos los datos con las mismas claves.
        // ?: "" evita nulos; el 0 es el valor por defecto de getIntExtra si no existiera la clave.
        // Como el Intent sobrevive al giro de pantalla, estos datos se recuperan al recrear la Activity.
        val nombre = intent.getStringExtra(EXTRA_NOMBRE) ?: ""
        val edad = intent.getIntExtra(EXTRA_EDAD, 0)

        // getString(id, valor) rellena el hueco %1$s del strings.xml
        txtBienvenida.text = getString(R.string.bienvenida, nombre)

        // when como expresión: devuelve el String del primer caso que se cumpla
        val mensaje = when {
            edad < 18 -> getString(R.string.msg_menor, nombre)
            edad == 18 -> getString(R.string.msg_justo, nombre)
            else -> getString(R.string.msg_mayor, nombre)
        }
        txtResultado.text = mensaje

        btnCompartir.setOnClickListener {
            // ACTION_SEND = "enviar contenido a otra app". type text/plain = es texto simple.
            val envio = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, mensaje)             // el texto que se comparte
            }
            try {
                // createChooser abre el selector "Compartir con…" para elegir app
                startActivity(Intent.createChooser(envio, getString(R.string.selector_compartir)))
            } catch (e: ActivityNotFoundException) {
                // Si no hay ninguna app capaz de recibirlo, avisamos y la app NO se cierra
                Toast.makeText(this, R.string.toast_sin_apps, Toast.LENGTH_SHORT).show()
            }
        }

        // finish() cierra esta Activity y Android vuelve a la anterior (MainActivity)
        btnVolver.setOnClickListener { finish() }
    }

    override fun onStart() { super.onStart(); Log.d(TAG, "ResultadoActivity onStart") }
    override fun onResume() { super.onResume(); Log.d(TAG, "ResultadoActivity onResume") }
    override fun onPause() { super.onPause(); Log.d(TAG, "ResultadoActivity onPause") }
    override fun onStop() { super.onStop(); Log.d(TAG, "ResultadoActivity onStop") }
    override fun onRestart() { super.onRestart(); Log.d(TAG, "ResultadoActivity onRestart") }
    override fun onDestroy() { super.onDestroy(); Log.d(TAG, "ResultadoActivity onDestroy") }
}
