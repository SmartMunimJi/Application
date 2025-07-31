// app/src/main/java/com/project/smartmunimji/network/ApiService.kt

package com.project.smartmunimji.network

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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    // Auth Endpoints
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<CommonResponse<LoginResponse>>

    @POST("auth/register/customer")
    suspend fun registerCustomer(@Body request: RegisterCustomerRequest): Response<CommonResponse<Void>> // Void as no data expected in success

    // Customer Endpoints
    @GET("customer/profile")
    suspend fun getCustomerProfile(): Response<CommonResponse<CustomerProfileResponse>>

    @PUT("customer/profile")
   suspend fun updateCustomerProfile(@Body request: UpdateProfileRequest): Response<CommonResponse<Void>>

    @GET("customer/sellers")
    suspend fun getActiveSellers(): Response<CommonResponse<List<SellerListResponse>>> // List of sellers

    @POST("customer/products/register")
    suspend fun registerProduct(@Body request: AddProductRequest): Response<CommonResponse<ProductListResponse>> // Returns data like registeredProductId, productName, warrantyValidUntil

    @GET("customer/products")
    suspend fun getCustomerProducts(): Response<CommonResponse<List<ProductListResponse>>> // List of products

    @POST("customer/claims")
    suspend fun submitClaim(@Body request: ClaimWarrantyRequest): Response<CommonResponse<Void>>

    @GET("customer/claims")
    suspend fun getCustomerClaims(): Response<CommonResponse<List<ClaimListResponse>>>

    // Placeholder for a single claim detail if needed (customer side)
    @GET("customer/claims/{claimId}")
    suspend fun getCustomerClaimDetails(@Path("claimId") claimId: Int): Response<CommonResponse<ClaimListResponse>>
}