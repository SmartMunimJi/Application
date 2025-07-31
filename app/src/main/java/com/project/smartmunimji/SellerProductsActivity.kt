// app/src/main/java/com/project/smartmunimji/SellerProductsActivity.kt

package com.project.smartmunimji

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.smartmunimji.adapters.ProductAdapter
import com.project.smartmunimji.databinding.ActivitySellerProductsBinding
import com.project.smartmunimji.model.response.ProductListResponse
import com.project.smartmunimji.network.RetrofitClient
import com.project.smartmunimji.repository.AppRepository
import com.project.smartmunimji.utils.TokenManager
import com.project.smartmunimji.viewmodel.Status
import com.project.smartmunimji.viewmodel.ViewAllProductsViewModel
import com.project.smartmunimji.viewmodel.ViewAllProductsViewModelFactory
import java.util.ArrayList

class SellerProductsActivity : AppCompatActivity(), ProductAdapter.OnProductClickListener {

    private lateinit var binding: ActivitySellerProductsBinding
    private lateinit var productsViewModel: ViewAllProductsViewModel
    private lateinit var productAdapter: ProductAdapter
    private lateinit var tokenManager: TokenManager

    private var selectedSellerName: String? = null // Seller name passed via Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        selectedSellerName = intent.getStringExtra("SELLER_NAME")
        binding.sellerNameText.text = selectedSellerName?.let {
            getString(R.string.products_from_seller, it)
        } ?: getString(R.string.products_from_seller, "Unknown Seller")


        tokenManager = TokenManager(this)

        // Initialize ViewModel (re-using ViewAllProductsViewModel)
        val apiService = RetrofitClient.getApiService(tokenManager)
        val repository = AppRepository(apiService)
        val viewModelFactory = ViewAllProductsViewModelFactory(repository)
        productsViewModel = ViewModelProvider(this, viewModelFactory).get(ViewAllProductsViewModel::class.java)

        // Setup RecyclerView
        productAdapter = ProductAdapter(emptyList(), this)
        binding.sellerProductsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@SellerProductsActivity)
            adapter = productAdapter
        }

        // --- Observe LiveData from ViewModel ---

        productsViewModel.products.observe(this) { allProducts ->
                val filteredProducts = if (selectedSellerName != null) {
            allProducts.filter { it.getShopName() == selectedSellerName }
        } else {
            allProducts
        }
            productAdapter.updateProducts(filteredProducts)
            binding.emptyStateText.visibility = if (filteredProducts.isEmpty()) View.VISIBLE else View.GONE
        }

        productsViewModel.fetchStatus.observe(this) { status ->
                when (status) {
            is Status.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.sellerProductsRecyclerView.visibility = View.GONE
                binding.emptyStateText.visibility = View.GONE
            }
            is Status.Success -> {
                binding.progressBar.visibility = View.GONE
                binding.sellerProductsRecyclerView.visibility = View.VISIBLE
                // Empty state handled by products observer
            }
            is Status.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.sellerProductsRecyclerView.visibility = View.GONE
                binding.emptyStateText.visibility = View.VISIBLE
                binding.emptyStateText.text = status.message
                Toast.makeText(this, status.message, Toast.LENGTH_LONG).show()
            }
        }
        }

        // --- Initial Data Fetch ---
        productsViewModel.fetchCustomerProducts()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    // --- ProductAdapter.OnProductClickListener implementations ---
    override fun onProductClick(product: ProductListResponse) {
        Toast.makeText(this, "Clicked on: ${product.getProductName()}", Toast.LENGTH_SHORT).show()
        // Implement navigation to a product detail page if desired
    }

    override fun onClaimWarrantyClick(product: ProductListResponse) {
        if (product.isWarrantyEligible()) {
            val intent = Intent(this, ClaimWarrantyActivity::class.java).apply {
                putExtra("REGISTERED_PRODUCT_ID", product.getRegisteredProductId())
                putExtra("PRODUCT_NAME", product.getProductName())
            }
            startActivity(intent)
        } else {
            Toast.makeText(this, "Warranty for ${product.getProductName()} is expired.", Toast.LENGTH_SHORT).show()
        }
    }
}