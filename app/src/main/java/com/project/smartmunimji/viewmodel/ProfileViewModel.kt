// app/src/main/java/com/project/smartmunimji/viewmodel/ProfileViewModel.kt

package com.project.smartmunimji.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.project.smartmunimji.model.AppErrorResponse
import com.project.smartmunimji.model.request.UpdateProfileRequest
import com.project.smartmunimji.model.response.CustomerProfileResponse
import com.project.smartmunimji.repository.AppRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ProfileViewModel(private val repository: AppRepository) : ViewModel() {

    private val _profile = MutableLiveData<CustomerProfileResponse?>()
    val profile: LiveData<CustomerProfileResponse?> = _profile

    private val _profileFetchStatus = MutableLiveData<ProfileStatus>()
    val profileFetchStatus: LiveData<ProfileStatus> = _profileFetchStatus

    private val _profileUpdateStatus = MutableLiveData<UpdateStatus>()
    val profileUpdateStatus: LiveData<UpdateStatus> = _profileUpdateStatus

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchCustomerProfile() {
        _isLoading.value = true
        _profileFetchStatus.value = ProfileStatus.Loading

        viewModelScope.launch {
            try {
                val response = repository.getCustomerProfile()
                if (response.isSuccessful) {
                    val commonResponse = response.body()
                    if (commonResponse != null && commonResponse.getStatus() == "success") {
                        _profile.value = commonResponse.getData()
                        _profileFetchStatus.value = ProfileStatus.Success
                    } else {
                        val errorMessage = commonResponse?.getMessage() ?: "Failed to fetch profile."
                        _profileFetchStatus.value = ProfileStatus.Error(errorMessage)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, AppErrorResponse::class.java)
                    val errorMessage = errorResponse?.getMessage() ?: "Error: ${response.code()} - Failed to fetch profile."
                    _profileFetchStatus.value = ProfileStatus.Error(errorMessage)
                }
            } catch (e: IOException) {
                _profileFetchStatus.value = ProfileStatus.Error("Network error: Please check your internet connection.")
            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorMessage, AppErrorResponse::class.java)
                _profileFetchStatus.value = ProfileStatus.Error(errorResponse?.getMessage() ?: "HTTP Error: ${e.code()}")
            } catch (e: Exception) {
                _profileFetchStatus.value = ProfileStatus.Error("An unexpected error occurred: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateCustomerProfile(name: String, address: String) {
        _isLoading.value = true
        _profileUpdateStatus.value = UpdateStatus.Loading

        viewModelScope.launch {
            try {
                val request = UpdateProfileRequest(name, address)
                val response = repository.updateCustomerProfile(request)

                if (response.isSuccessful) {
                    val commonResponse = response.body()
                    if (commonResponse != null && commonResponse.getStatus() == "success") {
                        // Re-fetch profile to ensure UI has the latest data
                        fetchCustomerProfile()
                        _profileUpdateStatus.value = UpdateStatus.Success(commonResponse.getMessage() ?: "Profile updated successfully!")
                    } else {
                        val errorMessage = commonResponse?.getMessage() ?: "Profile update failed."
                        _profileUpdateStatus.value = UpdateStatus.Error(errorMessage)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, AppErrorResponse::class.java)
                    val errorMessage = errorResponse?.getMessage() ?: "Error: ${response.code()} - Profile update failed."
                    _profileUpdateStatus.value = UpdateStatus.Error(errorMessage)
                }
            } catch (e: IOException) {
                _profileUpdateStatus.value = UpdateStatus.Error("Network error: Please check your internet connection.")
            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorMessage, AppErrorResponse::class.java)
                _profileUpdateStatus.value = UpdateStatus.Error(errorResponse?.getMessage() ?: "HTTP Error: ${e.code()}")
            } catch (e: Exception) {
                _profileUpdateStatus.value = UpdateStatus.Error("An unexpected error occurred: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}

// Sealed classes for different states
sealed class ProfileStatus {
    object Loading : ProfileStatus()
    object Success : ProfileStatus()
    data class Error(val message: String) : ProfileStatus()
}

sealed class UpdateStatus {
    object Idle : UpdateStatus() // Initial state
    object Loading : UpdateStatus()
    data class Success(val message: String) : UpdateStatus()
    data class Error(val message: String) : UpdateStatus()
}