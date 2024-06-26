package com.skincare.xoxo.Notificacion

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.skincare.xoxo.Inicio.NotificacionAdapter
import com.skincare.xoxo.R
import com.skincare.xoxo.Inicio.InicioActivity
import com.skincare.xoxo.Inicio.NotificacionViewModel
import com.skincare.xoxo.databinding.ActivityNotificacionBinding

class NotificacionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificacionBinding
    private lateinit var viewModel: NotificacionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(NotificacionViewModel::class.java)

        initNotificaciones()

        val arrowIcon = findViewById<ImageView>(R.id.arrowIcon)
        // Configura el OnClickListener para el arrowIcon
        arrowIcon.setOnClickListener {
            startActivity(Intent(this, InicioActivity::class.java))
        }
    }

    private fun initNotificaciones() {
        viewModel.notificacion.observe(this, Observer { notificaciones ->
            binding.recyclerNotificacion.layoutManager =
                LinearLayoutManager(this@NotificacionActivity, LinearLayoutManager.VERTICAL, false)
            binding.recyclerNotificacion.adapter = NotificacionAdapter(notificaciones)
        })
        viewModel.loadNotificaciones()
    }
}