package ve.com.teeac.svgs.authentication

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun AuthenticationScreen() {
    val heartTransition = rememberInfiniteTransition()

    val volumeHeart by heartTransition.animateFloat(
        initialValue = 80f,
        targetValue = 200f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 800,
                delayMillis = 100,
                easing = FastOutLinearInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(modifier = Modifier){
        Text(text = "Authentication")
    }
}