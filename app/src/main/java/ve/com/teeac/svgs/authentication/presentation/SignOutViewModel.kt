package ve.com.teeac.svgs.authentication.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ve.com.teeac.svgs.authentication.domain.use_case.SignOutUseCase
import javax.inject.Inject

@HiltViewModel
class SignOutViewModel @Inject constructor(
    private val useCase: SignOutUseCase
) : ViewModel() {
    fun signOut() = useCase()
}
