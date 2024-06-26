package com.skincare.xoxo.Comprar

import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.skincare.xoxo.R
import com.squareup.picasso.Picasso
import com.xoxo.xoxo.Inicio.Model.ProductosColeccion

class ComprarAdapter(
    private val listComprar: MutableList<ProductosColeccion>,
    private val sharedPreferences: SharedPreferences
) : RecyclerView.Adapter<ComprarViewHolder>() {

    private var onQuantityChangeListener: (() -> Unit)? = null
    private var onProductRemoveListener: ((Int) -> Unit)? = null

    fun setOnQuantityChangeListener(listener: () -> Unit) {
        onQuantityChangeListener = listener
    }

    fun setOnProductRemoveListener(listener: (Int) -> Unit) {
        onProductRemoveListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComprarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ComprarViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ComprarViewHolder, position: Int) {
        val producto = listComprar[position]
        holder.bind(producto) {
            onProductRemoveListener?.invoke(position)
        }
        holder.setOnIncrementClickListener {
            producto.cantidad++
            holder.numero.text = producto.cantidad.toString()
            onQuantityChangeListener?.invoke()
            updateSharedPreferences()
        }
        holder.setOnDecrementClickListener {
            if (producto.cantidad > 1) {
                producto.cantidad--
                holder.numero.text = producto.cantidad.toString()
                onQuantityChangeListener?.invoke()
                updateSharedPreferences()
            }
        }
    }

    override fun getItemCount(): Int {
        return listComprar.size
    }

    private fun updateSharedPreferences() {
        val editor = sharedPreferences.edit()
        val productosSet = listComprar.map {
            "${it.nombreColeccion}|${it.nombreProducto}|${it.precioProducto}|${it.tamanoProducto}|${it.imagenProducto}|${it.cantidad}"
        }.toSet()
        editor.putStringSet("productos", productosSet)
        editor.apply()
    }
}



