package com.xoxo.xoxo.Inicio.Views


import com.xoxo.xoxo.Inicio.Model.ProductoResponse
import com.xoxo.xoxo.Network.ApiClient
import retrofit2.Call

class InicioRepository {

    private val apiService = ApiClient.consumirApi2

    fun getProductos(): Call<ProductoResponse> {
        return apiService.getProductos()
    }
}
