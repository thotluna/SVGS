
package ve.com.teeac.svgs.authentication.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import ve.com.teeac.svgs.authentication.data.data_source.AuthRemoteUser
import ve.com.teeac.svgs.authentication.data.models.UserInfo
import ve.com.teeac.svgs.authentication.domain.repositories.AuthRepository
import ve.com.teeac.svgs.core.exceptions.CredentialsFailException
import ve.com.teeac.svgs.core.exceptions.UserCreationException

@ExperimentalCoroutinesApi
class AuthRepositoryImplTest {

    @get:Rule
    var exceptionRule: ExpectedException = ExpectedException.none()

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

        exceptionRule.expect(UserCreationException::class.java)
        exceptionRule.expectMessage("The user is already registered.")

        repository.signUpByEmailAndPassword(email, password)
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

        exceptionRule.expect(CredentialsFailException::class.java)
        exceptionRule.expectMessage("Fail credentials")
        repository.signInByEmailAndPassword(email, password)
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

        exceptionRule.expect(CredentialsFailException::class.java)
        exceptionRule.expectMessage("Fail credentials")
        repository.signInByEmailAndPassword(email, "12353215")
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
