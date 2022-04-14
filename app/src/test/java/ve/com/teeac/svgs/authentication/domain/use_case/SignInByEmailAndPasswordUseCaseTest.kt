
package ve.com.teeac.svgs.authentication.domain.use_case

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ve.com.teeac.svgs.authentication.data.data_source.AuthRemoteUser
import ve.com.teeac.svgs.authentication.data.models.User
import ve.com.teeac.svgs.authentication.data.repository.AuthRepositoryImpl
import ve.com.teeac.svgs.authentication.domain.repositories.AuthRepository
import ve.com.teeac.svgs.core.exceptions.ExceptionManager

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
class SignInByEmailAndPasswordUseCaseTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @MockK
    private lateinit var currentUser: FirebaseUser

    @MockK
    private lateinit var firebaseAuth: FirebaseAuth

    @MockK
    private lateinit var signInTask: Task<AuthResult>

    @MockK
    private lateinit var taskGetToken: Task<GetTokenResult>

    @MockK
    private lateinit var getTokenResult: GetTokenResult

    @MockK
    private lateinit var authResult: AuthResult

    private lateinit var authRemoteUser: AuthRemoteUser
    private lateinit var repository: AuthRepository
    private lateinit var useCase: SignInByEmailAndPasswordUseCase

    private val email = "abc@gmail.com"
    private val password = "1aA#1234"
    private val displayName = "any"
    private val token = "123546132"

    @Before
    fun setUp() {

        Dispatchers.setMain(mainThreadSurrogate)

        MockKAnnotations.init(this, relaxUnitFun = true)
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")

        authRemoteUser = AuthRemoteUser(firebaseAuth)
        repository = AuthRepositoryImpl(authRemoteUser)
        useCase = SignInByEmailAndPasswordUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()

        unmockkAll()
    }

    @Test
    fun signIn_ok() = runTest {

        every {
            firebaseAuth.signInWithEmailAndPassword(
                email,
                password
            )
        } answers { signInTask }
        every { signInTask.isSuccessful } returns true
        every { signInTask.isComplete } returns true
        every { signInTask.isCanceled } returns false
        every { signInTask.exception } returns null

        every { signInTask.result } returns authResult

        every { firebaseAuth.currentUser } returns currentUser
        every { currentUser.getIdToken(any()) } returns taskGetToken
        every { taskGetToken.isSuccessful } returns true
        every { taskGetToken.isComplete } returns true
        every { taskGetToken.isCanceled } returns false
        every { taskGetToken.exception } returns null
        every { taskGetToken.result } returns getTokenResult
        every { getTokenResult.token } returns token
        every { currentUser.displayName } returns displayName
        every { currentUser.email } returns email

        val expected = User(displayName, email, token)

        val userInfo = useCase(email, password)

        assertEquals(expected, userInfo)
    }

    @Test
    fun signIn_fail_badPassword() = runTest {

        every {
            firebaseAuth.signInWithEmailAndPassword(
                email,
                password
            )
        } throws FirebaseAuthInvalidCredentialsException("code_error", "message")

        val exceptionManager = ExceptionManager.getInstance()

        useCase(email, password)

        val result = exceptionManager.exceptionFlow.first()
        assertEquals("Fail credentials", result)
    }

    @Test
    fun signIn_fail_badEmail() = runTest {

        every {
            firebaseAuth.signInWithEmailAndPassword(
                email,
                password
            )
        } throws FirebaseAuthInvalidUserException("ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL", "message")

        val exceptionManager = ExceptionManager.getInstance()

        useCase(email, password)

        val result = exceptionManager.exceptionFlow.first()
        assertEquals("Fail credentials", result)
    }
}
