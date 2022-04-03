package ve.com.teeac.svgs.authentication.auth_google

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
import ve.com.teeac.svgs.R

@Composable
fun GoogleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorGoogle = ButtonDefaults.buttonColors(
        backgroundColor = colorResource(id = R.color.google_red),
        contentColor = MaterialTheme.colors.surface
    )

    Button(
        onClick = onClick,
        modifier = modifier,
        colors = colorGoogle,
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

    GoogleButton(
        onClick = { /*TODO*/ }
    )
}
