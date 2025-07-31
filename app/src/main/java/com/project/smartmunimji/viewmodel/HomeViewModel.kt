// app/src/main/java/com/project/smartmunimji/viewmodel/HomeViewModel.kt

package com.project.smartmunimji.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.project.smartmunimji.model.AppErrorResponse
import com.project.smartmunimji.model.response.CustomerProfileResponse
import com.project.smartmunimji.repository.AppRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class HomeViewModel(private val repository: AppRepository) : ViewModel() {

    private val _customerName = MutableLiveData<String>()
    val customerName: LiveData<String> = _customerName

    private val _fetchStatus = MutableLiveData<Status>()
    val fetchStatus: LiveData<Status> = _fetchStatus // To indicate loading/error for profile fetch

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        // Fetch profile automatically when ViewModel is created
        fetchCustomerProfile()
    }

    fun fetchCustomerProfile() {
        _isLoading.value = true
        _fetchStatus.value = Status.Loading

        viewModelScope.launch {
            try {
                val response = repository.getCustomerProfile()
                if (response.isSuccessful) {
                    val commonResponse = response.body()
                    if (commonResponse != null && commonResponse.getStatus() == "success") {
                        _customerName.value = commonResponse.getData()?.getName() ?: "User"
                        _fetchStatus.value = Status.Success
                    } else {
                        val errorMessage = commonResponse?.getMessage() ?: "Failed to fetch profile for welcome message."
                        _fetchStatus.value = Status.Error(errorMessage)
                        _customerName.value = "User" // Fallback
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, AppErrorResponse::class.java)
                    val errorMessage = errorResponse?.getMessage() ?: "Error: ${response.code()} - Failed to fetch profile."
                    _fetchStatus.value = Status.Error(errorMessage)
                    _customerName.value = "User" // Fallback
                }
            } catch (e: IOException) {
                _fetchStatus.value = Status.Error("Network error: Check connection for welcome message.")
                _customerName.value = "User" // Fallback
            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorMessage, AppErrorResponse::class.java)
                _fetchStatus.value = Status.Error(errorResponse?.getMessage() ?: "HTTP Error: ${e.code()}")
                _customerName.value = "User" // Fallback
            } catch (e: Exception) {
                _fetchStatus.value = Status.Error("An unexpected error occurred: ${e.message}")
                _customerName.value = "User" // Fallback
            } finally {
                _isLoading.value = false
            }
        }
    }
}