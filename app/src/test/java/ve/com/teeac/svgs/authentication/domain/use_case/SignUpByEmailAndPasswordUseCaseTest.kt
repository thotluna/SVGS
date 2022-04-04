
package ve.com.teeac.svgs.authentication.domain.use_case

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ve.com.teeac.svgs.authentication.data.data_source.AuthRemoteUser
import ve.com.teeac.svgs.authentication.data.models.UserInfo
import ve.com.teeac.svgs.authentication.data.repository.AuthRepositoryImpl
import ve.com.teeac.svgs.authentication.domain.repositories.AuthRepository
import ve.com.teeac.svgs.core.exceptions.ExceptionManager

@ExperimentalCoroutinesApi
class SignUpByEmailAndPasswordUseCaseTest {

    @MockK
    private lateinit var currentUser: FirebaseUser

    @MockK
    private lateinit var firebaseAuth: FirebaseAuth

    @MockK
    private lateinit var signUpTask: Task<AuthResult>

    @MockK
    private lateinit var taskGetToken: Task<GetTokenResult>

    @MockK
    private lateinit var getTokenResult: GetTokenResult

    @MockK
    private lateinit var authResult: AuthResult

    private lateinit var authRemoteUser: AuthRemoteUser
    private lateinit var repository: AuthRepository
    private lateinit var useCase: SignUpByEmailAndPasswordUseCase

    private val email = "abc@gmail.com"
    private val password = "1aA#1234"
    private val displayName = "any"
    private val token = "123546132"

    @Before
    fun setUp() {

        MockKAnnotations.init(this, relaxUnitFun = true)
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")

//        firebaseMock = mockk(relaxed = true)
        authRemoteUser = AuthRemoteUser(firebaseAuth)
        repository = AuthRepositoryImpl(authRemoteUser)
        useCase = SignUpByEmailAndPasswordUseCase(repository)
    }

    @Test
    fun createUser_OK() = runTest {

        every {
            firebaseAuth.createUserWithEmailAndPassword(
                email,
                password
            )
        } answers { signUpTask }
        every { signUpTask.isSuccessful } returns true
        every { signUpTask.isComplete } returns true
        every { signUpTask.isCanceled } returns false
        every { signUpTask.exception } returns null

        every { signUpTask.result } returns authResult

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
    fun createUser_Fail_EmailExist() = runTest {

        every {
            firebaseAuth.createUserWithEmailAndPassword(
                email,
                password
            )
        } throws FirebaseAuthUserCollisionException("ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL", "message")

        val exceptionManager = ExceptionManager.getInstance()

        useCase(email, password)

        val result = exceptionManager.exceptionFlow.first()
        assertEquals("The user is already registered.", result)
    }
}
