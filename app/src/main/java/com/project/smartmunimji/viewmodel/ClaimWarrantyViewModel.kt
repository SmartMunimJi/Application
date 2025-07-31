// app/src/main/java/com/project/smartmunimji/viewmodel/ClaimWarrantyViewModel.kt

package com.project.smartmunimji.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.project.smartmunimji.model.AppErrorResponse
import com.project.smartmunimji.model.request.ClaimWarrantyRequest
import com.project.smartmunimji.model.response.CommonResponse
import com.project.smartmunimji.repository.AppRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ClaimWarrantyViewModel(private val repository: AppRepository) : ViewModel() {

    private val _claimSubmissionStatus = MutableLiveData<ClaimSubmissionState>()
    val claimSubmissionStatus: LiveData<ClaimSubmissionState> = _claimSubmissionStatus

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun submitClaim(registeredProductId: Int, issueDescription: String) {
        _isLoading.value = true
        _claimSubmissionStatus.value = ClaimSubmissionState.Loading

        viewModelScope.launch {
            try {
                val request = ClaimWarrantyRequest(registeredProductId, issueDescription)
                val response = repository.submitClaim(request)

                if (response.isSuccessful) {
                    val commonResponse = response.body()
                    if (commonResponse != null && commonResponse.getStatus() == "success") {
                        _claimSubmissionStatus.value = ClaimSubmissionState.Success(
                            commonResponse.getMessage() ?: "Claim submitted successfully!"
                        )
                    } else {
                        val errorMessage = commonResponse?.getMessage() ?: "Claim submission failed."
                        _claimSubmissionStatus.value = ClaimSubmissionState.Error(errorMessage)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, AppErrorResponse::class.java)
                    val errorMessage = errorResponse?.getMessage() ?: "Error: ${response.code()} - Claim submission failed."
                    _claimSubmissionStatus.value = ClaimSubmissionState.Error(errorMessage)
                }
            } catch (e: IOException) {
                _claimSubmissionStatus.value = ClaimSubmissionState.Error("Network error: Please check your internet connection.")
            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorMessage, AppErrorResponse::class.java)
                _claimSubmissionStatus.value = ClaimSubmissionState.Error(errorResponse?.getMessage() ?: "HTTP Error: ${e.code()}")
            } catch (e: Exception) {
                _claimSubmissionStatus.value = ClaimSubmissionState.Error("An unexpected error occurred: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}

// Sealed class for claim submission state
sealed class ClaimSubmissionState {
    object Loading : ClaimSubmissionState()
    data class Success(val message: String) : ClaimSubmissionState()
    data class Error(val message: String) : ClaimSubmissionState()
}