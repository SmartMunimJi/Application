// app/src/main/java/com/project/smartmunimji/viewmodel/Status.kt
package com.project.smartmunimji.viewmodel

sealed class Status {
    object Loading : Status()
    object Success : Status()
    data class Error(val message: String) : Status()
}