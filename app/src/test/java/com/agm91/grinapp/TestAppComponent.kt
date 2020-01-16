package com.agm91.grinapp

import com.agm91.grinapp.model.dagger.ApplicationComponent
import com.agm91.grinapp.model.dagger.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class)])
interface TestAppComponent : ApplicationComponent {
    fun into(appRepositoryTest: UnitTest)
}