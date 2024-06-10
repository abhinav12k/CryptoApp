package com.example.crypto.utils

import android.content.Context
import com.example.crypto.CryptoApp

fun Context.trackEvent(eventName: String, eventProps: Map<String, String>? = null) {
    (applicationContext as CryptoApp).analyticsService.trackEvent(eventName, eventProps)
}