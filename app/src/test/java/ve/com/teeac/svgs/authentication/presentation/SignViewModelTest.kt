package ve.com.teeac.svgs.authentication.presentation

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ve.com.teeac.svgs.authentication.domain.use_case.ObserverStatusAuthUseCase
import ve.com.teeac.svgs.authentication.domain.use_case.SignInByEmailAndPasswordUseCase
import ve.com.teeac.svgs.authentication.domain.use_case.SignUpByEmailAndPasswordUseCase

@ExperimentalCoroutinesApi
@DelicateCoroutinesApi
class SignViewModelTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @MockK
    lateinit var signUpUseCase: SignUpByEmailAndPasswordUseCase

    @MockK
    lateinit var signInUseCase: SignInByEmailAndPasswordUseCase

    @MockK
    lateinit var observerStatusAuthUseCase: ObserverStatusAuthUseCase

    private lateinit var viewModel: SignViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(mainThreadSurrogate)

        coEvery { observerStatusAuthUseCase() } coAnswers {
            flow { emit(null) }
        }

        viewModel = SignViewModel(signUpUseCase, signInUseCase, observerStatusAuthUseCase)
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `inicializate viewModel`() {
        assertFalse(viewModel.isLoading.value)
        assertTrue(viewModel.isSingIn.value)
    }

    @Test
    fun `should be return isSignIn false`() {
        assertTrue(viewModel.isSingIn.value)
        viewModel.onEvent(SingEvent.ChangeSing)
        assertFalse(viewModel.isSingIn.value)
    }

    @Test
    fun `should be return isLoading true`() {
        assertFalse(viewModel.isLoading.value)
        viewModel.onEvent(SingEvent.OnLoading)
        assertTrue(viewModel.isLoading.value)
    }

    @Test
    fun `showul be call sign up use case`() = runTest {

        val username = "email"
        val password = "password"

        viewModel.onEvent(SingEvent.ChangeSing)
        assertFalse(viewModel.isSingIn.value)

//        viewModel.onEvent(SingEvent.Sing(username, password))

        viewModel.onSingUp(username, password)

        coVerify(timeout = 500, exactly = 1) { signUpUseCase(username, password) }
        confirmVerified(signUpUseCase)
    }

    @Test
    fun `should be call sign ip use case`() = runTest {

        val username = "email"
        val password = "password"

        assertTrue(viewModel.isSingIn.value)

//        viewModel.onEvent(SingEvent.Sing(username, password))
        viewModel.onSing(username, password)

        coVerify(timeout = 500, exactly = 1) { signInUseCase(username, password) }
        confirmVerified(signInUseCase)
    }
}
