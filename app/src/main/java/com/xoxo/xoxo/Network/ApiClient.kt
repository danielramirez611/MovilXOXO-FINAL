package com.xoxo.xoxo.Network
import com.xoxo.xoxo.Coleccion.Interface.ConsumirApi
import com.xoxo.xoxo.Inicio.Interface.ConsumirApi2
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://192.168.0.17:8080/api/v1/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val consumirApi: ConsumirApi = retrofit.create(ConsumirApi::class.java)
    val consumirApi2: ConsumirApi2 =retrofit.create(ConsumirApi2::class.java)

}
