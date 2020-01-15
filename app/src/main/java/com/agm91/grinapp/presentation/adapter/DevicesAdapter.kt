package com.agm91.grinapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.agm91.grinapp.BR
import com.agm91.grinapp.R
import com.agm91.grinapp.databinding.ItemDevicesBinding
import com.agm91.grinapp.model.BluetoothDevice

class DevicesAdapter(val listener: OnSaveButton) :
    RecyclerView.Adapter<DevicesAdapter.ViewHolder>() {
    private var data = mutableMapOf<String, BluetoothDevice>()

    fun addDevice(device: BluetoothDevice) {
        data[device.address] = device
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemDevicesBinding>(
                layoutInflater,
                R.layout.item_devices,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = data.values.toList()
        holder.bind(list[position])
    }

    inner class ViewHolder(private val binding: ItemDevicesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(device: BluetoothDevice) {
            with(binding) {
                setVariable(BR.device, device)
                binding.saveButton.setOnClickListener {
                    listener.onSaveButtonClick(device)
                }
                executePendingBindings()
            }
        }
    }

    interface OnSaveButton {
        fun onSaveButtonClick(device: BluetoothDevice)
    }
}