package com.agm91.grinapp

import androidx.appcompat.app.AppCompatActivity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.platform.app.InstrumentationRegistry
import com.agm91.grinapp.data.BluetoothDevicesApi
import com.agm91.grinapp.data.repository.DevicesRepository
import com.agm91.grinapp.data.viewmodel.DevicesViewModel
import com.agm91.grinapp.model.ApiResponse
import com.agm91.grinapp.model.BluetoothDevice
import com.agm91.grinapp.model.BluetoothDeviceResponse
import com.agm91.grinapp.model.dagger.NetworkModule
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class UnitTest {
    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var apiMock: BluetoothDevicesApi
    @MockK
    lateinit var repoMock: DevicesRepository
    @MockK
    lateinit var viewModelMock: DevicesViewModel

    @Inject
    lateinit var apiDagger: BluetoothDevicesApi
    @Inject
    lateinit var repoDagger: DevicesRepository
    @Inject
    lateinit var viewModelDagger: DevicesViewModel

    val net = NetworkModule()
    lateinit var api: BluetoothDevicesApi
    lateinit var repo: DevicesRepository
    lateinit var viewModel: DevicesViewModel

    private val devicesLiveData:
            MutableLiveData<ApiResponse<List<BluetoothDeviceResponse>>> = MutableLiveData()

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val app = instrumentation.targetContext.applicationContext as MyApplication

        val component = DaggerTestAppComponent.builder()
            .networkModule(TestApplicationModule())
            .build()
        component.into(this)
        app.appComponent = component

        val retrofit = net.provideRetrofit()
        api = net.provideBluetoothDevicesApi(retrofit)
        repo = DevicesRepository(api)
        viewModel = DevicesViewModel(repo)

        assertNotNull(api)
        assertNotNull(viewModel)
        assertNotNull(repo)
    }

    @Test
    fun `testing dagger injection`() {
        assertNotNull(apiDagger)
        assertNotNull(viewModelDagger)
        assertNotNull(repoDagger)
    }

    @Test
    fun `testing Mockk`() {
        assertNotNull(apiMock)
        assertNotNull(viewModelMock)
        assertNotNull(repoMock)
    }

    @Test
    fun `testing viewModel`() {
        viewModel.getDevices().observeOnce {
            System.out.println("Test: " + it.toString())
            Truth.assertThat(it.data != null || it.error != null)
        }
        Truth.assertThat(viewModel.getDevices().hasObservers())
    }

    @Test
    fun `testing upload and retrieve testDevice`() {
        val context = mock(AppCompatActivity::class.java)
        val device = createDummyDevice()
        repo.addDevice(device).observeOnce {
            System.out.println("Test: " + it.toString())
            Truth.assertThat(it.data != null)
            viewModel.getDevices().observeOnce {
                System.out.println("Test: " + it.toString())
                Truth.assertThat(it.data != null)
                it.data?.contains(createDummyDeviceResponse())
            }
        }

        Truth.assertThat(viewModel.getDevices().hasObservers())
        Truth.assertThat(repo.addDevice(device).hasObservers())
    }

    private fun createDevicesObserver(): Observer<ApiResponse<List<BluetoothDeviceResponse>>> =
        spyk(Observer { })

    private fun createDummyDeviceResponse(): BluetoothDeviceResponse {
        return BluetoothDeviceResponse(
            name = "Alejandro's Test",
            address = "00:11:22:33",
            date = "2018-09-24T04:42:49.705Z",
            id = "1",
            strength = "-3dB"
        )
    }

    private fun createDummyDevice(): BluetoothDevice {
        return BluetoothDevice(
            name = "Alejandro's Test",
            address = "00:11:22:33",
            strength = "-3dB"
        )
    }

    private fun createDummyDevices(): List<BluetoothDeviceResponse> {
        val devices = mutableListOf<BluetoothDeviceResponse>()
        for (x in 0..10) {
            devices.add(createDummyDeviceResponse())
        }
        return devices
    }

    private fun createDummyApiResponse(error: Boolean = false): ApiResponse<List<BluetoothDeviceResponse>> {
        if (error)
            return ApiResponse(error = Throwable("Oops, error..."))
        return ApiResponse(data = createDummyDevices())
    }
}
