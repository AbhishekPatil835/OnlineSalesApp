package com.android.onlinesales

import android.app.Application
import android.content.Context

class ApplicationClass : Application() {
    companion object {
        private lateinit var instance: ApplicationClass

        fun getInstance(): ApplicationClass {
            return instance
        }

        val context: Context
            get() = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    // Add necessary configurations and initializations if required.
    // ...
}
