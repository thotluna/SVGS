package ve.com.teeac.svgs.authentication.data.data_source

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ve.com.teeac.svgs.authentication.data.models.User
import ve.com.teeac.svgs.core.exceptions.AuthenticationException
import ve.com.teeac.svgs.core.exceptions.CredentialsFailException
import ve.com.teeac.svgs.core.exceptions.UserCollisionException
import java.lang.Exception

@ExperimentalCoroutinesApi
class AuthRemoteUserTest {

    @MockK
    lateinit var auth: FirebaseAuth

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

    private lateinit var remoteUser: AuthRemoteFirebase

    private val email = "user@gmail.com"
    private val password = "password"

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")

        remoteUser = AuthRemoteFirebase(auth)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should be return user by sign up by email and password `() = runTest {

        every { auth.createUserWithEmailAndPassword(email, password) } returns taskResult

        every { taskResult.isComplete } returns true
        every { taskResult.exception } returns null
        every { taskResult.isCanceled } returns false
        every { taskResult.result } returns authResult

        every { auth.currentUser } returns userFirebase
        every { userFirebase.getIdToken(any()) } returns taskToken
        every { taskToken.isComplete } returns true
        every { taskToken.exception } returns null
        every { taskToken.isCanceled } returns false
        every { taskToken.result } returns tokenResult

        every { userFirebase.displayName } returns "Test"
        every { userFirebase.email } returns email
        every { tokenResult.token } returns "token"

        val expected = User("Test", email, "token")

        val result = remoteUser.signUpByEmailAndPassword(email, password)

        println("result: $result")

        assertEquals(expected, result)

        verify(exactly = 1) { auth.createUserWithEmailAndPassword(email, password) }
        verify(exactly = 1) { auth.currentUser }
        confirmVerified(auth)
    }

    @Test(expected = AuthenticationException::class)
    fun `should be return error by sign after registering `() = runTest {

        every { auth.createUserWithEmailAndPassword(email, password) } returns taskResult

        every { taskResult.isComplete } returns true
        every { taskResult.exception } returns null
        every { taskResult.isCanceled } returns false
        every { taskResult.result } returns null

        every { auth.currentUser } returns null

        val result = remoteUser.signUpByEmailAndPassword(email, password)

        assertNull(result)

        verify(exactly = 1) { auth.createUserWithEmailAndPassword(email, password) }
        verify(exactly = 1) { auth.currentUser }
        confirmVerified(auth)
    }

    @Test(expected = UserCollisionException::class)
    fun `should be return error by sign up by email existent `() = runTest {

        every { auth.createUserWithEmailAndPassword(email, password) } returns taskResult

        every { taskResult.isComplete } returns true
        every { taskResult.exception } throws FirebaseAuthUserCollisionException("", "")

        val result = remoteUser.signUpByEmailAndPassword(email, password)

        assertNull(result)

        verify(exactly = 1) { auth.createUserWithEmailAndPassword(email, password) }
        confirmVerified(auth)
    }

    @Test(expected = Exception::class)
    fun `should be return error by sign up other exception `() = runTest {

        every { auth.createUserWithEmailAndPassword(email, password) } returns taskResult

        every { taskResult.isComplete } returns true
        every { taskResult.exception } throws ClassNotFoundException("this is an test exception")

        val result = remoteUser.signUpByEmailAndPassword(email, password)
        assertNull(result)

        verify(exactly = 1) { auth.createUserWithEmailAndPassword(email, password) }
        confirmVerified(auth)
    }

    @Test
    fun `should be return user by sign in by email and password `() = runTest {

        every { auth.signInWithEmailAndPassword(email, password) } returns taskResult

        every { taskResult.isComplete } returns true
        every { taskResult.exception } returns null
        every { taskResult.isCanceled } returns false
        every { taskResult.result } returns authResult

        every { auth.currentUser } returns userFirebase
        every { userFirebase.getIdToken(any()) } returns taskToken
        every { taskToken.isComplete } returns true
        every { taskToken.exception } returns null
        every { taskToken.isCanceled } returns false
        every { taskToken.result } returns tokenResult

        every { userFirebase.displayName } returns "Test"
        every { userFirebase.email } returns email
        every { tokenResult.token } returns "token"

        val expected = User("Test", email, "token")

        val result = remoteUser.signInByEmailAndPassword(email, password)

        println("result: $result")

        assertEquals(expected, result)

        verify(exactly = 1) { auth.signInWithEmailAndPassword(email, password) }
        verify(exactly = 1) { auth.currentUser }
        confirmVerified(auth)
    }

    @Test(expected = AuthenticationException::class)
    fun `should be return error by sign in  after registering `() = runTest {

        every { auth.signInWithEmailAndPassword(email, password) } returns taskResult

        every { taskResult.isComplete } returns true
        every { taskResult.exception } returns null
        every { taskResult.isCanceled } returns false
        every { taskResult.result } returns null

        every { auth.currentUser } returns null

        val result = remoteUser.signInByEmailAndPassword(email, password)

        assertNull(result)

        verify(exactly = 1) { auth.signInWithEmailAndPassword(email, password) }
        verify(exactly = 1) { auth.currentUser }
        confirmVerified(auth)
    }

    @Test(expected = AuthenticationException::class)
    fun `should be return error by sign in invalid user `() = runTest {

        every { auth.signInWithEmailAndPassword(email, password) } returns taskResult

        every { taskResult.isComplete } returns true
        every { taskResult.exception } throws FirebaseAuthInvalidUserException("", "")

        val result = remoteUser.signInByEmailAndPassword(email, password)

        assertNull(result)

        verify(exactly = 1) { auth.signInWithEmailAndPassword(email, password) }
        confirmVerified(auth)
    }

    @Test(expected = CredentialsFailException::class)
    fun `should be return error by sign in bad password `() = runTest {

        every { auth.signInWithEmailAndPassword(email, password) } returns taskResult

        every { taskResult.isComplete } returns true
        every { taskResult.exception } throws FirebaseAuthInvalidCredentialsException("", "")

        val result = remoteUser.signInByEmailAndPassword(email, password)

        assertNull(result)

        verify(exactly = 1) { auth.signInWithEmailAndPassword(email, password) }
        confirmVerified(auth)
    }

    @Test(expected = UserCollisionException::class)
    fun `should be return error by sign in other exception `() = runTest {

        every { auth.signInWithEmailAndPassword(email, password) } returns taskResult

        every { taskResult.isComplete } returns true
        every { taskResult.exception } throws UserCollisionException("this is an test exception")

        val result = remoteUser.signInByEmailAndPassword(email, password)
        assertNull(result)

        verify(exactly = 1) { auth.signInWithEmailAndPassword(email, password) }
        confirmVerified(auth)
    }

    @Test
    fun `should be return user by credentials`() = runTest {

        val authCredential = mockk<AuthCredential>()

        mockkStatic(GoogleAuthProvider::class)
        every { GoogleAuthProvider.getCredential(any(), any()) } returns authCredential

        every { auth.signInWithCredential(authCredential) } returns taskResult

        every { taskResult.isComplete } returns true
        every { taskResult.exception } returns null
        every { taskResult.isCanceled } returns false
        every { taskResult.result } returns authResult

        every { auth.currentUser } returns userFirebase

        every { userFirebase.getIdToken(any()) } returns taskToken
        every { taskToken.isComplete } returns true
        every { taskToken.exception } returns null
        every { taskToken.isCanceled } returns false
        every { taskToken.result } returns tokenResult

        every { userFirebase.displayName } returns "Test"
        every { userFirebase.email } returns email
        every { tokenResult.token } returns "token"

        val expected = User("Test", email, "token")

        val result = remoteUser.authenticationWithCredential(email, null)

        assertEquals(expected, result)

        verify(exactly = 1) { auth.signInWithCredential(authCredential) }
        verify(exactly = 1) { auth.currentUser }

        confirmVerified(auth)
    }

    @Test(expected = AuthenticationException::class)
    fun `should be return error by sign in by credentials  after registering `() = runTest {
        val token = "token"

        every { auth.signInWithCredential(any()) } returns taskResult

        every { taskResult.isComplete } returns true
        every { taskResult.exception } returns null
        every { taskResult.isCanceled } returns false
        every { taskResult.result } returns null

        every { auth.currentUser } returns null

        val result = remoteUser.authenticationWithCredential(token, null)

        assertNull(result)

        verify(exactly = 1) { auth.signInWithCredential(any()) }
        verify(exactly = 1) { auth.currentUser }
        confirmVerified(auth)
    }

    @Test(expected = AuthenticationException::class)
    fun `should be return error by sign in by credentials  user disabled `() = runTest {
        val token = "token"

        every { auth.signInWithCredential(any()) } returns taskResult

        every { taskResult.isComplete } returns true
        every { taskResult.exception } throws FirebaseAuthInvalidUserException("", "")

        val result = remoteUser.authenticationWithCredential(token, null)

        assertNull(result)

        verify(exactly = 1) { auth.signInWithCredential(any()) }
        confirmVerified(auth)
    }

    @Test(expected = CredentialsFailException::class)
    fun `should be return error by sign in by credentials  bad credential`() = runTest {
        val token = "token"

        every { auth.signInWithCredential(any()) } returns taskResult

        every { taskResult.isComplete } returns true
        every { taskResult.exception } throws FirebaseAuthInvalidCredentialsException("", "")

        val result = remoteUser.authenticationWithCredential(token, null)

        assertNull(result)

        verify(exactly = 1) { auth.signInWithCredential(any()) }
        confirmVerified(auth)
    }

    @Test(expected = UserCollisionException::class)
    fun `should be return error by sign in by credentials  user exist `() = runTest {
        val token = "token"

        every { auth.signInWithCredential(any()) } returns taskResult

        every { taskResult.isComplete } returns true
        every { taskResult.exception } throws FirebaseAuthUserCollisionException("", "")

        val result = remoteUser.authenticationWithCredential(token, null)

        assertNull(result)

        verify(exactly = 1) { auth.signInWithCredential(any()) }
        confirmVerified(auth)
    }

    @Test
    fun `should be sign out`() = runTest {
        every { auth.signOut() } returns Unit

        remoteUser.signOut()

        verify(exactly = 1) { auth.signOut() }
        confirmVerified(auth)
    }
}
