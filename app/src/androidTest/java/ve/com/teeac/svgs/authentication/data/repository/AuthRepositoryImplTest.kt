
package ve.com.teeac.svgs.authentication.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ve.com.teeac.svgs.authentication.data.data_source.AuthRemoteUser
import ve.com.teeac.svgs.authentication.data.models.UserInfo
import ve.com.teeac.svgs.authentication.domain.repositories.AuthRepository
import ve.com.teeac.svgs.core.exceptions.ExceptionManager

@ExperimentalCoroutinesApi
class AuthRepositoryImplTest {

    private lateinit var repository: AuthRepository
    private val auth = FirebaseAuth.getInstance()

    @Before
    fun setUp() {
        repository = AuthRepositoryImpl(
            AuthRemoteUser(FirebaseAuth.getInstance())
        )
    }

    @After
    fun setOut() = runTest {
        val user = auth.currentUser
        user?.let {
            it.delete()
            repository.signOut()
        }
    }

    @Test
    fun signUpByEmailAndPassword() = runTest {
        val email = "abc1@gmail.com"
        val password = "1aA@2345"
        val user: UserInfo? = repository.signUpByEmailAndPassword(email, password)
        assertNotNull(user)
        assertNotNull(user!!.token)
        assertNull(user.displayName)
        assertEquals(email, user.email)
    }

    @Test
    fun signUpByEmailAndPasswordWithEmailExistent_Exception() = runTest {

        val email = "abc2@gmail.com"
        val password = "2aA@2345"

        try {
            repository.signUpByEmailAndPassword(email, password)
            repository.signOut()
        } catch (e: Exception) {
            repository.signInByEmailAndPassword(email, password)
            repository.signOut()
        }

        val exceptionManager = ExceptionManager.getInstance()
        repository.signUpByEmailAndPassword(email, password)

        val result = exceptionManager.exceptionFlow.first()
        assertEquals("The user is already registered.", result)
    }

    @Test
    fun signInByEmailAndPassword() = runTest {
        val email = "abc3@gmail.com"
        val password = "3aA@2345"
        repository.signUpByEmailAndPassword(email, password)
        repository.signOut()
        val user: UserInfo? = repository.signInByEmailAndPassword(email, password)
        assertNotNull(user)
        assertNotNull(user!!.token)
        assertNull(user.displayName)
        assertEquals(email, user.email)
    }

    @Test
    fun signInByEmailAndPasswordWithEmailDoesNotExistent_Exception() = runTest {

        val email = "abc4@gmail.com"
        val password = "4aA@2345"

        val exceptionManager = ExceptionManager.getInstance()

        repository.signInByEmailAndPassword(email, password)

        val result = exceptionManager.exceptionFlow.first()
        assertEquals("Fail credentials", result)
    }

    @Test
    fun signInByEmailAndPasswordWithBadPassword() = runTest {

        val email = "abc5@gmail.com"
        val password = "5aA@2345"

        try {
            repository.signInByEmailAndPassword(email, password)
            repository.signOut()
        } catch (e: Exception) {
            repository.signUpByEmailAndPassword(email, password)
            repository.signOut()
        }

        val exceptionManager = ExceptionManager.getInstance()

        repository.signInByEmailAndPassword(email, "12353215")

        val result = exceptionManager.exceptionFlow.first()
        assertEquals("Fail credentials", result)
    }

    @Test
    fun authStateChanges() = runTest {

        val email = "abc6@gmail.com"
        val password = "6aA@2345"

        var user: UserInfo? = repository.authStateChanges().first()
        assertNull(user)
        repository.signUpByEmailAndPassword(email, password)
        user = repository.authStateChanges().first()
        assertNotNull(user)
        assertNotNull(user!!.token)
        assertEquals(email, user.email)
    }
}
