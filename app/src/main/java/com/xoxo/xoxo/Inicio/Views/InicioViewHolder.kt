package com.xoxo.xoxo.Inicio.Views

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.xoxo.xoxo.Detalle.DetalleActivity
import com.skincare.xoxo.Inicio.InicioActivity
import com.skincare.xoxo.R
import com.skincare.xoxo.databinding.ItemIncioBinding
import com.squareup.picasso.Picasso
import com.xoxo.xoxo.Favorito.FavoritosManager
import com.xoxo.xoxo.Inicio.Model.ProductosColeccion

class InicioViewHolder(private val binding: ItemIncioBinding) : RecyclerView.ViewHolder(binding.root) {

    private var isLiked: Boolean = false
    private var currentProduct: ProductosColeccion? = null
    private val favoritosManager = FavoritosManager(binding.root.context)

    init {
        binding.imgProducto.setOnClickListener {
            val producto = currentProduct
            if (producto != null) {
                val intent = Intent(binding.root.context, DetalleActivity::class.java).apply {
                    putExtra("coleccion", producto.nombreColeccion)
                    putExtra("producto", producto.nombreProducto)
                    putExtra("precio", producto.precioProducto.toString())
                    putExtra("descuento", producto.tamanoProducto.toString())
                    putExtra("stock", producto.stockProducto.toString())
                    putExtra("descripcion", producto.beneficiosProducto)
                    putExtra("imageUrl", producto.imagenProducto)
                }
                binding.root.context.startActivity(intent)
            }
        }

        binding.fabHeart.setOnClickListener {
            isLiked = !isLiked
            updateFabImage()

            val producto = currentProduct
            if (producto != null) {
                if (isLiked) {
                    addFavorito(producto)
                    showToast(binding.root.context, "Añadido a favoritos")
                } else {
                    removeFavorito(producto)
                    showToast(binding.root.context, "Eliminado de favoritos")
                }
            }
        }
    }

    fun bind(producto: ProductosColeccion) {
        currentProduct = producto
        val favoritos = favoritosManager.getFavoritos()
        isLiked = favoritos.any { it.idProducto == producto.idProducto }
        updateFabImage()
        Log.v("productos",producto.imagenProducto)
        Log.v("productos",producto.nombreProducto)

        binding.txtNProducto.text = producto.nombreProducto
        binding.txtBeneficios.text = producto.beneficiosProducto
        binding.txtPrecio.text = producto.precioProducto.toString()
        binding.txtStock.text = producto.stockProducto.toString()
        binding.txtTamano.text = producto.tamanoProducto.toString()
        binding.txtColeccion.text = producto.nombreColeccion

        // Modificar la URL de la imagen para eliminar "static" y construir la URL completa
        val imagenUrl = producto.imagenProducto.replace("static", "")
        val imageUrl = "http://192.168.0.17:8080$imagenUrl"

        // Cargar la imagen utilizando Picasso
        Picasso.get().load(imageUrl)
            .into(binding.imgProducto)
    }

    private fun addFavorito(producto: ProductosColeccion) {
        val favoritos = favoritosManager.getFavoritos().toMutableList()
        favoritos.add(producto)
        favoritosManager.saveFavoritos(favoritos)

        // Notify InicioActivity to update the UI
        (binding.root.context as? InicioActivity)?.updateProductoFavorito(producto, true)
    }

    private fun removeFavorito(producto: ProductosColeccion) {
        val favoritos = favoritosManager.getFavoritos().toMutableList()
        favoritos.removeAll { it.idProducto == producto.idProducto }
        favoritosManager.saveFavoritos(favoritos)

        // Notify InicioActivity to update the UI
        (binding.root.context as? InicioActivity)?.updateProductoFavorito(producto, false)
    }

    private fun updateFabImage() {
        val imageResource = if (isLiked) {
            R.drawable.heart_red
        } else {
            R.drawable.heart
        }
        binding.fabHeart.setImageResource(imageResource)
        if (isLiked) {
            binding.fabHeart.setColorFilter(
                ContextCompat.getColor(binding.root.context, R.color.rojo),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        } else {
            binding.fabHeart.clearColorFilter()
        }
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}