package com.xoxo.xoxo.Favorito

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xoxo.xoxo.Inicio.Model.ProductosColeccion

class FavoritosManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("favoritos_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveFavoritos(favoritos: List<ProductosColeccion>) {
        val json = gson.toJson(favoritos)
        prefs.edit().putString("favoritos", json).apply()
    }

    fun getFavoritos(): List<ProductosColeccion> {
        val json = prefs.getString("favoritos", null)
        if (json != null) {
            val type = object : TypeToken<List<ProductosColeccion>>() {}.type
            return gson.fromJson(json, type)
        }
        return emptyList()
    }
}
