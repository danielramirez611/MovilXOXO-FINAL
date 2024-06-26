package com.xoxo.xoxo.Network
import com.joranprojects.pruebaregister.ConsumirApi3
import com.xoxo.xoxo.Login.ConsumirApi4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient3 {
        private const val BASE_URL2 = "http://192.168.0.17:8080/auth/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL2)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val consumirApi3: ConsumirApi3 = retrofit.create(ConsumirApi3::class.java)
    val consumirApi4: ConsumirApi4 = retrofit.create(ConsumirApi4::class.java)

}
