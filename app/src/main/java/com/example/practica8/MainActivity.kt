package com.example.practica8

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var rcv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        rcv = findViewById(R.id.rvContactos)
    }

    override fun onResume() {
        super.onResume()
        val retrofit = RetrofitApp.getRetrofit()
        val servicio = retrofit.create(IContacto::class.java)
        val peticion: Call<List<Contacto>> = servicio.getContactos()

        peticion.enqueue(object : Callback<List<Contacto>> {
            override fun onResponse(call: Call<List<Contacto>>, response: Response<List<Contacto>>) {
                val contactos = response.body() ?: emptyList()
                Log.v("Respuesta", "Número de registros ${contactos.size}")

                // Configurar el RecyclerView aquí
                rcv.adapter = Adaptador(this@MainActivity, contactos)
                rcv.layoutManager = LinearLayoutManager(this@MainActivity)

                // Almacena los contactos en ProvisionalDatos
                ProvisionalDatos.listaContactos.clear()
                ProvisionalDatos.listaContactos.addAll(contactos)
                Log.d("CONTACTOS", "$contactos")
            }

            override fun onFailure(call: Call<List<Contacto>>, t: Throwable) {
                Log.e("ERROR", t.message.toString())
            }
        })
    }

    fun btnAgregar(v: View) {
        val intent = Intent(this, AgregarActivity::class.java)
        startActivity(intent)
    }

    fun clickItem(position: Int) {
        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra("position", position)
        startActivity(intent)
    }

}

