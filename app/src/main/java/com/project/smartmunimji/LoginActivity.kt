// app/src/main/java/com/project/smartmunimji/LoginActivity.kt

package com.project.smartmunimji

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.project.smartmunimji.databinding.ActivityLoginBinding
import com.project.smartmunimji.network.RetrofitClient
import com.project.smartmunimji.repository.AppRepository
import com.project.smartmunimji.utils.TokenManager
import com.project.smartmunimji.viewmodel.LoginState
import com.project.smartmunimji.viewmodel.LoginViewModel
import com.project.smartmunimji.viewmodel.LoginViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tokenManager = TokenManager(this)

        // Initialize ViewModel using ViewModelProvider.Factory
        val apiService = RetrofitClient.getApiService(tokenManager)
        val repository = AppRepository(apiService)
        val viewModelFactory = LoginViewModelFactory(repository)
        loginViewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)


        // Observe loginResult LiveData
        loginViewModel.loginResult.observe(this) { state ->
            when (state) {
                is LoginState.Loading -> {
                    binding.loginButton.isEnabled = false
                    // Optionally show a ProgressBar if you have one in your layout
                    Toast.makeText(this, "Logging in...", Toast.LENGTH_SHORT).show()
                }
                is LoginState.Success -> {
                    // Login successful, save token and navigate
                    state.data?.let {
                        tokenManager.saveAuthToken(it.getJwtToken(), it.getUserId(), it.getRole())
                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } ?: run {
                        Toast.makeText(this, "Login successful but no data received.", Toast.LENGTH_SHORT).show()
                        binding.loginButton.isEnabled = true
                    }
                }
                is LoginState.Error -> {
                    // Show error message
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                    binding.loginButton.isEnabled = true
                }
            }
        }

        // Observe isLoading LiveData (for a separate loading indicator like ProgressBar)
        loginViewModel.isLoading.observe(this) { isLoading ->
            binding.loginButton.isEnabled = !isLoading
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE // Ensure progressBar is handled
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginViewModel.login(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        binding.registerText.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

        binding.forgotPasswordText.setOnClickListener {
            Toast.makeText(this, "Forgot Password clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}