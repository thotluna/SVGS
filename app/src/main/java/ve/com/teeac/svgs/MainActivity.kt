package ve.com.teeac.svgs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import ve.com.teeac.svgs.core.traker_connection.NetworkStatusTrackerScreen
import ve.com.teeac.svgs.main.MainScreen
import ve.com.teeac.svgs.ui.theme.SVGSTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SVGSTheme {
                NetworkStatusTrackerScreen {
                    MainScreen()
                }
            }
        }
    }
}
