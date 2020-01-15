package com.agm91.grinapp.data

import com.agm91.grinapp.model.BluetoothDevice
import com.agm91.grinapp.model.BluetoothDeviceResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BluetoothDevicesApi {
    @GET("devices")
    fun getDevices(): Call<List<BluetoothDeviceResponse>>

    @POST("add")
    fun addDevice(@Body bluetoothDevice: BluetoothDevice): Call<BluetoothDevice>
}