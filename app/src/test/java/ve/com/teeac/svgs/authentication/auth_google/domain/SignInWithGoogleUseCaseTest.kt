package ve.com.teeac.svgs.authentication.auth_google.domain

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import ve.com.teeac.svgs.authentication.auth_google.Credentials
import ve.com.teeac.svgs.authentication.data.data_source.AuthRemoteUser
import ve.com.teeac.svgs.authentication.data.models.UserInfo
import ve.com.teeac.svgs.authentication.data.repository.AuthRepositoryImpl
import ve.com.teeac.svgs.authentication.domain.repositories.AuthRepository

@ExperimentalCoroutinesApi
class SignInWithGoogleUseCaseTest {

    @get:Rule
    var exceptionRule: ExpectedException = ExpectedException.none()

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
        /* token calculado
        val idToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImNlYzEzZGViZjRiOTY0Nzk2ODM3MzYyMDUwODI0NjZjMTQ3OTdiZDAiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI2NjExNDc0MjYxMjEtN25udGg1cDJuaDgybHJvaGFibWY1amU1c21vbnZudTguYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI2NjExNDc0MjYxMjEta2JpdWx1dDM4MHFqMW82MGppdXBwZWp0ZW9zc204OTMuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTMwNTk1MDUzOTkwNDc5OTg2MjciLCJlbWFpbCI6InRob3RsdW5hQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoiTG9ibyBEZSBMdW5hIiwicGljdHVyZSI6Imh0dHBzOi8vbGgzLmdvb2dsZXVzZXJjb250ZW50LmNvbS9hL0FBVFhBSnhjT19DYlh3UHRNajlORm5UbGpsSWxVTEp0QjZmSzdrZzVaaml0PXM5Ni1jIiwiZ2l2ZW5fbmFtZSI6IkxvYm8iLCJmYW1pbHlfbmFtZSI6IkRlIEx1bmEiLCJsb2NhbGUiOiJlcy00MTkiLCJpYXQiOjE2NDkwMjU1ODEsImV4cCI6MTY0OTAyOTE4MX0.vgal5caRWObHXLhTR2o-JwSEi_aKvaOnPc67E7K6N6b0yWIFtZgTA8Tc9IewCtGS8vMfSxqHP4kHFPnxha9nJ_Mfk3KLuDmjrFeykmI1xPwwRDkj_z6VlWp319t5Mzvvg_kaCX5UNdmEcFr9Zj4vOlr7xBd2kiyQwABNNw9ildxn8EI-sP8jdNsT3S10tyKiT6cPlVAKyIhaSbdGxL2yDNjurboJlqwbAFdDpirBUW9YdOLZfiysNUd2ec6pZklVEglzZ_YrG87BMMLORtEp2yIumZzC6k7bH4YWGKAf2HxDB9UwYfYph-zVt8IY0adE1WzV6odCYrJgsVHAJpCR-A"
        */

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

        val expected = UserInfo(displayName, email, token)

        val credential = Credentials(tok, null)

        val user = useCase(credential)

        assertEquals(expected, user)
    }
}
