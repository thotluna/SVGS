package ve.com.teeac.svgs.core.connection_network.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ve.com.teeac.svgs.R
import ve.com.teeac.svgs.ui.theme.Gayathri

@Preview
@Composable
fun NoConnectionScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.blue_black)),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier) {
            Image(
                painter = painterResource(id = R.drawable.disconnected),
                contentDescription = null
            )
            Text(
                text = "No internet connection",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h3.copy(
                    color = colorResource(id = R.color.white),
                    fontFamily = Gayathri
                )
            )
        }
    }
}
