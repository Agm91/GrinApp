package com.agm91.grinapp.model

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.agm91.grinapp.Utils
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat

data class BluetoothDeviceResponse(
    @SerializedName("name")
    override val name: String?,
    @SerializedName("address")
    override val address: String,
    @SerializedName("strength")
    override val strength: String,
    @SerializedName("_id")
    val id: String?,
    @SerializedName("created_at")
    val date: String?
) : BluetoothDeviceInterface {
    companion object {
        @JvmStatic
        @BindingAdapter("stringToDate")
        fun setStringToDate(view: TextView, date: String) {
            view.text = Utils.stringToDate(date).toString()
        }
    }
}