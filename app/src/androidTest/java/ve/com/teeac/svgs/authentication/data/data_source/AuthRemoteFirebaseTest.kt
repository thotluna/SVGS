
package ve.com.teeac.svgs.authentication.data.data_source

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthRemoteFirebaseTest {

    lateinit var auth: AuthRemoteFirebase

    private val username = "ela@gmail.com"
    private val password = "1aA#2345"

    @Before
    fun setUp() {
        auth = AuthRemoteFirebase(FirebaseAuth.getInstance())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun authStateChanges() = runTest {
        auth.signInByEmailAndPassword(username, password)

        auth.authStateChanges().first().let {
            assertNotNull(it)
            assertEquals(it?.email, username)
        }
    }
}
