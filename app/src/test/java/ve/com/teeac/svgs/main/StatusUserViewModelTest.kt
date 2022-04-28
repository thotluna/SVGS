package ve.com.teeac.svgs.main

import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ve.com.teeac.svgs.authentication.data.models.User
import ve.com.teeac.svgs.authentication.domain.use_case.StateUserUseCase
import ve.com.teeac.svgs.authentication.presentation.status_user.StatusUserViewModel

class StatusUserViewModelTest {

    @MockK
    lateinit var useCase: StateUserUseCase

    lateinit var viewModel: StatusUserViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `should be return user`() {

        val user = User("display", "emal", "token")

        every { useCase() } returns flow { emit(user) }

        viewModel = StatusUserViewModel(useCase)

        assertEquals(user, viewModel.currentUser.value)
    }

    @Test
    fun `should be return null`() {

        val user = User("display", "emal", "token")

        every { useCase() } returns flow { emit(null) }

        viewModel = StatusUserViewModel(useCase)

        assertNull(viewModel.currentUser.value)
    }
}
