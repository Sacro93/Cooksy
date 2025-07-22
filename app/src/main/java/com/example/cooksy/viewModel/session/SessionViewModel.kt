package com.example.cooksy.viewModel.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cooksy.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SessionViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _loginState = MutableStateFlow<Result<Unit>?>(null)
    val loginState: StateFlow<Result<Unit>?> = _loginState

    private val _registerState = MutableStateFlow<Result<Unit>?>(null)
    val registerState: StateFlow<Result<Unit>?> = _registerState

    private val _resetPasswordState = MutableStateFlow<Result<Unit>?>(null)
    val resetPasswordState: StateFlow<Result<Unit>?> = _resetPasswordState

    val isUserLoggedIn: Boolean
        get() = authRepository.isUserLoggedIn()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = authRepository.login(email, password)
        }
    }

    fun register(fullName: String, email: String, password: String, avatar: String) {
        viewModelScope.launch {
            _registerState.value = authRepository.register(fullName, email, password, avatar)
        }
    }

    fun sendPasswordReset(email: String) {
        viewModelScope.launch {
            _resetPasswordState.value = authRepository.sendResetPassword(email)
        }
    }

    fun logout() {
        authRepository.logout()
    }
    fun isEmailVerified(): Boolean {
        return authRepository.isEmailVerified()
    }
    fun setSessionPersistence(keepSession: Boolean) {
        authRepository.setKeepSession(keepSession)
    }


    // Limpieza de estados
    fun clearLoginState() { _loginState.value = null }
    fun clearRegisterState() { _registerState.value = null }
    fun clearResetState() { _resetPasswordState.value = null }
}