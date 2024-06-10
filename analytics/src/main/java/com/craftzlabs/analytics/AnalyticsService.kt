package com.craftzlabs.analytics

import android.content.Context

interface AnalyticsService {
    fun init(applicationContext: Context)
    fun trackEvent(eventName: String, params: Map<String, Any>? = null)
    fun setUserProperty(propertyName: String, propertyValue: String)
    fun resetUserProperties()
    fun blackListedEvents(): List<String> = emptyList()
}

fun AnalyticsService.shouldTrackEvent(eventName: String): Boolean {
    return !blackListedEvents().contains(eventName)
}