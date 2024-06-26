package com.xoxo.xoxo.Inicio.Model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ProductosColeccion(
    @SerializedName("beneficiosProducto") //descripcion del producto
    val beneficiosProducto: String?,
    @SerializedName("idProducto") //id del producto
    val idProducto: Int,
    @SerializedName("imagenProducto")//imagen del producto
    var imagenProducto: String,
    @SerializedName("nombreProducto") //nombre
    val nombreProducto: String,
    @SerializedName("precioProducto") //precio
    val precioProducto: Double,
    @SerializedName("stockProducto") //rectangulo rojo stock
    val stockProducto: Int,
    @SerializedName("tamanoProducto") //remplazara el descuento
    val tamanoProducto: Int,
    @SerializedName("nombreColeccion") // Add this field
    val nombreColeccion: String,
    var isFavorito: Boolean = false, // Nuevo campo
    var cantidad: Int = 1 // Nueva propiedad para la cantidad
) : Parcelable {
    fun updateImageUrl() {
        imagenProducto = imagenProducto.removePrefix("static")
    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(beneficiosProducto)
        parcel.writeInt(idProducto)
        parcel.writeString(imagenProducto)
        parcel.writeString(nombreProducto)
        parcel.writeDouble(precioProducto)
        parcel.writeInt(stockProducto)
        parcel.writeInt(tamanoProducto)
        parcel.writeString(nombreColeccion)
        parcel.writeByte(if (isFavorito) 1 else 0)
        parcel.writeInt(cantidad)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductosColeccion> {
        override fun createFromParcel(parcel: Parcel): ProductosColeccion {
            return ProductosColeccion(parcel)
        }

        override fun newArray(size: Int): Array<ProductosColeccion?> {
            return arrayOfNulls(size)
        }
    }
}

data class ProductoResponse(
    @SerializedName("mensaje")
    val mensaje: String,
    @SerializedName("object")
    val productoList: List<ProductosColeccion>
)
