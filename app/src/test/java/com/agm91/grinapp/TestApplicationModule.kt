package com.agm91.grinapp.model.dagger

import com.agm91.grinapp.data.BluetoothDevicesApi
import com.agm91.grinapp.model.dagger.NetworkModule
import io.mockk.mockk
import retrofit2.Retrofit

class TestApplicationModule : NetworkModule() {
    override fun provideBluetoothDevicesApi(retrofit: Retrofit): BluetoothDevicesApi = mockk()

    override fun provideRetrofit(): Retrofit = mockk()
}