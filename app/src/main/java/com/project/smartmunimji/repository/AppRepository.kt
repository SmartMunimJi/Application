// app/src/main/java/com/project/smartmunimji/repository/AppRepository.kt

package com.project.smartmunimji.repository

import com.project.smartmunimji.model.request.AddProductRequest
import com.project.smartmunimji.model.request.ClaimWarrantyRequest
import com.project.smartmunimji.model.request.LoginRequest
import com.project.smartmunimji.model.request.RegisterCustomerRequest
import com.project.smartmunimji.model.request.UpdateProfileRequest // This will be a new Java class later
import com.project.smartmunimji.model.response.ClaimListResponse
import com.project.smartmunimji.model.response.CustomerProfileResponse
import com.project.smartmunimji.model.response.LoginResponse
import com.project.smartmunimji.model.response.ProductListResponse
import com.project.smartmunimji.model.response.SellerListResponse
import com.project.smartmunimji.model.response.CommonResponse
import com.project.smartmunimji.network.ApiService
import retrofit2.Response

// This class acts as a single point of truth for data operations.
// ViewModels will interact with this Repository, not directly with ApiService.
class AppRepository(private val apiService: ApiService) {

    // Auth Operations
    suspend fun login(request: LoginRequest): Response<CommonResponse<LoginResponse>> {
        return apiService.login(request)
    }

    suspend fun registerCustomer(request: RegisterCustomerRequest): Response<CommonResponse<Void>> {
        return apiService.registerCustomer(request)
    }

    // Customer Profile Operations
    suspend fun getCustomerProfile(): Response<CommonResponse<CustomerProfileResponse>> {
        return apiService.getCustomerProfile()
    }

    suspend fun updateCustomerProfile(request: UpdateProfileRequest): Response<CommonResponse<Void>> {
        return apiService.updateCustomerProfile(request)
    }

    // Product Registration Operations
    suspend fun getActiveSellers(): Response<CommonResponse<List<SellerListResponse>>> {
        return apiService.getActiveSellers()
    }

    suspend fun registerProduct(request: AddProductRequest): Response<CommonResponse<ProductListResponse>> {
        return apiService.registerProduct(request)
    }

    suspend fun getCustomerProducts(): Response<CommonResponse<List<ProductListResponse>>> {
        return apiService.getCustomerProducts()
    }

    // Claim Operations
    suspend fun submitClaim(request: ClaimWarrantyRequest): Response<CommonResponse<Void>> {
        return apiService.submitClaim(request)
    }

    suspend fun getCustomerClaims(): Response<CommonResponse<List<ClaimListResponse>>> {
        return apiService.getCustomerClaims()
    }
}