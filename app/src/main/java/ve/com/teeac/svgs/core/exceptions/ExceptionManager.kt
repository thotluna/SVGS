package ve.com.teeac.svgs.core.exceptions

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ExceptionManager {

    private val _exceptionFlow = MutableSharedFlow<String>(replay = 1)
    val exceptionFlow = _exceptionFlow.asSharedFlow()

    companion object {

        private var instance: ExceptionManager? = null

        fun getInstance(): ExceptionManager {
            if (instance != null) return instance as ExceptionManager

            instance = ExceptionManager()

            return instance as ExceptionManager
        }
    }

    suspend fun setException(e: String) {
        _exceptionFlow.emit(e)
    }
}
