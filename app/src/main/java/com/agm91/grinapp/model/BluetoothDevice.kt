package com.agm91.grinapp.model

data class BluetoothDevice(
    override val name: String?,
    override val address: String,
    override val strength: String
) : BluetoothDeviceInterface