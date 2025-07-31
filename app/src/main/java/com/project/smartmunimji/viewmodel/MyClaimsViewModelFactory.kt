// app/src/main/java/com/project/smartmunimji/viewmodel/MyClaimsViewModelFactory.kt

package com.project.smartmunimji.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.smartmunimji.repository.AppRepository

class MyClaimsViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyClaimsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyClaimsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}