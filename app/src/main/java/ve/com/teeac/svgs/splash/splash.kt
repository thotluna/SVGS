
package ve.com.teeac.svgs.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay
import ve.com.teeac.svgs.R

enum class SplashState { Shown, Completed }
private const val SplashWaitTime: Long = 2000

@Composable
fun SplashScreen(modifier: Modifier = Modifier, onTimeout: () -> Unit) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary),
        contentAlignment = Alignment.Center,

    ) {
        val currentOnTimeout by rememberUpdatedState(onTimeout)

        LaunchedEffect(Unit) {
            delay(SplashWaitTime)
            currentOnTimeout()
        }
        Box(modifier = Modifier.fillMaxWidth(.5f)) {
            Image(painterResource(id = R.drawable.logo), contentDescription = null)
        }
    }
}
