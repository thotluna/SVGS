package ve.com.teeac.svgs.authentication.domain.use_case

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ve.com.teeac.svgs.authentication.data.data_source.AuthRemoteUser
import ve.com.teeac.svgs.authentication.data.models.User
import ve.com.teeac.svgs.authentication.data.repository.AuthRepositoryImpl
import ve.com.teeac.svgs.authentication.domain.repositories.AuthRepository

class ObserverStatusAuthUseCaseTest {

    @MockK
    lateinit var auth: AuthRemoteUser

    lateinit var useCase: ObserverStatusAuthUseCase

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(Dispatchers.Unconfined)
        val repository: AuthRepository = AuthRepositoryImpl(auth)
        useCase = ObserverStatusAuthUseCase(repository)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should be return user1`() = runTest {

        val expected = User("user", "email", "token")

        val sharedFlow = MutableSharedFlow<User?>()
//        val sharedFlow = _sharedFlow.asSharedFlow()
        sharedFlow.emit(expected)

        every { auth.authStateChanges() } returns sharedFlow

        val user = useCase().take(1)

        verify(exactly = 1) { auth.authStateChanges() }
        confirmVerified(auth)
    }
}
