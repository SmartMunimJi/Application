// app/src/main/java/com/project/smartmunimji/RegistrationActivity.kt

package com.project.smartmunimji

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.project.smartmunimji.databinding.ActivityRegistrationBinding
import com.project.smartmunimji.model.request.RegisterCustomerRequest
import com.project.smartmunimji.network.RetrofitClient
import com.project.smartmunimji.repository.AppRepository
import com.project.smartmunimji.utils.TokenManager
import com.project.smartmunimji.viewmodel.RegistrationState
import com.project.smartmunimji.viewmodel.RegistrationViewModel
import com.project.smartmunimji.viewmodel.RegistrationViewModelFactory

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var registrationViewModel: RegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize TokenManager (even if not saving tokens here, it's a dependency for RetrofitClient)
        val tokenManager = TokenManager(this)

        // Initialize ViewModel
        val apiService = RetrofitClient.getApiService(tokenManager)
        val repository = AppRepository(apiService)
        val viewModelFactory = RegistrationViewModelFactory(repository)
        registrationViewModel = ViewModelProvider(this, viewModelFactory).get(RegistrationViewModel::class.java)

        // Observe registrationResult LiveData
        registrationViewModel.registrationResult.observe(this) { state ->
            when (state) {
                is RegistrationState.Loading -> {
                    binding.registerButton.isEnabled = false
                    Toast.makeText(this, "Registering...", Toast.LENGTH_SHORT).show()
                }
                is RegistrationState.Success -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                    // Navigate to LoginActivity after successful registration
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish() // Finish this activity so user can't go back easily
                }
                is RegistrationState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                    binding.registerButton.isEnabled = true
                }
            }
        }

        // Observe isLoading LiveData
        registrationViewModel.isLoading.observe(this) { isLoading ->
            binding.registerButton.isEnabled = !isLoading
            // You can add a ProgressBar visibility toggle here if you have one
        }

        // Handle Terms and Conditions click
        binding.termsText.setOnClickListener {
            startActivity(Intent(this, TermsAndConditionsActivity::class.java))
        }

        binding.registerButton.setOnClickListener {
            val name = binding.nameInput.text.toString().trim()
            val email = binding.emailInput.text.toString().trim()
            val phone = binding.phoneInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()
            val address = binding.addressInput.text.toString().trim()
            val agreedToTerms = binding.termsCheckbox.isChecked

            // Client-side validation
            if (!agreedToTerms) {
                Toast.makeText(this, R.string.agree_to_terms, Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Stop execution
            }

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Stop execution
            }

            // Basic email validation
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Password length validation (e.g., min 6 characters as per backend)
            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create registration request object
            val request = RegisterCustomerRequest(name, email, password, phone, address)
            registrationViewModel.registerCustomer(request)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}