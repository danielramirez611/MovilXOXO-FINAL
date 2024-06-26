package com.xoxo.xoxo.Inicio.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xoxo.xoxo.Inicio.Model.ProductoResponse
import com.xoxo.xoxo.Inicio.Model.ProductosColeccion
import com.xoxo.xoxo.Inicio.Views.InicioRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class InicioViewModel : ViewModel() {

    private val repository = InicioRepository()
    private val _productos = MutableLiveData<List<ProductosColeccion>>()
    val productos: LiveData<List<ProductosColeccion>> = _productos

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchProductos() {
        val call = repository.getProductos()
        call.enqueue(object : Callback<ProductoResponse> {
            override fun onResponse(call: Call<ProductoResponse>, response: Response<ProductoResponse>) {
                if (response.isSuccessful) {
                    val productoResponse = response.body()
                    _productos.value = productoResponse?.productoList ?: emptyList()
                } else {
                    _error.value = "Error al obtener productos"
                }
            }

            override fun onFailure(call: Call<ProductoResponse>, t: Throwable) {
                _error.value = if (t is SocketTimeoutException) {
                    "Tiempo de espera agotado al intentar conectar al servidor"
                } else {
                    "Error de red: ${t.message}"
                }
            }
        })
    }
}
