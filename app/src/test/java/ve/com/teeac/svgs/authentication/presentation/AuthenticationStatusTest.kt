package ve.com.teeac.svgs.authentication.presentation

import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.LooperMode
import org.robolectric.shadows.ShadowLog
import ve.com.teeac.svgs.authentication.data.models.User
import ve.com.teeac.svgs.authentication.domain.use_case.StateUserUseCase
import ve.com.teeac.svgs.authentication.presentation.status_user.AuthenticationStatus
import ve.com.teeac.svgs.authentication.presentation.status_user.StatusUserViewModel
import ve.com.teeac.svgs.authentication.presentation.status_user.authenticationState

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class AuthenticationStatusTest {

    @get:Rule(order = 1)
    val compose = createComposeRule()

    @MockK
    lateinit var useCase: StateUserUseCase

    private lateinit var viewModel: StatusUserViewModel

    private val login = "logined"

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        ShadowLog.stream = System.out
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
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
            val state by authenticationState(viewModel)
            AuthenticationStatus(
                state
            ) {
                Text(login)
            }
        }

        compose.onNodeWithText(login)
            .assertIsDisplayed()
    }

    // Exception because need viewmodel for Form login
    @Test(expected = RuntimeException::class)
    fun `when status is false because not logined`() {

        every { useCase() } returns flow { emit(null) }

        viewModel = StatusUserViewModel(useCase)

        compose.setContent {
            val state by authenticationState(viewModel)
            AuthenticationStatus(
                state
            ) {
                Text(login)
            }
        }
    }
}
