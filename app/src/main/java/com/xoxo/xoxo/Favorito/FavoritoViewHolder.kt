package com.skincare.xoxo.Favorito

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.xoxo.xoxo.Detalle.DetalleActivity
import com.skincare.xoxo.Inicio.InicioActivity
import com.skincare.xoxo.R
import com.squareup.picasso.Picasso
import com.xoxo.xoxo.Favorito.FavoritosManager
import com.xoxo.xoxo.Inicio.Model.ProductosColeccion

class FavoritoViewHolder(inflater: LayoutInflater, parent: ViewGroup, private val onComprarClick: (ProductosColeccion) -> Unit) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_favorito, parent, false)) {

    private var txtColeccion: TextView? = null
    private var txtProducto: TextView? = null
    private var txtPrecio: TextView? = null
    private var txtDescuento: TextView? = null
    private var txtBeneficios: TextView? = null
    private var txtStock: TextView? = null
    private var image: ImageView? = null
    private var fabHeart: ImageView? = null

    init {
        itemView.findViewById<ImageView>(R.id.imgProducto).setOnClickListener {
            val producto = currentProduct
            if (producto != null) {
                val intent = Intent(itemView.context, DetalleActivity::class.java).apply {
                    putExtra("coleccion", producto.nombreColeccion)
                    putExtra("producto", producto.nombreProducto)
                    putExtra("precio", producto.precioProducto.toString())
                    putExtra("descuento", producto.tamanoProducto.toString())
                    putExtra("stock", producto.stockProducto.toString())
                    putExtra("descripcion", producto.beneficiosProducto)
                    putExtra("imageUrl", producto.imagenProducto)
                }
                itemView.context.startActivity(intent)
            }
        }
        txtColeccion = itemView.findViewById(R.id.txtColeccion)
        txtProducto = itemView.findViewById(R.id.txtNProducto)
        txtPrecio = itemView.findViewById(R.id.txtPrecio)
        txtDescuento = itemView.findViewById(R.id.txtTamano)
        txtBeneficios = itemView.findViewById(R.id.txtBeneficios)
        txtStock = itemView.findViewById(R.id.txtStock)
        image = itemView.findViewById(R.id.imgProducto)
        fabHeart = itemView.findViewById(R.id.fabHeart)

        fabHeart?.setOnClickListener {
            isLiked = !isLiked
            updateFabImage()

            val producto = currentProduct
            if (producto != null) {
                if (isLiked) {
                    addFavorito(producto)
                } else {
                    removeFavorito(producto)
                }
            }
        }


    }

    private var isLiked: Boolean = false
    private var currentProduct: ProductosColeccion? = null
    private val favoritosManager = FavoritosManager(itemView.context)

    fun bind(producto: ProductosColeccion) {
        currentProduct = producto
        val favoritos = favoritosManager.getFavoritos()
        isLiked = favoritos.any { it.nombreProducto == producto.nombreProducto }
        updateFabImage()

        txtColeccion?.text = producto.nombreColeccion
        txtProducto?.text = producto.nombreProducto
        txtPrecio?.text = producto.precioProducto.toString()
        txtDescuento?.text = producto.tamanoProducto.toString()
        txtBeneficios?.text = producto.beneficiosProducto
        txtStock?.text = producto.stockProducto.toString()

        if (image != null) {
            // Modificar la URL de la imagen para eliminar "static" y construir la URL completa
            val imagenUrl = producto.imagenProducto.replace("static", "")
            val imageUrl = "http://192.168.0.17:8080$imagenUrl"
            // Cargar la imagen utilizando Picasso
            Picasso.get().load(imageUrl).into(image)
        }
    }

    private fun addFavorito(producto: ProductosColeccion) {
        val favoritos = favoritosManager.getFavoritos().toMutableList()
        favoritos.add(producto)
        favoritosManager.saveFavoritos(favoritos)
    }

    private fun removeFavorito(producto: ProductosColeccion) {
        val favoritos = favoritosManager.getFavoritos().toMutableList()
        favoritos.removeAll { it.nombreProducto == producto.nombreProducto }
        favoritosManager.saveFavoritos(favoritos)

        // Notify InicioAdapter to update the UI
        (itemView.context as? InicioActivity)?.updateProductoFavorito(producto, false)
    }

    private fun updateFabImage() {
        val imageResource = if (isLiked) {
            R.drawable.heart_red // Corazón en rojo si está marcado como favorito
        } else {
            R.drawable.heart // Corazón en negro si no está marcado como favorito
        }
        fabHeart?.setImageResource(imageResource)
        // Cambiar el color del corazón según esté marcado como favorito o no
        if (isLiked) {
            fabHeart?.setColorFilter(
                ContextCompat.getColor(itemView.context, R.color.rojo),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        } else {
            fabHeart?.setColorFilter(
                ContextCompat.getColor(itemView.context, R.color.black),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }
}