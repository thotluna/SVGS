package ve.com.teeac.svgs.authentication.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ve.com.teeac.svgs.authentication.domain.use_case.ObserverStatusAuthUseCase
import ve.com.teeac.svgs.core.exceptions.ExceptionManager
import javax.inject.Inject

@HiltViewModel
class SignViewModel @Inject constructor(
    private val observerStatusAuthUseCase: ObserverStatusAuthUseCase
) : ViewModel() {

    private val exceptionManager = ExceptionManager.getInstance()

    private var _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            observerStatusAuthUseCase().collectLatest {
                it?.let {
                    _isLoading.value = false
                }
            }
        }

        viewModelScope.launch {
            exceptionManager.exceptionFlow.collectLatest {
                _isLoading.value = false
            }
        }
    }

    fun onEvent(event: SignEvent) {
        when (event) {
            is SignEvent.OnLoading -> _isLoading.value = !isLoading.value
        }
    }
}
