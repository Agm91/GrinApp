package com.agm91.grinapp.presentation.activity

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.agm91.grinapp.MyApplication
import com.agm91.grinapp.R
import com.agm91.grinapp.data.repository.DevicesRepository
import com.agm91.grinapp.databinding.ActivityMainBinding
import com.agm91.grinapp.presentation.activity.GenericActivity.Constants.Companion.FRAGMENT
import com.agm91.grinapp.presentation.activity.GenericActivity.Constants.Companion.FRAGMENT_SHOW_DEVICES
import com.agm91.grinapp.presentation.adapter.DevicesAdapter
import javax.inject.Inject

class MainActivity : AppCompatActivity(), DevicesAdapter.OnSaveButton {
    private val REQUEST_ENABLE_BT = 123
    private val REQUEST_PERMISSION_BT = 412

    @Inject
    lateinit var repository: DevicesRepository

    private lateinit var binding: ActivityMainBinding

    private val devicesAdapter = DevicesAdapter(this@MainActivity)

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

                    val rssi =
                        intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, java.lang.Short.MIN_VALUE)

                    val myDevice =
                        device?.address?.let {
                            com.agm91.grinapp.model.BluetoothDevice(
                                device.name,
                                it,
                                rssi.toString() + "dB"
                            )
                        }
                    myDevice?.let { devicesAdapter.addDevice(it) }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private val BluetoothAdapter.isDisabled: Boolean
        get() = !isEnabled

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as MyApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.fab.setOnClickListener {
            startActivity(Intent(this, GenericActivity::class.java).apply {
                putExtra(FRAGMENT, FRAGMENT_SHOW_DEVICES)
            })
        }

        bluetoothAdapter?.takeIf { it.isDisabled }?.apply {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)

        binding.recycler.adapter = devicesAdapter
    }

    private fun scanDevices() {
        if (bluetoothAdapter?.isDiscovering != true) {
            bluetoothAdapter?.startDiscovery()
        }
    }

    override fun onStart() {
        super.onStart()
        when (PermissionChecker.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )) {
            PermissionChecker.PERMISSION_GRANTED -> {
                scanDevices()
            }
            else -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_PERMISSION_BT
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_PERMISSION_BT
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSION_BT -> when (grantResults) {
                intArrayOf(PackageManager.PERMISSION_GRANTED) -> {
                    scanDevices()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK) {
            scanDevices()
        }
    }

    override fun onSaveButtonClick(device: com.agm91.grinapp.model.BluetoothDevice) {
        repository.addDevice(device).observe(this, Observer { apiResponse ->
            val message =
                getString(
                    if (apiResponse.data != null) R.string.upload_successfull
                    else R.string.upload_unsuccessfull
                )
            Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.text_share))
                sendIntent.type = "text/plain"

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
            R.id.action_gihub -> openUrl(getString(R.string.url_github))
            R.id.action_linkedin -> openUrl(getString(R.string.url_linkedin))
        }
        return true
    }

    private fun openUrl(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.activity_main, menu)
        return true
    }
}
