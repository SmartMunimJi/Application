// app/src/main/java/com/project/smartmunimji/ClaimWarrantyActivity.kt

package com.project.smartmunimji

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.project.smartmunimji.databinding.ActivityClaimWarrantyBinding
import com.project.smartmunimji.network.RetrofitClient
import com.project.smartmunimji.repository.AppRepository
import com.project.smartmunimji.utils.TokenManager
import com.project.smartmunimji.viewmodel.ClaimSubmissionState
import com.project.smartmunimji.viewmodel.ClaimWarrantyViewModel
import com.project.smartmunimji.viewmodel.ClaimWarrantyViewModelFactory

class ClaimWarrantyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClaimWarrantyBinding
    private lateinit var claimWarrantyViewModel: ClaimWarrantyViewModel
    private lateinit var tokenManager: TokenManager

    private var registeredProductId: Int = -1 // To hold the ID of the product for which claim is submitted
    private var productName: String = "" // To display product name in UI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClaimWarrantyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Retrieve product data from Intent extras
        registeredProductId = intent.getIntExtra("REGISTERED_PRODUCT_ID", -1)
        productName = intent.getStringExtra("PRODUCT_NAME") ?: "Selected Product"

        // Display product name
        binding.productNameText.text = getString(R.string.claiming_for_product, productName)

        // Initialize TokenManager
        tokenManager = TokenManager(this)

        // Initialize ViewModel
        val apiService = RetrofitClient.getApiService(tokenManager)
        val repository = AppRepository(apiService)
        val viewModelFactory = ClaimWarrantyViewModelFactory(repository)
        claimWarrantyViewModel = ViewModelProvider(this, viewModelFactory).get(ClaimWarrantyViewModel::class.java)

        // --- Observe LiveData from ViewModel ---

        // Observe claim submission status
        claimWarrantyViewModel.claimSubmissionStatus.observe(this) { state ->
            when (state) {
                is ClaimSubmissionState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.submitClaimButton.isEnabled = false
                    Toast.makeText(this, "Submitting claim...", Toast.LENGTH_SHORT).show()
                }
                is ClaimSubmissionState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                    finish() // Go back to the previous activity (e.g., ViewAllProductsActivity)
                }
                is ClaimSubmissionState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                    binding.submitClaimButton.isEnabled = true
                }
            }
        }

        // Observe general isLoading (for submit button)
        claimWarrantyViewModel.isLoading.observe(this) { isLoading ->
            binding.submitClaimButton.isEnabled = !isLoading
        }

        // --- UI Listeners ---
        binding.submitClaimButton.setOnClickListener {
            val issueDescription = binding.issueDescriptionInput.text.toString().trim()

            if (registeredProductId == -1) {
                Toast.makeText(this, "Error: Product ID not provided for claim.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (issueDescription.isEmpty()) {
                Toast.makeText(this, "Please describe the issue.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Trigger claim submission in ViewModel
            claimWarrantyViewModel.submitClaim(registeredProductId, issueDescription)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}