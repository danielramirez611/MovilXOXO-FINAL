package com.xoxo.xoxo.Login

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ConsumirApi4 {
    @POST("log-in")
    fun loginUser(@Body login: Login): Call<Void>
}
