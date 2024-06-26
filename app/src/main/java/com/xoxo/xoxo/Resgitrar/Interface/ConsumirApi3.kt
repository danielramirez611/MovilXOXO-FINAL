package com.joranprojects.pruebaregister

import com.joranprojects.pruebaregister.Resgitrar.Model.NUser
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ConsumirApi3 {
    @POST("sign-up")
    fun registerUser(@Body newUser: NUser): Call<Void>
}
