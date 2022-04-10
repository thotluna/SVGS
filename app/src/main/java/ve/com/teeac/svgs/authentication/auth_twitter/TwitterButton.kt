package ve.com.teeac.svgs.authentication.auth_twitter

import android.app.Activity
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ve.com.teeac.svgs.R
import ve.com.teeac.svgs.authentication.auth_twitter.domain.SignInTwitter

@Composable
fun TwitterButton(
    onLoading: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val signInTwitter = SignInTwitter("twitter.com")

    val colorTwitter = ButtonDefaults.buttonColors(
        backgroundColor = colorResource(id = R.color.twitter_blue),
        contentColor = MaterialTheme.colors.surface
    )

    Button(
        onClick = {
            onLoading()
            signInTwitter.signIn(context as Activity)
        },
        modifier = modifier,
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

@Preview
@Composable
private fun TwitterButtonPreview() {
    TwitterButton(
        {},
    )
}
