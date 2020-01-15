package com.agm91.grinapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.agm91.grinapp.BR
import com.agm91.grinapp.R
import com.agm91.grinapp.Utils
import com.agm91.grinapp.databinding.ItemShowDevicesBinding
import com.agm91.grinapp.model.BluetoothDeviceResponse

class ShowDevicesAdapter : RecyclerView.Adapter<ShowDevicesAdapter.ViewHolder>() {
    private var data = listOf<BluetoothDeviceResponse>()

    fun setDevices(devices: List<BluetoothDeviceResponse>) {
        this.data = devices
        notifyDataSetChanged()
    }

    fun resort(sort: Sort) {
        this.data = when (sort) {
            Sort.NAME -> this.data.sortedBy { device -> device.name }
            Sort.STRENGTH -> this.data.sortedBy { device -> device.strength }
            Sort.ADDRESS -> this.data.sortedBy { device -> device.address }
            else ->
                this.data.sortedBy { device ->
                    device.date?.let { date -> Utils.stringToDate(date) }
                }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemShowDevicesBinding>(
                layoutInflater,
                R.layout.item_show_devices,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class ViewHolder(private val binding: ItemShowDevicesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(device: BluetoothDeviceResponse) {
            with(binding) {
                setVariable(BR.device, device)
                executePendingBindings()
            }
        }
    }

    enum class Sort {
        NAME, STRENGTH, ADDRESS, DATE
    }
}