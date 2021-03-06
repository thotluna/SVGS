package ve.com.teeac.svgs.authentication.presentation.twitter_button

import android.app.Activity
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ve.com.teeac.svgs.R

@Composable
fun TwitterButton(
    onLoading: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    viewModel: TwitterButtonViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val colorTwitter = ButtonDefaults.buttonColors(
        backgroundColor = colorResource(id = R.color.twitter_blue),
        contentColor = MaterialTheme.colors.surface
    )

    fun click() {
        onLoading()
        viewModel.signIn(context as Activity)
    }

    Button(
        onClick = { click() },
        modifier = modifier,
        enabled = enabled,
        colors = colorTwitter, shape = RoundedCornerShape(24.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_twitter),
            contentDescription = stringResource(R.string.auth_google)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(stringResource(R.string.auth_with_twitter))
    }
}
