package com.agm91.grinapp

import androidx.lifecycle.Observer
import com.agm91.grinapp.data.repository.DevicesRepository
import com.agm91.grinapp.model.ApiResponse
import com.agm91.grinapp.model.BluetoothDeviceResponse
import com.agm91.grinapp.model.dagger.DaggerApplicationComponent
import io.mockk.MockKAnnotations
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    @Mock
    private lateinit var repository: DevicesRepository

    @Before
    fun setUp() {
        val component = DaggerApplicationComponent.builder().build()
        component.into(this)
        MockKAnnotations.init(this)
        repository = Mockito.mock(DevicesRepository::class.java)
    }

    @Test
    fun fetchData() {
        //given
        val returnedItem = createDummyApiResponse()
        val mockedObserver = createDevicesObserver()
        repository.getDevices().observeForever(mockedObserver)
        //when
        val data = repository.getDevices()
        data.value =
        //than
        verify(mockedObserver.onChanged(listOf(Post(returnedItem.id, returnedItem.title, returnedItem.url)))
    }

    @Test
    fun addDevice() {

    }

    private fun createDevicesObserver(): Observer<ApiResponse<List<BluetoothDeviceResponse>>> =
        spyk(Observer { })

    private fun createDummyDevice(): BluetoothDeviceResponse {
        return BluetoothDeviceResponse(
            name = "Alejandro",
            address = "00:11:22:33",
            date = "2018-09-24T04:42:49.705Z",
            id = "1",
            strength = "-3dB"
        )
    }

    private fun createDummyDevices(): List<BluetoothDeviceResponse> {
        val devices = mutableListOf<BluetoothDeviceResponse>()
        for (x in 0..10) {
            devices.add(createDummyDevice())
        }
        return devices
    }

    private fun createDummyApiResponse(error: Boolean = false): ApiResponse<List<BluetoothDeviceResponse>> {
        if (error)
            return ApiResponse(error = Throwable("Oops, error..."))
        return ApiResponse<List<BluetoothDeviceResponse>>(data = createDummyDevices())
    }
}
