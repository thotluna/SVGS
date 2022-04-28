package ve.com.teeac.svgs.authentication.presentation

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ve.com.teeac.svgs.authentication.domain.use_case.StateUserUseCase

@ExperimentalCoroutinesApi
@DelicateCoroutinesApi
class SignViewModelTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @MockK
    lateinit var stateUserUseCase: StateUserUseCase

    private lateinit var viewModel: SignViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(mainThreadSurrogate)

        coEvery { stateUserUseCase() } coAnswers {
            flow { emit(null) }
        }

        viewModel = SignViewModel(stateUserUseCase)
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
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
}
