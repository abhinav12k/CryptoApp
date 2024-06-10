package com.craftzlabs.analytics

import android.content.Context

class MixpanelAnalyticsService : AnalyticsService {
    override fun init(applicationContext: Context) {}

    override fun trackEvent(eventName: String, params: Map<String, Any>?) {
        println("Event Tracked in mixpanel - {$eventName: $params}")
    }

    override fun setUserProperty(propertyName: String, propertyValue: String) {}

    override fun resetUserProperties() {}

    override fun blackListedEvents(): List<String> {
        return listOf("test1")
    }
}