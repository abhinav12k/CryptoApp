package com.craftzlabs.analytics

import android.content.Context

class AnalyticsManager private constructor() : AnalyticsService {

    private var analyticsServices: MutableList<AnalyticsService> = mutableListOf()

    private fun registerAnalyticsService(analyticsService: AnalyticsService) {
        analyticsServices.add(analyticsService)
    }

    override fun init(applicationContext: Context) {
        analyticsServices.forEach { it.init(applicationContext) }
    }

    override fun trackEvent(eventName: String, params: Map<String, Any>?) {
        analyticsServices.filter { it.shouldTrackEvent(eventName) }
            .forEach { it.trackEvent(eventName, params) }
    }

    override fun setUserProperty(propertyName: String, propertyValue: String) {
        analyticsServices.forEach { it.setUserProperty(propertyName, propertyValue) }
    }

    override fun resetUserProperties() {
        analyticsServices.forEach { it.resetUserProperties() }
    }

    companion object Builder {

        private val analyticsManager = AnalyticsManager()

        fun registerAnalyticsService(service: AnalyticsService): Builder {
            analyticsManager.registerAnalyticsService(service)
            return this
        }

        fun registerAnalyticsServices(services: List<AnalyticsService>): Builder {
            services.forEach { analyticsService ->
                analyticsManager.registerAnalyticsService(
                    analyticsService
                )
            }
            return this
        }

        fun build(applicationContext: Context): AnalyticsManager {
            analyticsManager.init(applicationContext)
            return analyticsManager
        }
    }

}