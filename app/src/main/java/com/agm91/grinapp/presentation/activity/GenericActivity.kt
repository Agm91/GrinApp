package com.agm91.grinapp.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.agm91.grinapp.R
import com.agm91.grinapp.databinding.ActivityGenericBinding
import com.agm91.grinapp.presentation.fragment.ShowDevicesFragment

class GenericActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityGenericBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_generic)

        when (intent?.extras?.getString(Constants.FRAGMENT)) {
            Constants.FRAGMENT_SHOW_DEVICES -> {
                val newFragment = ShowDevicesFragment.newInstance(intent.extras)
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, newFragment)
                transaction.commit()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    class Constants {
        companion object {
            val FRAGMENT = "fragment"
            val FRAGMENT_SHOW_DEVICES = "show_devices"
        }
    }
}
