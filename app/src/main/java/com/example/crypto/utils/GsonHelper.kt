package com.example.crypto.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException


object GsonHelper {

    val gson: Gson by lazy {
        GsonBuilder()
            .setLenient()
            .create()
    }

    @Throws(JsonSyntaxException::class)
    fun <T> String.parseToClass(clazz: Class<T>): T = gson.fromJson(this, clazz)
}