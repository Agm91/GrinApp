package com.agm91.grinapp.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.agm91.grinapp.data.repository.DevicesRepository
import com.agm91.grinapp.model.ApiResponse
import com.agm91.grinapp.model.BluetoothDeviceResponse
import javax.inject.Inject

class DevicesViewModel @Inject constructor(private val repository: DevicesRepository) :
    ViewModel() {
    fun getDevices(): LiveData<ApiResponse<List<BluetoothDeviceResponse>>> {
        return repository.getDevices()
    }
}