package ve.com.teeac.svgs.authentication.domain.use_case

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import ve.com.teeac.svgs.authentication.data.data_source.AuthRemoteUser
import ve.com.teeac.svgs.authentication.data.models.User
import ve.com.teeac.svgs.authentication.data.repository.AuthRepositoryImpl
import ve.com.teeac.svgs.authentication.domain.repositories.AuthRepository

class StateUserUseCaseTest {

    @MockK
    lateinit var auth: AuthRemoteUser

    lateinit var useCase: StateUserUseCase

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(Dispatchers.Unconfined)
        val repository: AuthRepository = AuthRepositoryImpl(auth)
        useCase = StateUserUseCase(repository)
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

        every { auth.authStateChanges() } returns flow { emit(expected) }
            .shareIn(
                CoroutineScope(SupervisorJob() + Dispatchers.Unconfined),
                replay = 1,
                started = SharingStarted.WhileSubscribed()
            )

        val user = useCase().take(1)
        assertEquals(expected, user.first())

        verify(exactly = 1) { auth.authStateChanges() }
        confirmVerified(auth)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should be return null`() = runTest {

        every { auth.authStateChanges() } returns flow { emit(null) }
            .shareIn(
                CoroutineScope(SupervisorJob() + Dispatchers.Unconfined),
                replay = 1,
                started = SharingStarted.WhileSubscribed()
            )

        val user = useCase().take(1)
        assertNull(user.first())

        verify(exactly = 1) { auth.authStateChanges() }
        confirmVerified(auth)
    }
}
