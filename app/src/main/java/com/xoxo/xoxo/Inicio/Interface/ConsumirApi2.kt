package com.xoxo.xoxo.Inicio.Interface

import com.xoxo.xoxo.Inicio.Model.ProductoResponse
import retrofit2.Call
import retrofit2.http.GET

interface ConsumirApi2 {
    @GET("productos")
    fun getProductos(): Call<ProductoResponse>
}
