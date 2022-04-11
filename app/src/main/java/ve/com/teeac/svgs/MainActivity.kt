package ve.com.teeac.svgs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ve.com.teeac.svgs.core.connection_network.presentation.ConnectivityStatus
import ve.com.teeac.svgs.ui.theme.SVGSTheme

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SVGSTheme {
                ConnectivityStatus()
            }
        }
    }
}
