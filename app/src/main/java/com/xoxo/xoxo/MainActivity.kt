package com.skincare.xoxo

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.skincare.xoxo.Inicio.InicioActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Temporizador para esperar 6 segundos antes de pasar a otra actividad
        object : CountDownTimer(3000, 300) {
            override fun onTick(millisUntilFinished: Long) {
                // Método llamado cada segundo mientras el temporizador esté en marcha
            }

            override fun onFinish() {
                // Método llamado cuando el temporizador llega a cero
                val intent = Intent(this@MainActivity, InicioActivity::class.java)
                startActivity(intent)
                finish() // Finaliza la actividad actual para que no se pueda volver atrás
            }
        }.start() // Inicia el temporizador
    }
}
