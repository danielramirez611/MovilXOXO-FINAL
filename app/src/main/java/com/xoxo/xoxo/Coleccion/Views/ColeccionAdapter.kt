package com.xoxo.xoxo.Coleccion.Views

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skincare.xoxo.Coleccion.Coleccion
import com.skincare.xoxo.databinding.ItemColeccionBinding
import com.xoxo.xoxo.Inicio.Model.ProductosColeccion

class ColeccionAdapter() : RecyclerView.Adapter<ColeccionAdapter.ColeccionViewHolder>() {
    private var coleccionList: List<ProductosColeccion> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColeccionViewHolder {
        val binding = ItemColeccionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ColeccionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ColeccionViewHolder, position: Int) {
        holder.bind(coleccionList[position])
    }

    override fun getItemCount(): Int {
        return coleccionList.size
    }

    fun updateColecciones(colecciones: List<ProductosColeccion>) {
        coleccionList = colecciones
        notifyDataSetChanged()
    }

    class ColeccionViewHolder(private val binding: ItemColeccionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(coleccion: ProductosColeccion) {
            Log.v("colecciones",coleccion.nombreColeccion)
            binding.txtColeccion.text = coleccion.nombreColeccion

        }
    }
}