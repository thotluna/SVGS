package ve.com.teeac.svgs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ve.com.teeac.svgs.authentication.AuthenticationScreen
import ve.com.teeac.svgs.main.MainScreen
import ve.com.teeac.svgs.ui.theme.SVGSTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepVisibleCondition {
            viewModel.isLoading.value
        }
        setContent {
            SVGSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AuthenticationScreen()
                }
            }
        }
    }
}

sealed class DestinationScreen(val route: String) {
    object MainScreenDest : DestinationScreen(route = "main_screen")
}

@Composable
fun NavigationScreen() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = DestinationScreen.MainScreenDest.route
    ) {

        composable(route = DestinationScreen.MainScreenDest.route) {
            MainScreen()
        }
    }
}
