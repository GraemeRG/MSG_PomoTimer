package com.mobilesummergames.pomotimer1

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@App.applicationContext)
            modules (
                module {
                    viewModel {
                        TimerViewModel()
                    }
                }
            )
        }
    }
}