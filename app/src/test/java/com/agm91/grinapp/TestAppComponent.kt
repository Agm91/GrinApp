package com.agm91.grinapp.model.dagger

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [(NetworkModule::class)]
)
interface TestAppComponent : ApplicationComponent {
    fun into(appRepositoryTest: UnitTest)
}