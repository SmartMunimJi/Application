// app/src/main/java/com/project/smartmunimji/viewmodel/ProductRegistrationState.kt
package com.project.smartmunimji.viewmodel

import com.project.smartmunimji.model.response.ProductListResponse

sealed class ProductRegistrationState {
    object Loading : ProductRegistrationState()
    data class Success(val message: String, val registeredProduct: ProductListResponse?) : ProductRegistrationState()
    data class Error(val message: String) : ProductRegistrationState()
}