package com.example.cooksy.viewModel.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cooksy.data.model.User // Make sure this import is correct
import com.example.cooksy.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

const val DEFAULT_AVATAR = "chefcito"

class SessionViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _loginState = MutableStateFlow<Result<Unit>?>(null)
    val loginState: StateFlow<Result<Unit>?> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<Result<Unit>?>(null)
    val registerState: StateFlow<Result<Unit>?> = _registerState.asStateFlow()

    private val _resetPasswordState = MutableStateFlow<Result<Unit>?>(null)
    val resetPasswordState: StateFlow<Result<Unit>?> = _resetPasswordState.asStateFlow()

    // Firestore User Profile State
    private val _userProfile = MutableStateFlow<User?>(null)
    val userProfile: StateFlow<User?> = _userProfile.asStateFlow()

    // Local Avatar Preference State (from SharedPreferences)
    private val _localAvatarPreference = MutableStateFlow(DEFAULT_AVATAR)
    val localAvatarPreference: StateFlow<String> = _localAvatarPreference.asStateFlow()

    private val _profileLoading = MutableStateFlow(false)
    val profileLoading: StateFlow<Boolean> = _profileLoading.asStateFlow()

    private val _updateProfileResult = MutableStateFlow<Result<Unit>?>(null)
    val updateProfileResult: StateFlow<Result<Unit>?> = _updateProfileResult.asStateFlow()

    private val _changePasswordResult = MutableStateFlow<Result<Unit>?>(null)
    val changePasswordResult: StateFlow<Result<Unit>?> = _changePasswordResult.asStateFlow()

    val isUserLoggedIn: Boolean
        get() = authRepository.isUserLoggedIn()

    val currentUserEmail: String?
        get() = authRepository.currentUser?.email

    init {
        if (isUserLoggedIn) {
            loadUserProfile()
            loadLocalAvatarPreference()
        }
    }

    private fun loadLocalAvatarPreference() {
        val preferredAvatar = authRepository.getAvatarPreference()
        _localAvatarPreference.value = preferredAvatar ?: _userProfile.value?.avatar ?: DEFAULT_AVATAR
    }

    fun updateLocalAvatarPreference(avatarName: String) {
        if (avatarName == "chefcito" || avatarName == "chefcita") {
            authRepository.saveAvatarPreference(avatarName)
            _localAvatarPreference.value = avatarName
        }
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _profileLoading.value = true
            val result = authRepository.getUserProfile()
            result.onSuccess {
                _userProfile.value = it
                val preferredAvatar = authRepository.getAvatarPreference()
                _localAvatarPreference.value = preferredAvatar ?: it?.avatar ?: DEFAULT_AVATAR
            }
            result.onFailure {
                _userProfile.value = null
                _localAvatarPreference.value = authRepository.getAvatarPreference() ?: DEFAULT_AVATAR
            }
            _profileLoading.value = false
        }
    }

    fun updateUserFullName(fullName: String) {
        viewModelScope.launch {
            _profileLoading.value = true
            _updateProfileResult.value = authRepository.updateUserFullName(fullName)
            if (_updateProfileResult.value?.isSuccess == true) {
                val refreshResult = authRepository.getUserProfile()
                refreshResult.onSuccess { _userProfile.value = it }
            }
            _profileLoading.value = false
        }
    }

    fun changeUserPassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            _profileLoading.value = true
            _changePasswordResult.value = authRepository.changePassword(currentPassword, newPassword)
            _profileLoading.value = false
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = authRepository.login(email, password)
            if (_loginState.value?.isSuccess == true) {
                loadUserProfile()
                loadLocalAvatarPreference()
            }
        }
    }

    fun register(fullName: String, email: String, password: String, avatar: String) {
        viewModelScope.launch {
            _registerState.value = authRepository.register(fullName, email, password, avatar)
            if (_registerState.value?.isSuccess == true) {
                if (avatar == "chefcito" || avatar == "chefcita") {
                    authRepository.saveAvatarPreference(avatar)
                    _localAvatarPreference.value = avatar
                } else {
                    authRepository.saveAvatarPreference(DEFAULT_AVATAR)
                    _localAvatarPreference.value = DEFAULT_AVATAR
                }
            }
        }
    }

    fun sendPasswordReset(email: String) {
        viewModelScope.launch {
            _resetPasswordState.value = authRepository.sendResetPassword(email)
        }
    }

    fun logout() {
        authRepository.logout()
        _userProfile.value = null
        _localAvatarPreference.value = DEFAULT_AVATAR

        _loginState.value = null
        _registerState.value = null
        _resetPasswordState.value = null
        _updateProfileResult.value = null
        _changePasswordResult.value = null
    }

    fun isEmailVerified(): Boolean {
        return authRepository.isEmailVerified()
    }

    fun setSessionPersistence(keepSession: Boolean) {
        authRepository.setKeepSession(keepSession)
    }

    fun clearLoginState() { _loginState.value = null }
    fun clearRegisterState() { _registerState.value = null }
    fun clearResetState() { _resetPasswordState.value = null }
    fun clearUpdateProfileResult() { _updateProfileResult.value = null }
    fun clearChangePasswordResult() { _changePasswordResult.value = null }
}
