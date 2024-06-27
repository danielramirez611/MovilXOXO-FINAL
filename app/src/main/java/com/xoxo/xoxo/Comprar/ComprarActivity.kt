package com.skincare.xoxo.Comprar

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.skincare.xoxo.Favorito.FavoritoActivity
import com.skincare.xoxo.Inicio.InicioActivity
import com.skincare.xoxo.PerfilActivity
import com.skincare.xoxo.R
import com.xoxo.xoxo.Login.RegisterActivity
import com.xoxo.xoxo.Inicio.Model.ProductosColeccion
import com.xoxo.xoxo.PagoActivity

class ComprarActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var textTotalValue: TextView
    private lateinit var emptyCarritoView: TextView
    private lateinit var listComprar: MutableList<ProductosColeccion>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_comprar)

        val btnCompra = findViewById<Button>(R.id.btnCompra)
        btnCompra.setOnClickListener{
            val intent : Intent = Intent(this, PagoActivity::class.java)
            startActivity(intent)
        }



        auth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("carrito", MODE_PRIVATE)

        textTotalValue = findViewById(R.id.textTotalValue)
        emptyCarritoView = findViewById(R.id.emptyCarritoView)

        val carritoActual = sharedPreferences.getStringSet("productos", setOf()) ?: setOf()

        val producto = intent.getParcelableExtra<ProductosColeccion>("producto")
        if (producto != null) {
            // Si el producto no es nulo, agréguelo al carrito
            val productosList = carritoActual.toMutableSet()
            productosList.add(
                "${producto.nombreColeccion}|${producto.nombreProducto}|${producto.precioProducto}|${producto.tamanoProducto}|${producto.imagenProducto}"
            )
            sharedPreferences.edit().putStringSet("productos", productosList).apply()
        }

        val updatedCarrito = sharedPreferences.getStringSet("productos", setOf()) ?: setOf()

        if (updatedCarrito.isEmpty()) {
            emptyCarritoView.visibility = View.VISIBLE
        } else {
            emptyCarritoView.visibility = View.GONE

            listComprar = updatedCarrito.map {
                val data = it.split("|")
                ProductosColeccion(
                    beneficiosProducto = "",
                    idProducto = 0,
                    imagenProducto = data[4],
                    nombreProducto = data[1],
                    precioProducto = data[2].toDouble(),
                    stockProducto = 0,
                    tamanoProducto = data[3].toInt(),
                    nombreColeccion = data[0],
                    cantidad = 1
                )
            }.toMutableList()

            val recyclerComprar = findViewById<RecyclerView>(R.id.recyclerComprar)
            val adapter = ComprarAdapter(listComprar, sharedPreferences)
            recyclerComprar.adapter = adapter
            recyclerComprar.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            calcularTotal(listComprar)

            adapter.setOnQuantityChangeListener {
                calcularTotal(listComprar)
            }

            adapter.setOnProductRemoveListener { position ->
                listComprar.removeAt(position)
                adapter.notifyItemRemoved(position)
                if (listComprar.isEmpty()) {
                    emptyCarritoView.visibility = View.VISIBLE
                }
                calcularTotal(listComprar)
                updateSharedPreferences(listComprar)
            }
        }

        configureBottomNavigation()


// ComprarActivity
        btnCompra.setOnClickListener {
            val total = textTotalValue.text.toString().substring(2).toDouble()
            val nombresProductos = listComprar.joinToString(", ") { it.nombreProducto } // concatenar nombres de productos

            val intent = Intent(this, PagoActivity::class.java).apply {
                putExtra("total_compra", total)
                putExtra("nombres_productos", nombresProductos)
            }
            startActivity(intent)
        }


    }

    private fun calcularTotal(listComprar: List<ProductosColeccion>) {
        val total = listComprar.sumOf { it.precioProducto * it.cantidad }
        textTotalValue.text = "S/$total"
    }

    private fun aplicarCupon(cupon: String, listComprar: List<ProductosColeccion>) {
        val totalActual = listComprar.sumOf { it.precioProducto * it.cantidad }
        var nuevoTotal = totalActual

        when (cupon.toLowerCase()) {
            "xoxo" -> {
                // Descuento del 10% si el total es mayor o igual a 200 soles
                if (totalActual >= 200) {
                    val descuento = totalActual * 0.1
                    nuevoTotal = totalActual - descuento
                }
            }
            "skincare" -> {
                // Descuento del 20% si el total es mayor o igual a 300 soles
                if (totalActual >= 300) {
                    val descuento = totalActual * 0.2
                    nuevoTotal = totalActual - descuento
                }
            }
            else -> {
                // Cupón inválido, no se aplica descuento
                nuevoTotal = totalActual
            }
        }

        textTotalValue.text = "S/$nuevoTotal"
    }

    private fun configureBottomNavigation() {
        val navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val menu = navigation.menu
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item.itemId == R.id.fiveFragment) {
                item.isChecked = true
                break
            }
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.firstFragment -> {
                loadActivity(InicioActivity::class.java)
                true
            }
            R.id.secondFragment -> {
                loadActivity(FavoritoActivity::class.java)
                true
            }
            R.id.thirdFragment -> {
                loadActivity(RegisterActivity::class.java)
                true
            }
            R.id.fiveFragment -> {
                true // Actualmente en ComprarActivity, por lo tanto, no se requiere acción.
            }
            else -> false
        }
    }

    private fun loadActivity(activityClass: Class<*>) {
        if (activityClass == RegisterActivity::class.java) {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                startActivity(Intent(this, PerfilActivity::class.java))
            } else {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        } else {
            val intent = Intent(this, activityClass)
            startActivity(intent)
        }
    }

    private fun updateSharedPreferences(listComprar: List<ProductosColeccion>) {
        val editor = sharedPreferences.edit()
        val productosStringSet = listComprar.map {
            "${it.nombreColeccion}|${it.nombreProducto}|${it.precioProducto}|${it.tamanoProducto}|${it.imagenProducto}"
        }.toSet()
        editor.putStringSet("productos", productosStringSet)
        editor.apply()
    }
}
