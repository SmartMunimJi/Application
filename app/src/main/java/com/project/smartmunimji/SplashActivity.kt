package com.project.smartmunimji

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {

    private val TAG = "SplashActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_screen)

        val icon = findViewById<ImageView>(R.id.splash_icon)
        val appName = findViewById<TextView>(R.id.app_name)
        val wordStore = findViewById<TextView>(R.id.word_store)
        val wordVerify = findViewById<TextView>(R.id.word_verify)
        val wordTrust = findViewById<TextView>(R.id.word_trust)

        Log.d(TAG, "Icon: $icon, AppName: $appName, Store: $wordStore, Verify: $wordVerify, Trust: $wordTrust")

        if (wordTrust == null) {
            Log.e(TAG, "word_trust TextView is null!")
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val popupAnim = AnimationUtils.loadAnimation(this, R.anim.popup)
        val fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val slideInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)


        GlobalScope.launch(Dispatchers.Main) {
            try {

                Log.d(TAG, "Animating icon")
                icon?.visibility = ImageView.VISIBLE
                icon?.startAnimation(popupAnim)

                delay(800)
                Log.d(TAG, "Animating app name")
                appName?.visibility = TextView.VISIBLE
                appName?.startAnimation(fadeInAnim)

                delay(800) // Start at 1200ms
                Log.d(TAG, "Animating Store")
                wordStore?.visibility = TextView.VISIBLE
                wordStore?.startAnimation(slideInAnim)

                delay(800) // Start at 1600ms
                Log.d(TAG, "Animating Verify")
                wordVerify?.visibility = TextView.VISIBLE
                wordVerify?.startAnimation(slideInAnim)

                delay(800) // Start at 2000ms
                Log.d(TAG, "Animating Trust")
                wordTrust.visibility = TextView.VISIBLE
                wordTrust.startAnimation(slideInAnim)

                // Navigate to LoginActivity after total animation
                delay(800)
                Log.d(TAG, "Navigating to LoginActivity")
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            } catch (e: Exception) {
                Log.e(TAG, "Error during animation: ${e.message}", e)
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }
        }
    }
}