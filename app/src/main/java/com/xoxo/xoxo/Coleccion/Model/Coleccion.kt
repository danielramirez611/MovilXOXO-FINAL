package com.skincare.xoxo.Coleccion

import com.google.gson.annotations.SerializedName
import com.xoxo.xoxo.Inicio.Model.ProductosColeccion

data class Coleccion(

    @SerializedName("nombreColeccion")
    val nombreColeccion: String,
    @SerializedName("productosColeccion")
    val productosColeccion: List<ProductosColeccion>
)
data class ColeccionResponse(
    @SerializedName("mensaje")
    val mensaje: String,
    @SerializedName("object")
    val coleccionList: List<ProductosColeccion>
)