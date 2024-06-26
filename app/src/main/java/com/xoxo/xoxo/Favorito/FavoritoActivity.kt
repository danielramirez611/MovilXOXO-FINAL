package com.skincare.xoxo.Favorito

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.skincare.xoxo.Comprar.ComprarActivity
import com.skincare.xoxo.Inicio.InicioActivity
import com.skincare.xoxo.PerfilActivity
import com.skincare.xoxo.R
import com.xoxo.xoxo.Login.RegisterActivity
import com.xoxo.xoxo.Favorito.FavoritosManager

class FavoritoActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: FavoritoAdapter
    private lateinit var favoritosManager: FavoritosManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favorito)
        auth = FirebaseAuth.getInstance()

        favoritosManager = FavoritosManager(this)
        val favoritos = favoritosManager.getFavoritos()

        val recyclerFavorito = findViewById<RecyclerView>(R.id.recyclerFavorito)
        adapter = FavoritoAdapter(favoritos) { producto ->
            val intent = Intent(this, ComprarActivity::class.java)
            intent.putExtra("producto", producto)
            startActivity(intent)
        }
        recyclerFavorito.adapter = adapter
        recyclerFavorito.layoutManager = GridLayoutManager(this, 2)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        val navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val menu = navigation.menu
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item.itemId == R.id.secondFragment) {
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
                loadActivity(ComprarActivity::class.java)
                true
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
}
