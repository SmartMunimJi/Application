// app/src/main/java/com/project/smartmunimji/ViewAllProductsActivity.kt

package com.project.smartmunimji

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.smartmunimji.adapters.ProductAdapter // Corrected import path for adapter
import com.project.smartmunimji.databinding.ActivityViewAllProductsBinding
import com.project.smartmunimji.model.response.ProductListResponse
import com.project.smartmunimji.network.RetrofitClient
import com.project.smartmunimji.repository.AppRepository
import com.project.smartmunimji.utils.TokenManager
import com.project.smartmunimji.viewmodel.Status
import com.project.smartmunimji.viewmodel.ViewAllProductsViewModel
import com.project.smartmunimji.viewmodel.ViewAllProductsViewModelFactory

class ViewAllProductsActivity : AppCompatActivity(), ProductAdapter.OnProductClickListener {

    private lateinit var binding: ActivityViewAllProductsBinding
    private lateinit var productsViewModel: ViewAllProductsViewModel
    private lateinit var productAdapter: ProductAdapter
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAllProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tokenManager = TokenManager(this)

        // Initialize ViewModel
        val apiService = RetrofitClient.getApiService(tokenManager)
        val repository = AppRepository(apiService)
        val viewModelFactory = ViewAllProductsViewModelFactory(repository)
        productsViewModel = ViewModelProvider(this, viewModelFactory).get(ViewAllProductsViewModel::class.java)

        // Setup RecyclerView
        productAdapter = ProductAdapter(emptyList(), this) // Pass 'this' as listener
        binding.productsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ViewAllProductsActivity)
            adapter = productAdapter
        }

        // --- Observe LiveData from ViewModel ---

        // Observe product list
        productsViewModel.products.observe(this) { products ->
                productAdapter.updateProducts(products) // Update adapter with new data
            binding.emptyStateText.visibility = if (products.isEmpty()) View.VISIBLE else View.GONE
        }

        // Observe fetch status
        productsViewModel.fetchStatus.observe(this) { status ->
                when (status) {
            is Status.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.productsRecyclerView.visibility = View.GONE
                binding.emptyStateText.visibility = View.GONE
            }
            is Status.Success -> {
                binding.progressBar.visibility = View.GONE
                binding.productsRecyclerView.visibility = View.VISIBLE
                // Empty state handled by products observer
            }
            is Status.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.productsRecyclerView.visibility = View.GONE
                binding.emptyStateText.visibility = View.VISIBLE // Show text for error
                binding.emptyStateText.text = status.message // Display error message
                Toast.makeText(this, status.message, Toast.LENGTH_LONG).show()
            }
        }
        }

        // --- Initial Data Fetch ---
        productsViewModel.fetchCustomerProducts()
    }

    override fun onResume() {
        super.onResume()
        // Re-fetch products when returning to this activity (e.g., after adding a new product)
        productsViewModel.fetchCustomerProducts()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    // --- ProductAdapter.OnProductClickListener implementations ---
    override fun onProductClick(product: ProductListResponse) {
        // This might be used for future product detail page navigation if needed
        Toast.makeText(this, "Clicked on: ${product.getProductName()}", Toast.LENGTH_SHORT).show()
    }

    override fun onClaimWarrantyClick(product: ProductListResponse) {
        if (product.isWarrantyEligible()) {
            val intent = Intent(this, ClaimWarrantyActivity::class.java).apply {
                putExtra("REGISTERED_PRODUCT_ID", product.getRegisteredProductId())
                putExtra("PRODUCT_NAME", product.getProductName())
                // You can pass more data if needed for the claim activity
            }
            startActivity(intent)
        } else {
            Toast.makeText(this, "Warranty for ${product.getProductName()} is expired.", Toast.LENGTH_SHORT).show()
        }
    }
}