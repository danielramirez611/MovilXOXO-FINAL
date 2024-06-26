// ColeccionRepository.kt
package com.xoxo.xoxo.Coleccion.Views


import com.skincare.xoxo.Coleccion.ColeccionResponse
import com.xoxo.xoxo.Network.ApiClient
import retrofit2.Call

class ColeccionRepository {

    private val apiService = ApiClient.consumirApi

    fun getColecciones(): Call<ColeccionResponse> {
        return apiService.getColecciones()
    }
}