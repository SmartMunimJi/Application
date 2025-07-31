// app/src/main/java/com/project/smartmunimji/utils/TokenManager.kt

package com.project.smartmunimji.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class TokenManager(context: Context) {

    // Using EncryptedSharedPreferences for more secure storage of sensitive data
    // In a real app, this should be initialized once, perhaps in Application class
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val prefs = EncryptedSharedPreferences.create(
        "auth_prefs", // Name of your shared preferences file
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val KEY_JWT_TOKEN = "jwt_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_ROLE = "user_role"
    }

    fun saveAuthToken(token: String, userId: Int, role: String) {
        prefs.edit().putString(KEY_JWT_TOKEN, token).apply()
        prefs.edit().putInt(KEY_USER_ID, userId).apply()
        prefs.edit().putString(KEY_USER_ROLE, role).apply()
    }

    fun getJwtToken(): String? {
        return prefs.getString(KEY_JWT_TOKEN, null)
    }

    fun getUserId(): Int {
        return prefs.getInt(KEY_USER_ID, -1) // -1 as default for not found
    }

    fun getUserRole(): String? {
        return prefs.getString(KEY_USER_ROLE, null)
    }

    fun clearAuthToken() {
        prefs.edit().remove(KEY_JWT_TOKEN).apply()
        prefs.edit().remove(KEY_USER_ID).apply()
        prefs.edit().remove(KEY_USER_ROLE).apply()
    }

    fun isAuthenticated(): Boolean {
        return getJwtToken() != null && getUserId() != -1 && getUserRole() != null
    }
}