package com.agm91.grinapp.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.agm91.grinapp.MyApplication
import com.agm91.grinapp.R
import com.agm91.grinapp.data.viewmodel.DevicesViewModel
import com.agm91.grinapp.databinding.ActivityMainBinding
import com.agm91.grinapp.presentation.adapter.ShowDevicesAdapter
import javax.inject.Inject

class ShowDevicesFragment : Fragment() {
    private var binding: ActivityMainBinding? = null
    private val adapter = ShowDevicesAdapter()
    @Inject lateinit var viewModel: DevicesViewModel

    override fun onAttach(context: Context) {
        (context.applicationContext as MyApplication).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionbar = (activity as AppCompatActivity).supportActionBar
        actionbar?.let {
            actionbar.apply {
                title = getString(R.string.saved_devices)
                setDisplayHomeAsUpEnabled(true)
            }
        }
        bind()
        viewModel.getDevices().observe(this, Observer { apiResponse ->
            if (apiResponse.data != null) {
                val list = apiResponse.data
                list?.let { adapter.setDevices(it) }
            } else Toast.makeText(context, getString(R.string.error), Toast.LENGTH_LONG).show()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.activity_main, container, false)
        bind()
        return binding?.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val select = when (item.itemId) {
            R.id.action_sort_name -> ShowDevicesAdapter.Sort.NAME
            R.id.action_sort_strength -> ShowDevicesAdapter.Sort.STRENGTH
            R.id.action_sort_address -> ShowDevicesAdapter.Sort.ADDRESS
            R.id.action_sort_date -> ShowDevicesAdapter.Sort.DATE
            else -> null
        }
        if (select != null) {
            adapter.resort(select)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_show_devices, menu)
    }

    private fun bind() {
        binding?.recycler?.adapter = adapter
        binding?.fab?.hide()
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle?) =
            ShowDevicesFragment().apply {
                arguments = bundle
                setHasOptionsMenu(true)
            }
    }
}
