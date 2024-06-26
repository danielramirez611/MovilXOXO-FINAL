package com.xoxo.xoxo.Coleccion.Views
// ColeccionViewModel.kt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skincare.xoxo.Coleccion.Coleccion
import com.skincare.xoxo.Coleccion.ColeccionResponse
import com.xoxo.xoxo.Inicio.Model.ProductosColeccion

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class ColeccionViewModel : ViewModel() {

    private val repository = ColeccionRepository()
    private val _colecciones = MutableLiveData<List<ProductosColeccion>>()
    val colecciones: LiveData<List<ProductosColeccion>> = _colecciones

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchColecciones() {
        val call = repository.getColecciones()
        call.enqueue(object : Callback<ColeccionResponse> {
            override fun onResponse(call: Call<ColeccionResponse>, response: Response<ColeccionResponse>) {
                if (response.isSuccessful) {
                    val coleccionResponse = response.body()
                    _colecciones.value = coleccionResponse?.coleccionList ?: emptyList()
                } else {
                    _error.value = "Error al obtener colecciones"
                }
            }

            override fun onFailure(call: Call<ColeccionResponse>, t: Throwable) {
                _error.value = if (t is SocketTimeoutException) {
                    "Tiempo de espera agotado al intentar conectar al servidor"
                } else {
                    "Error de red: ${t.message}"
                }
            }
        })
    }
}