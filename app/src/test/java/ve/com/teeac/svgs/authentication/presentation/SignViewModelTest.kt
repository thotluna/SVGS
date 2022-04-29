package ve.com.teeac.svgs.authentication.presentation

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ve.com.teeac.svgs.authentication.data.models.User
import ve.com.teeac.svgs.authentication.domain.use_case.StateUserUseCase

@ExperimentalCoroutinesApi
@DelicateCoroutinesApi
class SignViewModelTest {

    @MockK
    lateinit var stateUserUseCase: StateUserUseCase

    private lateinit var viewModel: SignViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(Dispatchers.Unconfined)

        every { stateUserUseCase() } returns flow { emit(null) }
            .shareIn(
                CoroutineScope(SupervisorJob() + Dispatchers.Unconfined),
                replay = 1,
                started = SharingStarted.WhileSubscribed()
            )

        viewModel = SignViewModel(stateUserUseCase)
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun inicializate_viewModel() {
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun should_be_return_isLoading_true() {
        assertFalse(viewModel.isLoading.value)
        viewModel.onEvent(SignEvent.OnLoading)
        assertTrue(viewModel.isLoading.value)
    }

    @Test
    fun should_be_return_isLoading_false() {
        assertFalse(viewModel.isLoading.value)
        viewModel.onEvent(SignEvent.OnLoading)
        assertTrue(viewModel.isLoading.value)

        val expected = User("user", "email", "token")

        every { stateUserUseCase() } returns flow { emit(expected) }
            .shareIn(
                CoroutineScope(SupervisorJob() + Dispatchers.Unconfined),
                replay = 1,
                started = SharingStarted.WhileSubscribed()
            )

        viewModel = SignViewModel(stateUserUseCase)

        assertFalse(viewModel.isLoading.value)
    }
}
