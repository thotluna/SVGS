
package ve.com.teeac.svgs.authentication.domain.use_case

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import ve.com.teeac.svgs.authentication.data.data_source.AuthRemoteUser
import ve.com.teeac.svgs.authentication.data.models.UserInfo
import ve.com.teeac.svgs.authentication.data.repository.AuthRepositoryImpl
import ve.com.teeac.svgs.authentication.domain.repositories.AuthRepository
import ve.com.teeac.svgs.core.exceptions.CredentialsFailException

@ExperimentalCoroutinesApi
class SignInByEmailAndPasswordUseCaseTest {

    @get:Rule
    var exceptionRule: ExpectedException = ExpectedException.none()

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

        MockKAnnotations.init(this, relaxUnitFun = true)
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")

        authRemoteUser = AuthRemoteUser(firebaseAuth)
        repository = AuthRepositoryImpl(authRemoteUser)
        useCase = SignInByEmailAndPasswordUseCase(repository)
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

        val expected = UserInfo(displayName, email, token)

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

        exceptionRule.expect(CredentialsFailException::class.java)
        exceptionRule.expectMessage("Fail credentials")

        useCase(email, password)
    }

    @Test
    fun signIn_fail_badEmail() = runTest {

        every {
            firebaseAuth.signInWithEmailAndPassword(
                email,
                password
            )
        } throws FirebaseAuthInvalidUserException("ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL", "message")

        exceptionRule.expect(CredentialsFailException::class.java)
        exceptionRule.expectMessage("Fail credentials")

        useCase(email, password)
    }
}
