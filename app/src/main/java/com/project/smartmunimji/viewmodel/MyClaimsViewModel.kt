// app/src/main/java/com/project/smartmunimji/viewmodel/MyClaimsViewModel.kt

package com.project.smartmunimji.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.project.smartmunimji.model.AppErrorResponse
import com.project.smartmunimji.model.WarrantyClaim
import com.project.smartmunimji.model.response.ClaimListResponse
import com.project.smartmunimji.model.response.CommonResponse
import com.project.smartmunimji.repository.AppRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MyClaimsViewModel(private val repository: AppRepository) : ViewModel() {

    // Make claims LiveData public for observation
    private val _claims = MutableLiveData<List<ClaimListResponse>>()
    val claims: LiveData<List<ClaimListResponse>> = _claims // No 'private set' or default private setter

    private val _fetchStatus = MutableLiveData<Status>()
    val fetchStatus: LiveData<Status> = _fetchStatus

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchCustomerClaims() {
        _isLoading.value = true
        _fetchStatus.value = Status.Loading

        viewModelScope.launch {
            try {
                val response = repository.getCustomerClaims()

                if (response.isSuccessful) {
                    val commonResponse = response.body()
                    if (commonResponse != null && commonResponse.getStatus() == "success") {
                        _claims.value = commonResponse.getData() ?: emptyList()
                        _fetchStatus.value = Status.Success
                    } else {
                        val errorMessage = commonResponse?.getMessage() ?: "Failed to load claims."
                        _fetchStatus.value = Status.Error(errorMessage)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, AppErrorResponse::class.java)
                    val errorMessage = errorResponse?.getMessage() ?: "Error: ${response.code()} - Failed to fetch claims."
                    _fetchStatus.value = Status.Error(errorMessage)
                }
            } catch (e: IOException) {
                _fetchStatus.value = Status.Error("Network error: Please check your internet connection.")
            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorMessage, AppErrorResponse::class.java)
                _fetchStatus.value = Status.Error(errorResponse?.getMessage() ?: "HTTP Error: ${e.code()}")
            } catch (e: Exception) {
                _fetchStatus.value = Status.Error("An unexpected error occurred: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}