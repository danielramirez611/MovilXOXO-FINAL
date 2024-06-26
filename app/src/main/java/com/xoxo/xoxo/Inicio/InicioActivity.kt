package com.skincare.xoxo.Inicio

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import com.skincare.xoxo.Comprar.ComprarActivity
import com.skincare.xoxo.Favorito.FavoritoActivity
import com.skincare.xoxo.Notificacion.NotificacionActivity
import com.skincare.xoxo.PerfilActivity
import com.skincare.xoxo.R
import com.xoxo.xoxo.Login.RegisterActivity
import com.skincare.xoxo.ViewPagerAdapter
import com.skincare.xoxo.databinding.ActivityInicioBinding
import com.xoxo.xoxo.Coleccion.Views.ColeccionAdapter
import com.xoxo.xoxo.Coleccion.Views.ColeccionViewModel
import com.xoxo.xoxo.Inicio.Adapters.InicioAdapter
import com.xoxo.xoxo.Inicio.Model.ProductosColeccion
import com.xoxo.xoxo.Inicio.ViewModel.InicioViewModel

class InicioActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var viewPager: ViewPager2
    private lateinit var dotsIndicator: DotsIndicator
    private lateinit var adapter: ViewPagerAdapter
    private val handler = Handler(Looper.getMainLooper())
    private val slideRunnable = object : Runnable {
        override fun run() {
            viewPager.currentItem = (viewPager.currentItem + 1) % adapter.itemCount
            handler.postDelayed(this, 3000) // 3 segundos
        }
    }

    private lateinit var binding: ActivityInicioBinding
    private val viewModelProducto: InicioViewModel by viewModels()
    private val viewModelColeccion: ColeccionViewModel by viewModels()
    private lateinit var adapterProducto: InicioAdapter
    private lateinit var adapterColeccion: ColeccionAdapter
    private lateinit var usernameTextView: TextView // Declaración de usernameTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // Inicializar FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Retrieve intent extras
        val userEmailFromIntent = intent.getStringExtra("userEmail")
        val userEmail = userEmailFromIntent ?: getUserEmailFromPreferences()
        // Initialize views
        usernameTextView = findViewById(R.id.usernameTextView)

        // Set userEmail to usernameTextView
        if (userEmail != null) {
            val trimmedText = if (userEmail.length > 15) {
                "${userEmail.substring(0, 15)}..."
            } else {
                userEmail
            }
            usernameTextView.text = trimmedText
        } else {
            usernameTextView.text = "" // Clear text if userEmail is null
        }

        // Inicializar ViewPager y DotsIndicator
        viewPager = findViewById(R.id.viewPager)
        dotsIndicator = findViewById(R.id.dotsIndicator)

        // Inicializar Adapter del ViewPager
        val images = listOf(
            R.drawable.carusel1,
            R.drawable.carusel2,
            R.drawable.carusel3
        )
        adapter = ViewPagerAdapter(images)
        viewPager.adapter = adapter

        // Configurar PageTransformer para animación suave
        viewPager.setPageTransformer { page, position ->
            page.apply {
                translationX = -position * width // Slide transition
                alpha = 1 - Math.abs(position) // Fade transition
                scaleX = 1 - Math.abs(position) / 4 // Scale transition
                scaleY = 1 - Math.abs(position) / 4 // Scale transition
            }
        }

        dotsIndicator.setViewPager2(viewPager)

        // Iniciar auto-slide
        handler.postDelayed(slideRunnable, 3000) // 3 segundos

        // Configurar el RecyclerView y su Adapter para colecciones
        adapterColeccion = ColeccionAdapter()
        binding.recyclerColeccion.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerColeccion.adapter = adapterColeccion

        // Observar los cambios en el ViewModel de colecciones
        viewModelColeccion.colecciones.observe(this, Observer { colecciones ->
            adapterColeccion.updateColecciones(colecciones)
        })

        viewModelColeccion.error.observe(this, Observer { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        })

        // Realizar la solicitud API a través del ViewModel de colecciones
        viewModelColeccion.fetchColecciones()

        // Configurar RecyclerView y su adaptador para productos
        adapterProducto = InicioAdapter()
        binding.recyclerInicio.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerInicio.adapter = adapterProducto

        // Observar los cambios en el ViewModel de productos
        viewModelProducto.productos.observe(this, Observer { productos ->
            adapterProducto.updateData(productos)
        })

        // Observar errores en el ViewModel de productos
        viewModelProducto.error.observe(this, Observer { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        })

        // Observar cambios en el campo de búsqueda
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapterProducto.filter(s.toString()) // Filtrar productos según el texto ingresado
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Realizar la solicitud API a través del ViewModel de productos
        viewModelProducto.fetchProductos()

        // Configurar padding para las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar la visibilidad del sistema UI
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        // Configurar el listener para la BottomNavigationView
        val navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        // Configurar el click listener para el botón de notificación
        val btnNoti = findViewById<ImageView>(R.id.btnNoti)
        btnNoti.setOnClickListener {
            startActivity(Intent(this, NotificacionActivity::class.java))
        }
    }
    private fun getUserEmailFromPreferences(): String? {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        return sharedPreferences.getString("userEmail", null)
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
                loadActivity(ComprarActivity::class.java)
                true
            }
            else -> false
        }
    }

    fun updateProductoFavorito(producto: ProductosColeccion, isLiked: Boolean) {
        val index = adapterProducto.getIndex(producto)
        if (index != -1) {
            adapterProducto.updateProductoFavorito(index, isLiked)
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

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(slideRunnable)
    }
}
