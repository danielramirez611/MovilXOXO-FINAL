package com.xoxo.xoxo.Resgitrar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.joranprojects.pruebaregister.Resgitrar.Model.NUser
import com.skincare.xoxo.R
import com.xoxo.xoxo.Login.RegisterActivity
import com.xoxo.xoxo.Network.ApiClient3
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CrearActivity:  AppCompatActivity() {
    private lateinit var txtNombre: EditText
    private lateinit var txtApellido: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtClave: EditText
    private lateinit var btnRegistrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear)

        txtNombre = findViewById(R.id.txtEmail)
        txtApellido = findViewById(R.id.txtNombre)
        txtEmail = findViewById(R.id.txtApellido)
        txtClave = findViewById(R.id.txtClave)
        btnRegistrar = findViewById(R.id.btnRegistrar)

        btnRegistrar.setOnClickListener {
            val nombre = txtNombre.text.toString().trim()
            val apellido = txtApellido.text.toString().trim()
            val correo = txtEmail.text.toString().trim()
            val password = txtClave.text.toString().trim()

            if (nombre.isNotEmpty() && apellido.isNotEmpty() && correo.isNotEmpty() && password.isNotEmpty()) {
                val newUser = NUser(apellido, correo, nombre, password)
                registerUser(newUser)
            } else {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(newUser: NUser) {
        val call: Call<Void> = ApiClient3.consumirApi3.registerUser(newUser)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CrearActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()

                    // Aquí redirigimos a RegisterActivity después del registro exitoso
                    val intent = Intent(this@CrearActivity, RegisterActivity::class.java).apply {
                        putExtra("correo", newUser.correo) // Enviar el correo registrado
                        putExtra("contrasena", newUser.password) // Enviar la contraseña registrada
                    }
                    startActivity(intent)
                    finish() // Opcional: para finalizar la actividad actual después de redirigir
                } else {
                    Log.e("CrearActivity", "Error en el registro: ${response.code()} ${response.message()}")
                    response.errorBody()?.let { errorBody ->
                        Log.e("CrearActivity", "Cuerpo del error: ${errorBody.string()}")
                    }
                    Toast.makeText(this@CrearActivity, "Error en el registro: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("CrearActivity", "Fallo en la llamada: ${t.message}", t)
                Toast.makeText(this@CrearActivity, "Fallo: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
