package com.skincare.xoxo.Comprar

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skincare.xoxo.R
import com.xoxo.xoxo.Inicio.Model.ProductosColeccion

class ComprarViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_comprar, parent, false)) {
    private var imgProducto: ImageView = itemView.findViewById(R.id.imgProducto)
    private var txtColeccion: TextView = itemView.findViewById(R.id.textCategoria1)
    private var txtProducto: TextView = itemView.findViewById(R.id.textNProdcuto1)
    private var txtDescuento: TextView = itemView.findViewById(R.id.tamaño)
    private var txtPrecio: TextView = itemView.findViewById(R.id.textprecio1)
    var numero: TextView = itemView.findViewById(R.id.numero)
    private var btnSuma: ImageButton = itemView.findViewById(R.id.btnSuma)
    private var btnResta: ImageButton = itemView.findViewById(R.id.btnResta)
    private var iconoBasura: ImageView = itemView.findViewById(R.id.iconoBasura)

    fun bind(comprar: ProductosColeccion, onDeleteClicked: (position: Int) -> Unit) {
        val modifiedImageUrl = comprar.imagenProducto?.replace("static", "")
        val fullImageUrl = "http://192.168.0.17:8080$modifiedImageUrl"
        Glide.with(itemView.context).load(fullImageUrl).into(imgProducto)
        imgProducto.tag = fullImageUrl
        txtColeccion.text = comprar.nombreColeccion
        txtProducto.text = comprar.nombreProducto
        txtDescuento.text = "${comprar.tamanoProducto} ml"
        txtPrecio.text = "S/${comprar.precioProducto}"
        numero.text = comprar.cantidad.toString()

        iconoBasura.setOnClickListener {
            onDeleteClicked(adapterPosition)
        }
    }

    fun setOnIncrementClickListener(listener: () -> Unit) {
        btnSuma.setOnClickListener {
            listener.invoke()
        }
    }

    fun setOnDecrementClickListener(listener: () -> Unit) {
        btnResta.setOnClickListener {
            listener.invoke()
        }
    }
}