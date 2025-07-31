// app/src/main/java/com/project/smartmunimji/viewmodel/LoginViewModel.kt

package com.project.smartmunimji.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.project.smartmunimji.model.AppErrorResponse
import com.project.smartmunimji.model.request.LoginRequest
import com.project.smartmunimji.model.response.LoginResponse
import com.project.smartmunimji.model.response.CommonResponse
import com.project.smartmunimji.repository.AppRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// No change to LoginState as it's specific to LoginViewModel
// LoginViewModel does not use 'Status' or 'ProductRegistrationState' directly

class LoginViewModel(private val repository: AppRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginState>()
    val loginResult: LiveData<LoginState> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) {
        _isLoading.value = true
        _loginResult.value = LoginState.Loading

        viewModelScope.launch {
            try {
                val request = LoginRequest(email, password)
                val response = repository.login(request)

                if (response.isSuccessful) {
                    val commonResponse = response.body()
                    if (commonResponse != null && commonResponse.getStatus() == "success") {
                        _loginResult.value = LoginState.Success(commonResponse.getData())
                    } else {
                        val errorMessage = commonResponse?.getMessage() ?: "Login failed. Please try again."
                        _loginResult.value = LoginState.Error(errorMessage)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, AppErrorResponse::class.java)
                    val errorMessage = errorResponse?.getMessage() ?: "Error: ${response.code()} - Unknown error"

                    _loginResult.value = LoginState.Error(errorMessage)
                }
            } catch (e: IOException) {
                _loginResult.value = LoginState.Error("Network error: Please check your internet connection.")
            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorMessage, AppErrorResponse::class.java)
                _loginResult.value = LoginState.Error(errorResponse?.getMessage() ?: "HTTP Error: ${e.code()}")
            } catch (e: Exception) {
                _loginResult.value = LoginState.Error("An unexpected error occurred: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}

// Still nested here as it's specific to Login
sealed class LoginState {
    object Loading : LoginState()
    data class Success(val data: LoginResponse?) : LoginState()
    data class Error(val message: String) : LoginState()
}