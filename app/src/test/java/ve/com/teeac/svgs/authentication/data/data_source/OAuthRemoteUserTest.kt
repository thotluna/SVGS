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
import ve.com.teeac.svgs.core.exceptions.AuthenticationException
import ve.com.teeac.svgs.core.exceptions.CredentialsFailException
import ve.com.teeac.svgs.core.exceptions.UserCollisionException

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

    @Test(expected = CredentialsFailException::class)
    fun `should be return CredentialsFailException for pending `() = runTest {

        every { auth.pendingAuthResult } returns taskResult
        every { taskResult.isComplete } returns true
        every { taskResult.exception } throws FirebaseAuthInvalidCredentialsException("", "")

        remoteUser.signIn(activity)

        verify(exactly = 1) { auth.pendingAuthResult }
        confirmVerified(auth)
    }

    @Test(expected = CredentialsFailException::class)
    fun `should be return CredentialsFailException for start activity `() = runTest {

        every { auth.pendingAuthResult } returns taskResult

        every { taskResult.isComplete } returns true
        every { taskResult.exception } answers { null } andThenThrows FirebaseAuthInvalidCredentialsException("", "")
        every { taskResult.isSuccessful } returns true
        every { taskResult.isCanceled } returns false
        every { taskResult.result } answers { null }

        every { auth.startActivityForSignInWithProvider(any(), any()) } returns taskResult

        remoteUser.signIn(activity)

        verifySequence {
            auth.pendingAuthResult
            auth.startActivityForSignInWithProvider(any(), any())
        }

        verify(exactly = 1) { auth.pendingAuthResult }
        verify(exactly = 1) { auth.startActivityForSignInWithProvider(any(), any()) }
        confirmVerified(auth)
    }

    @Test(expected = AuthenticationException::class)
    fun `should be return AuthenticationException for pending `() = runTest {

        every { auth.pendingAuthResult } returns taskResult
        every { taskResult.isComplete } returns true
        every { taskResult.exception } throws FirebaseAuthInvalidUserException("", "")

        remoteUser.signIn(activity)

        verify(exactly = 1) { auth.pendingAuthResult }
        confirmVerified(auth)
    }

    @Test(expected = AuthenticationException::class)
    fun `should be return AuthenticationException for start activity `() = runTest {

        every { auth.pendingAuthResult } returns taskResult

        every { taskResult.isComplete } returns true
        every { taskResult.exception } answers { null } andThenThrows FirebaseAuthInvalidUserException("", "")
        every { taskResult.isSuccessful } returns true
        every { taskResult.isCanceled } returns false
        every { taskResult.result } answers { null }

        every { auth.startActivityForSignInWithProvider(any(), any()) } returns taskResult

        remoteUser.signIn(activity)

        verifySequence {
            auth.pendingAuthResult
            auth.startActivityForSignInWithProvider(any(), any())
        }

        verify(exactly = 1) { auth.pendingAuthResult }
        verify(exactly = 1) { auth.startActivityForSignInWithProvider(any(), any()) }
        confirmVerified(auth)
    }

    @Test(expected = UserCollisionException::class)
    fun `should be return UserCollisionException for pending `() = runTest {

        every { auth.pendingAuthResult } returns taskResult
        every { taskResult.isComplete } returns true
        every { taskResult.exception } throws FirebaseAuthUserCollisionException("", "")

        remoteUser.signIn(activity)

        verify(exactly = 1) { auth.pendingAuthResult }
        confirmVerified(auth)
    }

    @Test(expected = UserCollisionException::class)
    fun `should be return UserCollisionException for start activity `() = runTest {

        every { auth.pendingAuthResult } returns taskResult

        every { taskResult.isComplete } returns true
        every { taskResult.exception } answers { null } andThenThrows FirebaseAuthUserCollisionException("", "")
        every { taskResult.isSuccessful } returns true
        every { taskResult.isCanceled } returns false
        every { taskResult.result } answers { null }

        every { auth.startActivityForSignInWithProvider(any(), any()) } returns taskResult

        remoteUser.signIn(activity)

        verifySequence {
            auth.pendingAuthResult
            auth.startActivityForSignInWithProvider(any(), any())
        }

        verify(exactly = 1) { auth.pendingAuthResult }
        verify(exactly = 1) { auth.startActivityForSignInWithProvider(any(), any()) }
        confirmVerified(auth)
    }

    @Test(expected = AuthenticationException::class)
    fun `should be return exception web for start activity `() = runTest {

        every { auth.pendingAuthResult } returns taskResult

        every { taskResult.isComplete } returns true
        every { taskResult.exception } answers { null } andThenThrows FirebaseAuthWebException("", "")
        every { taskResult.isSuccessful } returns true
        every { taskResult.isCanceled } returns false
        every { taskResult.result } answers { null }

        every { auth.startActivityForSignInWithProvider(any(), any()) } returns taskResult

        remoteUser.signIn(activity)

        verifySequence {
            auth.pendingAuthResult
            auth.startActivityForSignInWithProvider(any(), any())
        }

        verify(exactly = 1) { auth.pendingAuthResult }
        verify(exactly = 1) { auth.startActivityForSignInWithProvider(any(), any()) }
        confirmVerified(auth)
    }

    @Test(expected = AuthenticationException::class)
    fun `should be return exception web for pending `() = runTest {

        every { auth.pendingAuthResult } returns taskResult
        every { taskResult.isComplete } returns true
        every { taskResult.exception } throws FirebaseAuthWebException("", "")

        remoteUser.signIn(activity)

        verify(exactly = 1) { auth.pendingAuthResult }
        confirmVerified(auth)
    }

    @Test(expected = AuthenticationException::class)
    fun `should be return FirebaseAuthException for pending `() = runTest {

        every { auth.pendingAuthResult } returns taskResult
        every { taskResult.isComplete } returns true
        every { taskResult.exception } throws FirebaseAuthException("", "")

        remoteUser.signIn(activity)

        verify(exactly = 1) { auth.pendingAuthResult }
        confirmVerified(auth)
    }

    @Test(expected = AuthenticationException::class)
    fun `should be return FirebaseAuthException for start activity `() = runTest {

        every { auth.pendingAuthResult } returns taskResult

        every { taskResult.isComplete } returns true
        every { taskResult.exception } answers { null } andThenThrows FirebaseAuthException("", "")
        every { taskResult.isSuccessful } returns true
        every { taskResult.isCanceled } returns false
        every { taskResult.result } answers { null }

        every { auth.startActivityForSignInWithProvider(any(), any()) } returns taskResult

        remoteUser.signIn(activity)

        verifySequence {
            auth.pendingAuthResult
            auth.startActivityForSignInWithProvider(any(), any())
        }

        verify(exactly = 1) { auth.pendingAuthResult }
        verify(exactly = 1) { auth.startActivityForSignInWithProvider(any(), any()) }
        confirmVerified(auth)
    }
}
