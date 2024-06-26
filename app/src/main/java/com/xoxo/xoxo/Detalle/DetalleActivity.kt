package com.xoxo.xoxo.Detalle

// DetalleActivity.kt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.skincare.xoxo.Comprar.ComprarActivity
import com.skincare.xoxo.Inicio.InicioActivity
import com.skincare.xoxo.R

class DetalleActivity : AppCompatActivity() {

    private val viewModel: DetalleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)

        val arrowIcon = findViewById<ImageButton>(R.id.arrowIcon)
        arrowIcon.setOnClickListener {
            startActivity(Intent(this, InicioActivity::class.java))
        }

        val shopIcon = findViewById<ImageButton>(R.id.shopIcon)
        shopIcon.setOnClickListener {
            startActivity(Intent(this, ComprarActivity::class.java))
        }

        val btnWhat = findViewById<ImageButton>(R.id.btnWhat)
        btnWhat.setOnClickListener {
            viewModel.openWhatsApp("+51933928155")
        }

        val btnRest = findViewById<ImageButton>(R.id.btnRest)
        btnRest.setOnClickListener {
            viewModel.openWebPage("https://xoxoskincarebr.com/")
        }

        val btnAgregar = findViewById<Button>(R.id.btnAgregar)

        val coleccion = intent.getStringExtra("coleccion")
        val producto = intent.getStringExtra("producto")
        val precio = intent.getStringExtra("precio")
        val descuento = intent.getStringExtra("descuento")
        val descripcion = intent.getStringExtra("descripcion")
        val imageUrl = intent.getStringExtra("imageUrl")

        val product = Product(coleccion, producto, precio, descuento, descripcion, imageUrl)
        viewModel.setProduct(product)

        val txtColeccion = findViewById<TextView>(R.id.txtColeccion)
        val txtProducto = findViewById<TextView>(R.id.txtProducto)
        val txtPrecio = findViewById<TextView>(R.id.textprecio)
        val txtDescuento = findViewById<TextView>(R.id.descuento)
        val txtDescripcion = findViewById<TextView>(R.id.textDescripcion)
        val imageView = findViewById<ImageView>(R.id.imageView)

        viewModel.product.observe(this, Observer { product ->
            txtColeccion.text = product.coleccion
            txtProducto.text = product.producto
            txtPrecio.text = "S/${product.precio}"
            txtDescripcion.text = product.descripcion
            txtDescuento.text = "${product.descuento}.ml"

            // Modificar la URL de la imagen para eliminar "static" y construir la URL completa
            val modifiedImageUrl = product.imageUrl?.replace("static", "")
            val fullImageUrl = "http://192.168.0.17:8080$modifiedImageUrl"
            Glide.with(this).load(fullImageUrl).into(imageView)
            imageView.tag = fullImageUrl
        })

        btnAgregar.setOnClickListener {
            viewModel.product.value?.let { product ->
                viewModel.agregarAlCarrito(product)
                Toast.makeText(this, "Producto añadido al carrito", Toast.LENGTH_LONG).show()
            }
        }
    }
}
