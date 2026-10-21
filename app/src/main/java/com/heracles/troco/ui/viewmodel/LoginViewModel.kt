package com.heracles.troco.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heracles.troco.domain.model.AuthResult
import com.heracles.troco.domain.model.User
import com.heracles.troco.domain.repository.AuthRepository
import com.heracles.troco.domain.usecase.auth.ObserveUserUseCase
import com.heracles.troco.ui.screen.AuthUiState
import com.heracles.troco.ui.screen.LoginErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val observeUserUseCase: ObserveUserUseCase
) : ViewModel() {

    private val _authenticated = MutableStateFlow(false)
    private val _isLoading = MutableStateFlow(false)
    private val _phone = MutableStateFlow("")
    private val _errors = MutableStateFlow(LoginErrors())
    private val _password = MutableStateFlow("")

    private val _userProfileState =
        MutableStateFlow<UserProfileUiState>(UserProfileUiState.Loading)

    val userProfileState: StateFlow<UserProfileUiState> =
        _userProfileState.asStateFlow()

    init {
        observeUserProfile()

        if (authRepository.usuarioAtual != null) {
            viewModelScope.launch {
                val validatedSession = authRepository.validateSession()

                _authenticated.value = validatedSession
            }
        }
    }

    val uiState = combine(
        _phone,
        _isLoading,
        _password,
        _errors,
        _authenticated,
    ) { phone, loading, password, errors, authenticated ->
        AuthUiState(
            phone = phone,
            isLoading = loading,
            password = password,
            loginError = errors,
            autenticado = authenticated
        )
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            AuthUiState()
        )

    fun onPhoneChange(value: String) {
        val digitsOnly = value.filter { it.isDigit() }
        if (digitsOnly.length <= 11) {
            _phone.value = digitsOnly
        }
        _errors.value = _errors.value.copy(phone = "")
    }

    fun onPasswordChange(value: String) {
        _password.value = value
        _errors.value = _errors.value.copy(password = "")
    }

    fun signIn() {
        if (_isLoading.value) return

        _errors.value = LoginErrors()

        val phone = _phone.value
        val password = _password.value

        when {
            phone.isBlank() -> {
                _errors.value = LoginErrors(
                    phone = "Telefone não pode ser vazio"
                )
            }

            password.isBlank() -> {
                _errors.value = LoginErrors(
                    password = "Senha não pode ser vazia"
                )
            }

            else -> {
                viewModelScope.launch {
                    _isLoading.value = true

                    try {
                        val result = authRepository.loginWithPhone(
                            phone = phone,
                            password = password
                        )

                        applyResult(result)

                    } catch (e: CancellationException) {
                        throw e

                    } catch (e: Exception) {
                        _errors.value = LoginErrors(
                            password = "Não foi possível realizar o login. Tente novamente."
                        )

                    } finally {
                        _isLoading.value = false
                    }
                }
            }
        }
    }

    private fun applyResult(result: AuthResult) {
        when (result) {
            is AuthResult.Success -> {
                _authenticated.value = true
            }

            is AuthResult.Error -> _errors.value = LoginErrors(password = result.message)
        }
    }

    private fun observeUserProfile() {
        viewModelScope.launch {
            observeUserUseCase()
                .catch { error ->
                    _userProfileState.value =
                        UserProfileUiState.Error(
                            message = error.localizedMessage
                                ?: "Não foi possível carregar o perfil."
                        )
                }
                .collect { user ->
                    _userProfileState.value = when {
                        user != null -> UserProfileUiState.Loaded(user)
                        authRepository.usuarioAtual == null -> UserProfileUiState.SignedOut
                        else -> UserProfileUiState.Missing
                    }
                }
        }
    }
}

sealed interface UserProfileUiState {
    data object Loading : UserProfileUiState

    data object SignedOut : UserProfileUiState

    data class Loaded(
        val user: User
    ) : UserProfileUiState

    data object Missing : UserProfileUiState

    data class Error(
        val message: String
    ) : UserProfileUiState
}