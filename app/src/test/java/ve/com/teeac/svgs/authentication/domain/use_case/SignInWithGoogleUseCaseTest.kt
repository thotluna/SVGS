package ve.com.teeac.svgs.authentication.domain.use_case

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ve.com.teeac.svgs.authentication.data.data_source.AuthRemoteUser
import ve.com.teeac.svgs.authentication.data.data_source.Credentials
import ve.com.teeac.svgs.authentication.data.models.User
import ve.com.teeac.svgs.authentication.data.repository.AuthRepositoryImpl
import ve.com.teeac.svgs.authentication.domain.repositories.AuthRepository

@ExperimentalCoroutinesApi
class SignInWithGoogleUseCaseTest {

    @MockK
    private lateinit var firebaseAuth: FirebaseAuth

    @MockK
    private lateinit var signInTask: Task<AuthResult>

    @MockK
    private lateinit var authResult: AuthResult

    @MockK
    private lateinit var taskGetToken: Task<GetTokenResult>

    @MockK
    private lateinit var getTokenResult: GetTokenResult

    @MockK
    private lateinit var currentUser: FirebaseUser

    private val email = "abc@gmail.com"
    private val displayName = "any"
    private val token = "123546132"

    private lateinit var authRemoteUser: AuthRemoteUser
    private lateinit var repository: AuthRepository
    private lateinit var useCase: SignInWithGoogleUseCase

    @Before
    fun setUp() {

        MockKAnnotations.init(this, relaxUnitFun = true)

        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        mockkStatic(GoogleAuthProvider::class)

        authRemoteUser = AuthRemoteUser(firebaseAuth)
        repository = AuthRepositoryImpl(authRemoteUser)
        useCase = SignInWithGoogleUseCase(repository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `sign in with idToken of Google`() = runTest {

        val tok = "abc"

        every { firebaseAuth.signInWithCredential(GoogleAuthProvider.getCredential(tok, null)) } returns signInTask
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

        val credential = Credentials(tok, null)

        val user = useCase(credential)

        assertEquals(expected, user)
    }
}
