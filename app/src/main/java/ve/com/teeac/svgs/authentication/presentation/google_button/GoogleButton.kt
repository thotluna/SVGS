package ve.com.teeac.svgs.authentication.presentation.google_button

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ve.com.teeac.svgs.R
import ve.com.teeac.svgs.authentication.data.data_source.AuthResultContract

@Composable
fun GoogleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    viewModel: GoogleAuthViewModel = hiltViewModel(),

) {

    val authResultLauncher =
        rememberLauncherForActivityResult(contract = AuthResultContract()) { credentials ->
            viewModel.singInGoogle(credentials)
        }

    val colorGoogle = ButtonDefaults.buttonColors(
        backgroundColor = colorResource(id = R.color.google_red),
        contentColor = MaterialTheme.colors.surface
    )

    fun handleClick() {
        authResultLauncher.launch(viewModel.signInRequestCode)
        onClick()
    }

    Button(
        onClick = { handleClick() },
        modifier = modifier,
        colors = colorGoogle,
        enabled = enabled,
        shape = RoundedCornerShape(24.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_google),
            contentDescription = stringResource(R.string.auth_google)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(stringResource(R.string.auth_with_google))
    }
}

@Preview
@Composable
private fun GoogleButtonPreview() {
    GoogleButton({}, enabled = false)
}
