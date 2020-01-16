package com.agm91.grinapp.model.dagger

import com.agm91.grinapp.data.BluetoothDevicesApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
open class NetworkModule {
    @Provides
    open fun provideBluetoothDevicesApi(retrofit: Retrofit): BluetoothDevicesApi {
        return retrofit.create(BluetoothDevicesApi::class.java)
    }

    @Provides
    open fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://grin-bluetooth-api.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}