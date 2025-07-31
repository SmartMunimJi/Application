// app/src/main/java/com/project/smartmunimji/viewmodel/RegistrationViewModel.kt

package com.project.smartmunimji.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.project.smartmunimji.model.AppErrorResponse
import com.project.smartmunimji.model.request.RegisterCustomerRequest
import com.project.smartmunimji.model.response.CommonResponse
import com.project.smartmunimji.repository.AppRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class RegistrationViewModel(private val repository: AppRepository) : ViewModel() {

    private val _registrationResult = MutableLiveData<RegistrationState>()
    val registrationResult: LiveData<RegistrationState> = _registrationResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun registerCustomer(request: RegisterCustomerRequest) {
        _isLoading.value = true
        _registrationResult.value = RegistrationState.Loading

        viewModelScope.launch {
            try {
                val response = repository.registerCustomer(request)

                if (response.isSuccessful) {
                    val commonResponse = response.body()
                    if (commonResponse != null && commonResponse.getStatus() == "success") {
                        _registrationResult.value = RegistrationState.Success("Registration successful! Please log in.")
                    } else {
                        val errorMessage = commonResponse?.getMessage() ?: "Registration failed. Please try again."
                        _registrationResult.value = RegistrationState.Error(errorMessage)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, AppErrorResponse::class.java)
                    val errorMessage = errorResponse?.getMessage() ?: "Error: ${response.code()} - Unknown error"
                    _registrationResult.value = RegistrationState.Error(errorMessage)
                }
            } catch (e: IOException) {
                _registrationResult.value = RegistrationState.Error("Network error: Please check your internet connection.")
            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorMessage, AppErrorResponse::class.java)
                _registrationResult.value = RegistrationState.Error(errorResponse?.getMessage() ?: "HTTP Error: ${e.code()}")
            } catch (e: Exception) {
                _registrationResult.value = RegistrationState.Error("An unexpected error occurred: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}

// Sealed class to represent different states of the registration operation
sealed class RegistrationState {
    object Loading : RegistrationState()
    data class Success(val message: String) : RegistrationState()
    data class Error(val message: String) : RegistrationState()
}