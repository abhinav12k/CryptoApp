package com.example.crypto.utils

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

object GsonHelper {

    private val gson by lazy { Gson() }

    @Throws(JsonSyntaxException::class)
    fun <T> String.parseToClass(clazz: Class<T>): T = gson.fromJson(this, clazz)
}