package ve.com.teeac.svgs.authentication.presentation.status_user

import androidx.compose.ui.test.junit4.createComposeRule
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog
import ve.com.teeac.svgs.authentication.data.models.User
import ve.com.teeac.svgs.authentication.domain.use_case.StateUserUseCase

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class AuthenticationStateTest {

    @get:Rule(order = 1)
    val compose = createComposeRule()

    @MockK
    lateinit var useCase: StateUserUseCase

    private lateinit var viewModel: StatusUserViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        ShadowLog.stream = System.out
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `when status is true then show login`() {

        val user = User("display", "emal", "token")

        every { useCase() } returns flow { emit(user) }

        viewModel = StatusUserViewModel(useCase)

        compose.setContent {
            val auth = authenticationState(viewModel = viewModel)
            assertTrue(auth.value)
        }
    }

    @Test
    fun `when status is false then show login`() {

        every { useCase() } returns flow { emit(null) }

        viewModel = StatusUserViewModel(useCase)

        compose.setContent {
            val auth = authenticationState(viewModel = viewModel)
            assertFalse(auth.value)
        }
    }
}
