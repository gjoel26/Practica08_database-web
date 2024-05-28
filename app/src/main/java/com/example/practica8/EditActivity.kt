package com.example.practica8

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditActivity : AppCompatActivity() {
    var position: Int = 0
    lateinit var txtNombre: EditText
    lateinit var txtTelefono: EditText
    lateinit var contactos: MutableList<Contacto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar)
        val txtTitulo = findViewById<TextView>(R.id.txtTitulo)
        txtTitulo.text = "Modificar Contacto"
        position = intent.getIntExtra("position", -1)
        Log.e("Contacto", "Se recibió un $position")
        txtNombre = findViewById(R.id.txtNombre)
        txtTelefono = findViewById(R.id.txtTelefono)

        // Obteniendo la lista de contactos de ProvisionalDatos
        contactos = ProvisionalDatos.listaContactos

        val contacto = contactos[position]
        txtNombre.setText(contacto.nombre)
        txtTelefono.setText(contacto.telefono)

        // Mostrar el botón "Eliminar" solo si se está editando un contacto existente
        findViewById<Button>(R.id.btnEliminar).visibility = View.VISIBLE
        // Asociar el método eliminarContacto al botón de eliminar
        findViewById<Button>(R.id.btnEliminar).setOnClickListener {
            eliminarContacto()
        }
    }

    fun guardar(v: View) {
        val nombre = txtNombre.text.toString()
        val telefono = txtTelefono.text.toString()
        val retrofit = RetrofitApp.getRetrofit()
        val servicio = retrofit.create(IContacto::class.java)
        val contacto = Contacto(contactos[position].idcontacto, nombre, telefono)

        val peticion: Call<Boolean> = servicio.modificar(contacto)

        peticion.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    val res = response.body()
                    if (res != null && res) {
                        // Modificar el contacto en la lista local
                        contactos[position] = contacto
                        Toast.makeText(
                            this@EditActivity,
                            "Contacto modificado correctamente",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@EditActivity,
                            "Error al modificar el contacto",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@EditActivity,
                        "Error en la respuesta del servidor",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.e("ERROR", t.message.toString())
                Toast.makeText(
                    applicationContext,
                    "Error al conectar con el servidor",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun eliminarContacto() {
        val retrofit = RetrofitApp.getRetrofit()
        val servicio = retrofit.create(IContacto::class.java)

        // Obtener el nombre y el número de teléfono del contacto que se desea eliminar
        val contacto = contactos[position]

        // Hacer la solicitud al servidor pasando los datos del contacto
        val peticion: Call<Boolean> = servicio.eliminar(contacto)

        peticion.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    val res = response.body()
                    if (res != null && res) {
                        // Eliminar el contacto de la lista local
                        contactos.removeAt(position)
                        Toast.makeText(
                            this@EditActivity,
                            "Contacto eliminado correctamente",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@EditActivity,
                            "Error al eliminar el contacto",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@EditActivity,
                        "Error en la respuesta del servidor",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.e("ERROR", t.message.toString())
                Toast.makeText(
                    applicationContext,
                    "Error al conectar con el servidor",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}