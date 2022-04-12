package ve.com.teeac.svgs.authentication.presentation.twitter_button

import android.app.Activity
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import ve.com.teeac.svgs.authentication.domain.use_case.SignInTwitterUseCase

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
class TwitterButtonViewModelTest {

    @MockK
    lateinit var useCase: SignInTwitterUseCase

    @MockK
    lateinit var activity: Activity

    private lateinit var viewModel: TwitterButtonViewModel

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(mainThreadSurrogate)

        viewModel = TwitterButtonViewModel(useCase)
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `signIn with twitter`() = runTest {

        viewModel.signIn(activity)

        coVerify(timeout = 500, exactly = 1) { useCase(activity) }

        confirmVerified(useCase)
    }
}
