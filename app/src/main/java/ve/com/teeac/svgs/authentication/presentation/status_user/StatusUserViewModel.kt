package ve.com.teeac.svgs.authentication.presentation.status_user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ve.com.teeac.svgs.authentication.data.models.User
import ve.com.teeac.svgs.authentication.domain.use_case.StateUserUseCase
import javax.inject.Inject

@HiltViewModel
class StatusUserViewModel @Inject constructor(
    useCase: StateUserUseCase
) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    val hasUser: Boolean
        get() = _currentUser.value != null

    init {
        viewModelScope.launch {
            useCase().collect {
                _currentUser.value = it
            }
        }
    }
}
