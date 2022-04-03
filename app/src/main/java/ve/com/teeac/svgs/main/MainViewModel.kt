package ve.com.teeac.svgs.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ve.com.teeac.svgs.authentication.data.models.UserInfo
import ve.com.teeac.svgs.authentication.domain.use_case.ObserverStatusAuthUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    useCase: ObserverStatusAuthUseCase
) : ViewModel() {

    private val _currentUser = MutableStateFlow<UserInfo?>(null)
    val currentUser: StateFlow<UserInfo?> = _currentUser

    init {
        viewModelScope.launch {

            useCase().collect {
                _currentUser.value = it
            }
        }
    }
}

enum class MainStatus {
    LOADING,
    LOGGED,
    ANONYMOUS
}
