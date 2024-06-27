package com.skincare.xoxo

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.skincare.xoxo.Comprar.ComprarActivity
import com.skincare.xoxo.Favorito.FavoritoActivity
import com.skincare.xoxo.Inicio.InicioActivity
import com.skincare.xoxo.databinding.ActivityPerfilBinding
import com.xoxo.xoxo.Login.RegisterActivity
import java.io.ByteArrayOutputStream

class PerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        sharedPref = getSharedPreferences("myPrefs", MODE_PRIVATE)

        // Recibir datos del intent
        val userName = intent.getStringExtra("userName")
        val userEmail = intent.getStringExtra("userEmail")

        // Establecer los datos del usuario en las vistas
        binding.usernameEditText.setText(userName)
        binding.emailEditText.setText(userEmail)

        // Actualizar la UI con la información de FirebaseUser
        updateUI(user)

        // Configurar el listener para el ImageView de perfil
        binding.logoImageView.setOnClickListener {
            pickImage()
        }

        // Configurar el listener para el botón "Cerrar Sesión"
        binding.btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }

        // Configurar el ImageView para que la imagen se muestre redonda
        binding.logoImageView.clipToOutline = true
        binding.logoImageView.scaleType = ImageView.ScaleType.CENTER_CROP
    }

    override fun onResume() {
        super.onResume()
        user = auth.currentUser!! // Actualizar el usuario actual
        updateUI(user) // Actualizar la interfaz de usuario
    }

    private fun updateUI(user: FirebaseUser) {
        val sharedPref = getSharedPreferences("myPrefs", MODE_PRIVATE)
        val userName = sharedPref.getString("userName", user.displayName)
        val userEmail = sharedPref.getString("userEmail", user.email)

        binding.usernameEditText.setText(userName)
        binding.emailEditText.setText(userEmail)

        val encodedImage = sharedPref.getString("encodedImage", null)
        if (encodedImage != null) {
            val imageBytes = Base64.decode(encodedImage, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            binding.logoImageView.setImageBitmap(decodedImage)
        } else {
            Glide.with(this)
                .load(user.photoUrl)
                .into(binding.logoImageView)
        }

        // Configurar el padding para ajustar las barras del sistema
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

        // Marcar manualmente el botón correspondiente al fragmento de colecciones
        val menu = navigation.menu
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item.itemId == R.id.thirdFragment) {
                item.isChecked = true
                break
            }
        }
    }

    private fun pickImage() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickIntent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
            val encodedImage = bitmapToBase64(bitmap)

            // Guardar la imagen codificada en SharedPreferences
            val editor = sharedPref.edit()
            editor.putString("encodedImage", encodedImage)
            editor.apply()

            // Mostrar la imagen seleccionada en ImageView
            binding.logoImageView.setImageBitmap(bitmap)
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun cerrarSesion() {
        auth.signOut()

        // Limpiar preferencias compartidas
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    companion object {
        const val PICK_IMAGE_REQUEST = 1
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
            // Verificar si el usuario está autenticado
            val currentUser = auth.currentUser
            if (currentUser != null) {
                // Si el usuario está autenticado, cargar PerfilActivity en lugar de RegisterActivity
                startActivity(Intent(this, PerfilActivity::class.java))
            } else {
                // Si el usuario no está autenticado, cargar RegisterActivity
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        } else {
            // Cargar otras actividades sin necesidad de verificar la autenticación
            val intent = Intent(this, activityClass)
            startActivity(intent)
        }
    }
}
