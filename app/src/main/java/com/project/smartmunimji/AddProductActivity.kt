// app/src/main/java/com/project/smartmunimji/AddProductActivity.kt

package com.project.smartmunimji

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.project.smartmunimji.databinding.ActivityAddProductBinding
import com.project.smartmunimji.model.response.SellerListResponse
import com.project.smartmunimji.network.RetrofitClient
import com.project.smartmunimji.repository.AppRepository
import com.project.smartmunimji.utils.TokenManager
import com.project.smartmunimji.viewmodel.AddProductViewModel
import com.project.smartmunimji.viewmodel.AddProductViewModelFactory
import com.project.smartmunimji.viewmodel.ProductRegistrationState
import com.project.smartmunimji.viewmodel.Status
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProductBinding
    private lateinit var addProductViewModel: AddProductViewModel
    private lateinit var tokenManager: TokenManager

    private var selectedSellerId: Int = -1 // To store the selected seller's ID
    private var activeSellers: List<SellerListResponse> = emptyList() // Store fetched sellers

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tokenManager = TokenManager(this)

        // Initialize ViewModel
        val apiService = RetrofitClient.getApiService(tokenManager)
        val repository = AppRepository(apiService)
        val viewModelFactory = AddProductViewModelFactory(repository)
        addProductViewModel = ViewModelProvider(this, viewModelFactory).get(AddProductViewModel::class.java)

        // --- Observe LiveData from ViewModel ---

        // Observe seller list for spinner
        addProductViewModel.sellers.observe(this) { sellers ->
                activeSellers = sellers
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sellers.map { it.getShopName() })
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.sellerSpinner.adapter = adapter

            if (sellers.isNotEmpty()) {
                binding.sellerSpinner.setSelection(0) // Select the first item by default
                selectedSellerId = sellers[0].getSellerId() // Set initial selected ID
            } else {
                // If no sellers, ensure selectedSellerId is invalid and disable submit
                selectedSellerId = -1
                Toast.makeText(this, "No active sellers available. Cannot register product.", Toast.LENGTH_LONG).show()
                binding.submitButton.isEnabled = false
            }
        }

        // Observe seller fetch status
        addProductViewModel.sellerFetchStatus.observe(this) { status ->
                when (status) {
            is Status.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.submitButton.isEnabled = false
                binding.sellerSpinner.isEnabled = false
            }
            is Status.Success -> {
                binding.progressBar.visibility = View.GONE
                binding.sellerSpinner.isEnabled = true
                // Enable submit button only if sellers are available
                binding.submitButton.isEnabled = activeSellers.isNotEmpty()
            }
            is Status.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.sellerSpinner.isEnabled = false
                binding.submitButton.isEnabled = false
                Toast.makeText(this, status.message, Toast.LENGTH_LONG).show()
            }
        }
        }

        // Observe product registration result
        addProductViewModel.productRegistrationResult.observe(this) { state ->
                when (state) {
            is ProductRegistrationState.Loading -> {
                binding.submitButton.isEnabled = false
                Toast.makeText(this, "Registering product...", Toast.LENGTH_SHORT).show()
            }
            is ProductRegistrationState.Success -> {
                Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                finish() // Go back to previous activity (MyProductsFragment host)
            }
            is ProductRegistrationState.Error -> {
                Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                binding.submitButton.isEnabled = true
            }
        }
        }

        // Observe general isLoading (for submit button)
        addProductViewModel.isLoading.observe(this) { isLoading ->
                binding.submitButton.isEnabled = !isLoading && activeSellers.isNotEmpty()
        }

        // --- UI Listeners ---

        // Spinner item selection listener
        binding.sellerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedSellerId = activeSellers[position].getSellerId()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedSellerId = -1
            }
        }

        // Date input click listener to show DatePickerDialog
        binding.dateInput.setOnClickListener {
            showDatePickerDialog()
        }

        binding.submitButton.setOnClickListener {
            val orderId = binding.orderIdInput.text.toString().trim()
            val date = binding.dateInput.text.toString().trim() // This will be in YYYY-MM-DD format

            if (selectedSellerId == -1) {
                Toast.makeText(this, "Please select a seller", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (orderId.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Trigger product registration in ViewModel
            addProductViewModel.registerProduct(selectedSellerId, orderId, date)
        }

        // --- Initial Data Fetch ---
        addProductViewModel.fetchActiveSellers()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                        val selectedDate = Calendar.getInstance()
                        selectedDate.set(selectedYear, selectedMonth, selectedDay)
                        // Format date to YYYY-MM-DD for backend
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        binding.dateInput.setText(dateFormat.format(selectedDate.time))
                },
                year,
                month,
                day
        )
        datePickerDialog.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}