package com.skincare.xoxo.Inicio

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.skincare.xoxo.Notificacion.Notificacion
import com.skincare.xoxo.R

class NotificacionViewModel(application: Application) : AndroidViewModel(application) {

    private val _notificacion = MutableLiveData<MutableList<Notificacion>>()
    val notificacion: LiveData<MutableList<Notificacion>> = _notificacion

        fun loadNotificaciones() {
            val ref = FirebaseDatabase.getInstance().getReference("Notificacion")
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val lists = mutableListOf<Notificacion>()
                    for (childSnapshot in snapshot.children) {
                        val list = childSnapshot.getValue(Notificacion::class.java)
                        if (list != null) {
                            lists.add(list)
                        }
                    }
                    _notificacion.value = lists
                }

                override fun onCancelled(error: DatabaseError) {
                Toast.makeText(getApplication(), "Sin conexion a internet", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
