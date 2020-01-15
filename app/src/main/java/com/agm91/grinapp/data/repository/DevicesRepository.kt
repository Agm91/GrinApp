package com.agm91.grinapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.agm91.grinapp.data.BluetoothDevicesApi
import com.agm91.grinapp.model.ApiResponse
import com.agm91.grinapp.model.BluetoothDevice
import com.agm91.grinapp.model.BluetoothDeviceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class DevicesRepository
@Inject constructor(private val api: BluetoothDevicesApi) {
    fun addDevice(device: BluetoothDevice): LiveData<ApiResponse<BluetoothDevice>> {
        val data = MutableLiveData<ApiResponse<BluetoothDevice>>()
        api.addDevice(device).enqueue(object : Callback<BluetoothDevice> {
            override fun onFailure(call: Call<BluetoothDevice>, t: Throwable) {
                data.value = ApiResponse(error = t)
            }

            override fun onResponse(
                call: Call<BluetoothDevice>,
                response: Response<BluetoothDevice>
            ) {
                if (response.isSuccessful)
                    data.value = ApiResponse(data = response.body())
                else
                    data.value =
                        ApiResponse(error = Throwable(response.message()))
            }
        })
        return data
    }

    fun getDevices(): LiveData<ApiResponse<List<BluetoothDeviceResponse>>> {
        val data = MutableLiveData<ApiResponse<List<BluetoothDeviceResponse>>>()
        api.getDevices().enqueue(object : Callback<List<BluetoothDeviceResponse>> {
            override fun onFailure(call: Call<List<BluetoothDeviceResponse>>, t: Throwable) {
                data.value = ApiResponse(error = t)
            }

            override fun onResponse(
                call: Call<List<BluetoothDeviceResponse>>,
                response: Response<List<BluetoothDeviceResponse>>
            ) {
                if (response.isSuccessful)
                    data.value = ApiResponse(data = response.body())
                else
                    data.value =
                        ApiResponse(error = Throwable(response.message()))
            }
        })
        return data
    }
}