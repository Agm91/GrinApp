package com.agm91.grinapp

import android.app.Application
import com.agm91.grinapp.model.dagger.DaggerApplicationComponent

class MyApplication : Application() {
    val appComponent = DaggerApplicationComponent.create()
}