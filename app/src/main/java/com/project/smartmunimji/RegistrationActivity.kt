package com.project.smartmunimji

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.smartmunimji.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle Terms and Conditions click to start T&C activity
        binding.termsText.setOnClickListener {
            startActivity(Intent(this, TermsAndConditionsActivity::class.java))
        }

        binding.registerButton.setOnClickListener {
            val name = binding.nameInput.text.toString()
            val email = binding.emailInput.text.toString()
            val phone = binding.phoneInput.text.toString()
            val password = binding.passwordInput.text.toString()
            val address = binding.addressInput.text.toString()
            val agreedToTerms = binding.termsCheckbox.isChecked

            if (!agreedToTerms) {
                Toast.makeText(this, R.string.agree_to_terms, Toast.LENGTH_SHORT).show()
            } else if (name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty() && password.isNotEmpty() && address.isNotEmpty()) {
                Toast.makeText(this, "Registering user...", Toast.LENGTH_SHORT).show()
                // Mock registration delay
                Handler(Looper.getMainLooper()).postDelayed({
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }, 2000)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}