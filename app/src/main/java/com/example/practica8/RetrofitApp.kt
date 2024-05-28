package com.example.practica8

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitApp {
    companion object {
        fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("http://192.168.1.73:4567")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}
