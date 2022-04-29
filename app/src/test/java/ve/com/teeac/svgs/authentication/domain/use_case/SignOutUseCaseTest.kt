package ve.com.teeac.svgs.authentication.domain.use_case

import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ve.com.teeac.svgs.authentication.data.data_source.AuthRemoteUser
import ve.com.teeac.svgs.authentication.data.repository.AuthRepositoryImpl
import ve.com.teeac.svgs.authentication.domain.repositories.AuthRepository

class SignOutUseCaseTest {

    @MockK
    lateinit var auth: AuthRemoteUser

    lateinit var signOutUseCase: SignOutUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        val repository: AuthRepository = AuthRepositoryImpl(auth)
        signOutUseCase = SignOutUseCase(repository)
    }

    @Test
    fun `should be use auth signOut`() {
        every { auth.signOut() } returns Unit

        signOutUseCase()

        verify(exactly = 1) { auth.signOut() }
        confirmVerified(auth)
    }
}
