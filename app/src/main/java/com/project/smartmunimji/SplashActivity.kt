// app/src/main/java/com/project/smartmunimji/SplashActivity.kt

package com.project.smartmunimji

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.project.smartmunimji.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private val TAG = "SplashActivity"
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        tokenManager = TokenManager(this) // Initialize TokenManager

        val icon = findViewById<ImageView>(R.id.splash_icon)
        val appName = findViewById<TextView>(R.id.app_name_splash) // Ensure this ID exists in your layout
        val wordStore = findViewById<TextView>(R.id.word_store)
        val wordVerify = findViewById<TextView>(R.id.word_verify)
        val wordTrust = findViewById<TextView>(R.id.word_trust)

        // Null checks for views, crucial for robustness
        if (icon == null || appName == null || wordStore == null || wordVerify == null || wordTrust == null) {
            Log.e(TAG, "One or more TextViews/ImageViews are null! Skipping animation.")
            // Immediately navigate if views are not found
            navigateToNextActivity()
            return
        }

        val popupAnim = AnimationUtils.loadAnimation(this, R.anim.popup)
        val fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val slideInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)

        GlobalScope.launch(Dispatchers.Main) {
            try {
                icon.visibility = ImageView.VISIBLE
                icon.startAnimation(popupAnim)

                delay(800)
                appName.visibility = TextView.VISIBLE
                appName.startAnimation(fadeInAnim)

                delay(800)
                wordStore.visibility = TextView.VISIBLE
                wordStore.startAnimation(slideInAnim)

                delay(800)
                wordVerify.visibility = TextView.VISIBLE
                wordVerify.startAnimation(slideInAnim)

                delay(800)
                wordTrust.visibility = TextView.VISIBLE
                wordTrust.startAnimation(slideInAnim)

                delay(800) // Final delay before navigating
                navigateToNextActivity()

            } catch (e: Exception) {
                Log.e(TAG, "Error during animation: ${e.message}", e)
                navigateToNextActivity() // Navigate even if animation fails
            }
        }
    }

    private fun navigateToNextActivity() {
        if (tokenManager.isAuthenticated()) {
            // User is already logged in, go to main activity
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // Not authenticated, go to login
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish() // Finish splash activity so it's not on the back stack
    }
}