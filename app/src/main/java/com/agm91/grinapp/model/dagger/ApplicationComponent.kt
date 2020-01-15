package com.agm91.grinapp.model.dagger

import com.agm91.grinapp.presentation.activity.MainActivity
import com.agm91.grinapp.presentation.fragment.ShowDevicesFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface ApplicationComponent {
    fun inject(fragment: ShowDevicesFragment)
    fun inject(activity: MainActivity)
}