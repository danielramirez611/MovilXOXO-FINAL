package com.xoxo.xoxo.Detalle

// DetalleViewModel.kt

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide

class DetalleViewModel(application: Application) : AndroidViewModel(application) {

    private val _product = MutableLiveData<Product>()
    val product: LiveData<Product> get() = _product

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("carrito", Context.MODE_PRIVATE)

    fun setProduct(product: Product) {
        _product.value = product
    }

    fun openWhatsApp(phoneNumber: String) {
        val message = "Hola, quisiera consultar sobre sus productos"
        val url = "https://wa.me/$phoneNumber/?text=${Uri.encode(message)}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        getApplication<Application>().startActivity(intent)
    }

    fun openWebPage(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        getApplication<Application>().startActivity(intent)
    }

    fun agregarAlCarrito(product: Product) {
        val editor = sharedPreferences.edit()
        val productoString = "${product.coleccion}|${product.producto}|${product.precio}|${product.descuento}|${product.imageUrl}"
        val carritoActual = sharedPreferences.getStringSet("productos", mutableSetOf()) ?: mutableSetOf()
        carritoActual.add(productoString)
        editor.putStringSet("productos", carritoActual)
        editor.apply()
    }
}
