package com.xoxo.xoxo.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.skincare.xoxo.Inicio.InicioActivity
import com.skincare.xoxo.PerfilActivity
import com.skincare.xoxo.R
import com.skincare.xoxo.databinding.RegisterActivityBinding
import com.xoxo.xoxo.Network.ApiClient3
import com.xoxo.xoxo.Resgitrar.CrearActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: RegisterActivityBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    private lateinit var txtEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var btnIniciar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance()

        // Set up sign-in with Google
        setUpSignInProcess()

        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)
        btnIniciar = findViewById(R.id.btnIniciar)

        // Obtener datos enviados desde CrearActivity
        val correo = intent.getStringExtra("correo")
        val contrasena = intent.getStringExtra("contrasena")

        // Llenar los campos si los datos no son nulos
        correo?.let { txtEmail.setText(it) }
        contrasena?.let { txtPassword.setText(it) }

        btnIniciar.setOnClickListener {
            val email = txtEmail.text.toString().trim()
            val password = txtPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val login = Login(email, password)
                loginUser(login)
            } else {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            }
        }

        val btnCrear = findViewById<Button>(R.id.btnCrear)
        btnCrear.setOnClickListener {
            startActivity(Intent(this, CrearActivity::class.java))
            finish() // Opcional: finalizar RegisterActivity si se regresa a CrearActivity
        }
    }

    private fun setUpSignInProcess() {
        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            ).build()

        binding.btnPerfil.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                startIntentSenderForResult(
                    result.pendingIntent.intentSender, 2, null, 0, 0, 0, null
                )
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(data)
                val idToken = credential.googleIdToken
                when {
                    idToken != null -> {
                        firebaseAuthWithGoogle(idToken)
                    }
                    else -> {
                        // Handle error
                    }
                }
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        val profilePicUrl = it.photoUrl?.toString()
                        val userEmail = it.email // Obtener el correo electrónico del usuario
                        if (userEmail != null) {
                            saveUserEmail(userEmail)
                        } // Guardar el correo electrónico en SharedPreferences
                        val intent = Intent(this, InicioActivity::class.java).apply {
                            putExtra("profilePicUrl", profilePicUrl)
                            putExtra("userEmail", userEmail) // Pasar el correo electrónico del usuario
                        }
                        startActivity(intent)
                        finish()
                    }
                } else {
                    // Handle failure
                }
            }
    }

    private fun loginUser(login: Login) {
        val call: Call<Void> = ApiClient3.consumirApi4.loginUser(login)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

                    // Retrieve user details
                    val userEmail = txtEmail.text.toString().trim()
                    saveUserEmail(userEmail) // Guardar el correo electrónico en SharedPreferences

                    // Pass userEmail to PerfilActivity
                    val intent = Intent(this@RegisterActivity, PerfilActivity::class.java).apply {
                        putExtra("userEmail", userEmail)
                    }
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("LoginActivity", "Error en el inicio de sesión: ${response.code()} ${response.message()}")
                    response.errorBody()?.let { errorBody ->
                        Log.e("LoginActivity", "Cuerpo del error: ${errorBody.string()}")
                    }
                    Toast.makeText(this@RegisterActivity, "Error en el inicio de sesión: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("LoginActivity", "Fallo en la llamada: ${t.message}", t)
                Toast.makeText(this@RegisterActivity, "Fallo: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserEmail(userEmail: String) {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userEmail", userEmail)
        editor.apply()
    }

}
