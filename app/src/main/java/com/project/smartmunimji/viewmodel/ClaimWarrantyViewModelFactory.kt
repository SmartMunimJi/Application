// app/src/main/java/com/project/smartmunimji/viewmodel/ClaimWarrantyViewModelFactory.kt

package com.project.smartmunimji.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.smartmunimji.repository.AppRepository

class ClaimWarrantyViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClaimWarrantyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ClaimWarrantyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}