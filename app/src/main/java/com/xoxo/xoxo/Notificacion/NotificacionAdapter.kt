package com.skincare.xoxo.Inicio

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skincare.xoxo.Notificacion.Notificacion
import com.skincare.xoxo.databinding.ItemNotificacionBinding

class NotificacionAdapter(private val items: MutableList<Notificacion>) :
    RecyclerView.Adapter<NotificacionAdapter.Viewholder>() {

    private lateinit var context: Context

    inner class Viewholder(val binding: ItemNotificacionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        context = parent.context
        val binding = ItemNotificacionBinding.inflate(LayoutInflater.from(context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = items[position]
        holder.binding.textNotificacion.text = item.info
        Glide.with(holder.itemView.context)
            .load(item.imagen)
            .into(holder.binding.imageNotificacion)
    }

    override fun getItemCount(): Int = items.size
}
