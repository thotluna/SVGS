package ve.com.teeac.svgs.core.traker_connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
@Suppress("DEPRECATION")
class NetworkStatusTrackerTest {

    private lateinit var networkStatusTracker: NetworkStatusTracker

    @MockK(relaxed = true)
    private lateinit var context: Context

    @MockK
    private lateinit var manager: ConnectivityManager

    @MockK
    private lateinit var networkInfo: NetworkInfo

    @MockK
    private lateinit var network: Network

    @MockK
    private lateinit var networkCapabilities: NetworkCapabilities

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns manager
        networkStatusTracker = NetworkStatusTracker(context)
    }

    /*
    TODO: Trouble running network status. this makes the flow never stop and we have a timeout error
    @Test
    fun `should return true when network is connected`() = runTest {
        //given
        mockkConstructor(NetworkRequest.Builder::class)
        val request = mockk<NetworkRequest>()
        every { anyConstructed<NetworkRequest.Builder>().addCapability(any()).build() } returns request

        //when
        val statusResult = networkStatusTracker.networkStatus.first()

        //then
        assertEquals(NetworkStatus.Available, statusResult)
    }
    */

    @Test
    fun `should return true when the network is available an sdk version less than Q`() {
        // given
        every { manager.activeNetworkInfo } returns networkInfo
        every { networkInfo.isConnectedOrConnecting } returns true

        // when
        val isNetworkAvailable = networkStatusTracker.hasConnection()

        // then
        assertTrue(isNetworkAvailable)
    }

    @Test
    fun `should return false when network is available an sdk version less than Q`() {
        // given
        every { manager.activeNetworkInfo } returns networkInfo
        every { networkInfo.isConnectedOrConnecting } returns false

        // when
        val isNetworkAvailable = networkStatusTracker.hasConnection()

        // then
        assertFalse(isNetworkAvailable)
    }

    @Test
    fun `should return true when network is available wifi an sdk version greater than Q`() {
        // given
        every { manager.activeNetwork } returns network
        every { manager.getNetworkCapabilities(network) } returns networkCapabilities
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns false
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns true

        // when
        val isNetworkAvailable = networkStatusTracker.hasConnection(30)

        // then
        assertTrue(isNetworkAvailable)
    }

    @Test
    fun `should return true when network is available cellular an sdk version greater than Q`() {
        // given
        every { manager.activeNetwork } returns network
        every { manager.getNetworkCapabilities(network) } returns networkCapabilities
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns true
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns false

        // when
        val isNetworkAvailable = networkStatusTracker.hasConnection(30)

        // then
        assertTrue(isNetworkAvailable)
    }

    @Test
    fun `should return false when network is available an sdk version greater than Q`() {
        // given
        every { manager.activeNetwork } returns network
        every { manager.getNetworkCapabilities(network) } returns null

        // when
        val isNetworkAvailable = networkStatusTracker.hasConnection(30)

        // then
        assertFalse(isNetworkAvailable)
    }
}
