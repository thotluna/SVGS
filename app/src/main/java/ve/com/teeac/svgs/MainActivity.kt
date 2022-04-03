package ve.com.teeac.svgs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ve.com.teeac.svgs.main.MainScreen
import ve.com.teeac.svgs.ui.theme.SVGSTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val activityViewModel: MainActivityViewModel by viewModels {
        SavedStateViewModelFactory(application, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            activityViewModel.isLoading.value
        }

        setContent {
            SVGSTheme {
                MainScreen()
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
