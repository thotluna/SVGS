package ve.com.teeac.svgs.authentication.data.data_source

import android.app.Activity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ve.com.teeac.svgs.authentication.data.models.User

@ExperimentalCoroutinesApi
class OAuthRemoteUserTest {

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

    private lateinit var remoteUser: OAuthRemoteUser

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")

        remoteUser = OAuthFirebase(auth, provider)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should be return user for pendingTask`() = runTest {

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

        val result = remoteUser.signIn(activity)

        assertEquals(expected, result)
    }

    @Test
    fun `should be return user for start activity `() = runTest {

        every { auth.pendingAuthResult } returns taskResult

        every { taskResult.isSuccessful } returns true
        every { taskResult.isComplete } returns true
        every { taskResult.isCanceled } returns false
        every { taskResult.exception } returns null
        every { taskResult.result } answers { null } andThenAnswer { authResult }

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

        every { auth.startActivityForSignInWithProvider(any(), any()) } returns taskResult

        val result = remoteUser.signIn(activity)

        assertEquals(expected, result)

        verifySequence {
            auth.pendingAuthResult
            auth.startActivityForSignInWithProvider(any(), any())
        }

        verify(exactly = 1) { auth.pendingAuthResult }
        verify(exactly = 1) { auth.startActivityForSignInWithProvider(any(), any()) }
        confirmVerified(auth)
    }

//    @Test
//    fun `should be return User for signInForProvider`() = runTest {
//
//        every { auth.pendingAuthResult } returns null
//        every { auth.startActivityForSignInWithProvider(activity, provider) } returns taskResult
//
//        every { taskResult.isSuccessful } returns true
//        every { taskResult.isComplete } returns true
//        every { taskResult.isCanceled } returns false
//        every { taskResult.exception } returns null
//        every { taskResult.result } returns authResult
//
//        every { authResult.user } returns userFirebase
//
//        every { userFirebase.uid } returns "123"
//        every { userFirebase.displayName } returns "Test"
//        every { userFirebase.email } returns "email"
//        every { userFirebase.getIdToken(any()) } returns taskToken
//
//        every { taskToken.isSuccessful } returns true
//        every { taskToken.isComplete } returns true
//        every { taskToken.isCanceled } returns false
//        every { taskToken.exception } returns null
//        every { taskToken.result } returns tokenResult
//
//        every { tokenResult.token } returns "token"
//
//        val expected = User("Test", "email", "token")
//
//        val result = remoteUser.signIn(activity)
//
//        assertEquals(expected, result)
//    }
}
