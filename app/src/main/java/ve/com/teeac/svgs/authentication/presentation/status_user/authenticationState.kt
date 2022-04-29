package ve.com.teeac.svgs.authentication.presentation.status_user

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun authenticationState(
    viewModel: StatusUserViewModel = hiltViewModel()
): State<Boolean> {

    return produceState(initialValue = viewModel.hasUser) {
        viewModel.currentUser.collect { user ->
            value = user != null
        }
    }
}
