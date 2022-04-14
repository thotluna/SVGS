package ve.com.teeac.svgs.authentication.data.data_source

import android.app.Activity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ve.com.teeac.svgs.authentication.data.models.User

@ExperimentalCoroutinesApi
class AuthenticationOAuthByFirebaseTest {

    @MockK
    lateinit var auth: FirebaseAuth

    @MockK
    lateinit var provider: OAuthProvider

    @MockK
    lateinit var activity: Activity

    @MockK
    private lateinit var taskResult: Task<AuthResult>

    @MockK
    private lateinit var authResult: AuthResult

    @MockK
    private lateinit var userFirebase: FirebaseUser

    @MockK
    private lateinit var taskToken: Task<GetTokenResult>

    @MockK
    private lateinit var tokenResult: GetTokenResult

    private lateinit var authenticationOAuthByFirebase: AuthenticationOAuthByFirebase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")

        authenticationOAuthByFirebase = AuthenticationOAuthByFirebase(auth, provider)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should be return UserInfo for pendingTask`() = runTest {

        every { auth.pendingAuthResult } returns taskResult

        every { taskResult.isSuccessful } returns true
        every { taskResult.isComplete } returns true
        every { taskResult.isCanceled } returns false
        every { taskResult.exception } returns null
        every { taskResult.result } returns authResult

        every { authResult.user } returns userFirebase

        every { userFirebase.uid } returns "123"
        every { userFirebase.displayName } returns "Test"
        every { userFirebase.email } returns "email"
        every { userFirebase.getIdToken(any()) } returns taskToken

        every { taskToken.isSuccessful } returns true
        every { taskToken.isComplete } returns true
        every { taskToken.isCanceled } returns false
        every { taskToken.exception } returns null
        every { taskToken.result } returns tokenResult

        every { tokenResult.token } returns "token"

        val expected = User("Test", "email", "token")

        val result = authenticationOAuthByFirebase.signIn(activity)

        assertEquals(expected, result)
    }

    @Test
    fun `should be return null for pendingTask`() = runTest {

        every { auth.pendingAuthResult } returns taskResult

        every { taskResult.isSuccessful } returns true
        every { taskResult.isComplete } returns true
        every { taskResult.isCanceled } returns false
        every { taskResult.exception } returns null
        every { taskResult.result } returns null

        every { authResult.user } returns null

        val result = authenticationOAuthByFirebase.signIn(activity)

        assertNull(result)
    }

    @Test
    fun `should be return UserInfo for signInForProvider`() = runTest {

        every { auth.pendingAuthResult } returns null
        every { auth.startActivityForSignInWithProvider(activity, provider) } returns taskResult

        every { taskResult.isSuccessful } returns true
        every { taskResult.isComplete } returns true
        every { taskResult.isCanceled } returns false
        every { taskResult.exception } returns null
        every { taskResult.result } returns authResult

        every { authResult.user } returns userFirebase

        every { userFirebase.uid } returns "123"
        every { userFirebase.displayName } returns "Test"
        every { userFirebase.email } returns "email"
        every { userFirebase.getIdToken(any()) } returns taskToken

        every { taskToken.isSuccessful } returns true
        every { taskToken.isComplete } returns true
        every { taskToken.isCanceled } returns false
        every { taskToken.exception } returns null
        every { taskToken.result } returns tokenResult

        every { tokenResult.token } returns "token"

        val expected = User("Test", "email", "token")

        val result = authenticationOAuthByFirebase.signIn(activity)

        assertEquals(expected, result)
    }
}
