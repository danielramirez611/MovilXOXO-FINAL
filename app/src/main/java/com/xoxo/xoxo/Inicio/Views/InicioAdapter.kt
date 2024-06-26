package com.xoxo.xoxo.Inicio.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skincare.xoxo.databinding.ItemIncioBinding
import com.xoxo.xoxo.Inicio.Model.ProductosColeccion
import com.xoxo.xoxo.Inicio.Views.InicioViewHolder

class InicioAdapter : RecyclerView.Adapter<InicioViewHolder>() {
    private var productoList: List<ProductosColeccion> = emptyList()
    private var originalList: List<ProductosColeccion> = emptyList() // Keep original list for filtering

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InicioViewHolder {
        val binding = ItemIncioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InicioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InicioViewHolder, position: Int) {
        val producto = productoList[position]
        producto.updateImageUrl() // Actualizar la URL de la imagen
        holder.bind(producto)
    }

    override fun getItemCount(): Int {
        return productoList.size
    }

    fun updateData(data: List<ProductosColeccion>) {
        productoList = data
        originalList = data
        notifyDataSetChanged()
    }

    fun getIndex(producto: ProductosColeccion): Int {
        return productoList.indexOfFirst { it.idProducto == producto.idProducto }
    }

    fun updateProductoFavorito(index: Int, isLiked: Boolean) {
        if (index in productoList.indices) {
            val producto = productoList[index]
            producto.isFavorito = isLiked
            notifyItemChanged(index)
        }
    }

    // Filtering methods if you have any search functionality
    fun filter(query: String) {
        productoList = if (query.isEmpty()) {
            originalList
        } else {
            originalList.filter { it.nombreProducto.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }
}
