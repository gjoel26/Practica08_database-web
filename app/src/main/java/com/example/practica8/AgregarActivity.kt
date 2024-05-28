package com.example.practica8

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AgregarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar)
    }

    fun guardar(v: View) {
        val nombre = findViewById<EditText>(R.id.txtNombre).text.toString()
        val telefono = findViewById<EditText>(R.id.txtTelefono).text.toString()

        val retrofit = RetrofitApp.getRetrofit()
        val servicio = retrofit.create(IContacto::class.java)

        val contacto = Contacto(0, nombre, telefono)

        val peticion: Call<Boolean> = servicio.agregar(contacto)

        peticion.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    val res = response.body()
                    if (res != null && res) {
                        Toast.makeText(
                            applicationContext,
                            "Contacto guardado correctamente",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Error al guardar el contacto",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Error en la respuesta del servidor",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.e("ERROR", "Error al procesar la solicitud", t)
                Toast.makeText(
                    applicationContext,
                    "Error al conectar con el servidor",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
