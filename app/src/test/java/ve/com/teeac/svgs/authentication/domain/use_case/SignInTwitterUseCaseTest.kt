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
import ve.com.teeac.svgs.authentication.data.data_source.AuthenticationOAuthByFirebase
import ve.com.teeac.svgs.authentication.data.models.UserInfo

@ExperimentalCoroutinesApi
class SignInTwitterUseCaseTest {

    @MockK(relaxed = true)
    lateinit var auth: AuthenticationOAuthByFirebase

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

        val expected = UserInfo(
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
