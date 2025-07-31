// app/src/main/java/com/project/smartmunimji/viewmodel/AddProductViewModel.kt

package com.project.smartmunimji.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.project.smartmunimji.model.AppErrorResponse
import com.project.smartmunimji.model.request.AddProductRequest
import com.project.smartmunimji.model.response.ProductListResponse
import com.project.smartmunimji.model.response.SellerListResponse
import com.project.smartmunimji.model.response.CommonResponse
import com.project.smartmunimji.repository.AppRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
// --- IMPORTANT: Removed internal sealed class definitions, now importing ---

class AddProductViewModel(private val repository: AppRepository) : ViewModel() {

    private val _sellers = MutableLiveData<List<SellerListResponse>>()
    val sellers: LiveData<List<SellerListResponse>> = _sellers

    private val _sellerFetchStatus = MutableLiveData<Status>()
    val sellerFetchStatus: LiveData<Status> = _sellerFetchStatus

    private val _productRegistrationResult = MutableLiveData<ProductRegistrationState>()
    val productRegistrationResult: LiveData<ProductRegistrationState> = _productRegistrationResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchActiveSellers() {
        _isLoading.value = true
        _sellerFetchStatus.value = Status.Loading

        viewModelScope.launch {
            try {
                val response = repository.getActiveSellers()
                if (response.isSuccessful) {
                    val commonResponse = response.body()
                    if (commonResponse != null && commonResponse.getStatus() == "success") {
                        _sellers.value = commonResponse.getData() ?: emptyList()
                        _sellerFetchStatus.value = Status.Success
                    } else {
                        val errorMessage = commonResponse?.getMessage() ?: "Failed to load sellers."
                        _sellerFetchStatus.value = Status.Error(errorMessage)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, AppErrorResponse::class.java)
                    val errorMessage = errorResponse?.getMessage() ?: "Error: ${response.code()} - Failed to fetch sellers"
                    _sellerFetchStatus.value = Status.Error(errorMessage)
                }
            } catch (e: IOException) {
                _sellerFetchStatus.value = Status.Error("Network error: Please check your internet connection.")
            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorMessage, AppErrorResponse::class.java)
                _sellerFetchStatus.value = Status.Error(errorResponse?.getMessage() ?: "HTTP Error: ${e.code()}")
            } catch (e: Exception) {
                _sellerFetchStatus.value = Status.Error("An unexpected error occurred: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun registerProduct(sellerId: Int, orderId: String, purchaseDate: String) {
        _isLoading.value = true
        _productRegistrationResult.value = ProductRegistrationState.Loading

        viewModelScope.launch {
            try {
                val request = AddProductRequest(sellerId, orderId, purchaseDate)
                val response = repository.registerProduct(request)

                if (response.isSuccessful) {
                    val commonResponse = response.body()
                    if (commonResponse != null && commonResponse.getStatus() == "success") {
                        _productRegistrationResult.value = ProductRegistrationState.Success(
                            commonResponse.getMessage() ?: "Product registered successfully!",
                            commonResponse.getData()
                        )
                    } else {
                        val errorMessage = commonResponse?.getMessage() ?: "Product registration failed."
                        _productRegistrationResult.value = ProductRegistrationState.Error(errorMessage)
                    }
                } else {
                    if (response.code() == 424) {
                        val errorBody = response.errorBody()?.string()
                        val errorResponse = Gson().fromJson(errorBody, AppErrorResponse::class.java)
                        val errorMessage = errorResponse?.getMessage() ?: "Validation failed: Could not register product with seller."
                        _productRegistrationResult.value = ProductRegistrationState.Error(errorMessage)
                    } else {
                        val errorBody = response.errorBody()?.string()
                        val errorResponse = Gson().fromJson(errorBody, AppErrorResponse::class.java)
                        val errorMessage = errorResponse?.getMessage() ?: "Error: ${response.code()} - Product registration failed."
                        _productRegistrationResult.value = ProductRegistrationState.Error(errorMessage)
                    }
                }
            } catch (e: IOException) {
                _productRegistrationResult.value = ProductRegistrationState.Error("Network error: Please check your internet connection.")
            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorMessage, AppErrorResponse::class.java)
                _productRegistrationResult.value = ProductRegistrationState.Error(errorResponse?.getMessage() ?: "HTTP Error: ${e.code()}")
            } catch (e: Exception) {
                _productRegistrationResult.value = ProductRegistrationState.Error("An unexpected error occurred: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}