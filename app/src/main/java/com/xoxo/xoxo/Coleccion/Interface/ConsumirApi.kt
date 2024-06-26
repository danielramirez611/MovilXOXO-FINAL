package com.xoxo.xoxo.Coleccion.Interface

import com.skincare.xoxo.Coleccion.ColeccionResponse
import retrofit2.Call
import retrofit2.http.GET

interface ConsumirApi {
    @GET("colecciones")
    fun getColecciones(): Call<ColeccionResponse>
}
