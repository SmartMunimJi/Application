// app/src/main/java/com/project/smartmunimji/viewmodel/ViewAllProductsViewModel.kt

package com.project.smartmunimji.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.project.smartmunimji.model.AppErrorResponse
// --- IMPORTANT: No longer importing 'Product', only 'ProductListResponse' from response package
import com.project.smartmunimji.model.response.ProductListResponse
import com.project.smartmunimji.model.response.CommonResponse
import com.project.smartmunimji.repository.AppRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
// --- IMPORTANT: Removed internal sealed class definitions, now importing ---

class ViewAllProductsViewModel(private val repository: AppRepository) : ViewModel() {

    private val _products = MutableLiveData<List<ProductListResponse>>()
    val products: LiveData<List<ProductListResponse>> = _products

    private val _fetchStatus = MutableLiveData<Status>()
    val fetchStatus: LiveData<Status> = _fetchStatus

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchCustomerProducts() {
        _isLoading.value = true
        _fetchStatus.value = Status.Loading // Set loading state for UI

        viewModelScope.launch {
            try {
                val response = repository.getCustomerProducts()

                if (response.isSuccessful) {
                    val commonResponse = response.body()
                    if (commonResponse != null && commonResponse.getStatus() == "success") {
                        _products.value = commonResponse.getData() ?: emptyList()
                        _fetchStatus.value = Status.Success // Indicate success
                    } else {
                        val errorMessage = commonResponse?.getMessage() ?: "Failed to load products."
                        _fetchStatus.value = Status.Error(errorMessage)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, AppErrorResponse::class.java)
                    val errorMessage = errorResponse?.getMessage() ?: "Error: ${response.code()} - Failed to fetch products."
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
                _isLoading.value = false // Always set loading to false
            }
        }
    }
}