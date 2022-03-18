
package ve.com.teeac.svgs.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.delay
import ve.com.teeac.svgs.DestinationScreen
import ve.com.teeac.svgs.R
import ve.com.teeac.svgs.ui.theme.BlueBlack

@Composable
fun SplashScreen(navController: NavController) {

    val scaleAnimation: Animatable<Float, AnimationVector1D> =
        remember { Animatable(initialValue = 0f) }

    AnimationSplashContent(
        scaleAnimation = scaleAnimation,
        navController = navController,
        durationMillisAnimation = 1500,
        delayScreen = 3000L
    )

    DesignSplashScreen(
        imagePainter = painterResource(id =
        R.drawable.logo),
        scaleAnimation = scaleAnimation
    )
}


@Composable
fun AnimationSplashContent(
    scaleAnimation: Animatable<Float, AnimationVector1D>,
    navController: NavController,
    durationMillisAnimation: Int,
    delayScreen: Long
) {

    LaunchedEffect(key1 = true) {
        scaleAnimation.animateTo(
            targetValue = 0.5F,
            animationSpec = tween(
                durationMillis = durationMillisAnimation,
                easing = {
                    OvershootInterpolator(3F).getInterpolation(it)
                }
            )
        )

        delay(timeMillis = delayScreen)

        navController.navigate(route =
        DestinationScreen.MainScreenDest.route) {
            popUpTo(route =
            DestinationScreen.SplashScreenDest.route) {
                inclusive = true
            }
        }
    }
}

@Composable
fun DesignSplashScreen(
    modifier: Modifier = Modifier,
    imagePainter: Painter,
    scaleAnimation: Animatable<Float, AnimationVector1D>
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    0.0f to Color.Black,
                    1f to BlueBlack,
                    start = Offset(0.0f, 50.0f),
                    end = Offset(0.0f, 100.0f)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = imagePainter,
                contentDescription = "Logo Splash Screen",
                modifier = modifier
                    .statusBarsPadding()
                    .size(400.dp)
                    .scale(scale = scaleAnimation.value),
            )

            Text(
                text = stringResource(R.string.service_control_and_management_system),
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center,
                modifier = modifier.scale(scale =
                scaleAnimation.value)
            )
        }
    }
}