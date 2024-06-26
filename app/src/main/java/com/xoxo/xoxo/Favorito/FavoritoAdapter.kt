package com.skincare.xoxo.Favorito

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xoxo.xoxo.Inicio.Model.ProductosColeccion

class FavoritoAdapter(
    private val listFavorito: List<ProductosColeccion>,
    private val onComprarClick: (ProductosColeccion) -> Unit
) : RecyclerView.Adapter<FavoritoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FavoritoViewHolder(inflater, parent, onComprarClick)
    }

    override fun onBindViewHolder(holder: FavoritoViewHolder, position: Int) {
        val favorito = listFavorito[position]
        holder.bind(favorito)
    }

    override fun getItemCount(): Int = listFavorito.size
}
