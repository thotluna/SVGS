package ve.com.teeac.svgs.authentication.domain.use_case

import android.app.Activity
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ve.com.teeac.svgs.authentication.data.models.User
import ve.com.teeac.svgs.authentication.domain.repositories.OAuthRepository

@ExperimentalCoroutinesApi
class SignInTwitterUseCaseTest {

    @MockK(relaxed = true)
    lateinit var auth: OAuthRepository

    @MockK(relaxed = true)
    lateinit var activity: Activity

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun signIn() = runTest {

        val expected = User(
            displayName = "displayName",
            email = "email",
            token = "token"
        )

        coEvery { auth.signIn(any()) } returns expected

        val userInfo = SignInTwitterUseCase(auth)(activity)

        assertEquals(expected, userInfo)

        coVerify { auth.signIn(activity) }
        confirmVerified(auth)
    }
}
