// app/src/main/java/com/project/smartmunimji/MainActivity.kt

package com.project.smartmunimji

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.smartmunimji.databinding.ActivityMainBinding
import com.project.smartmunimji.utils.TokenManager // Import TokenManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        // Note: MainActivity toolbar typically doesn't need a back button if it's the main hub
        // supportActionBar?.setDisplayHomeAsUpEnabled(true) // Removed for typical main activity behavior

        tokenManager = TokenManager(this) // Initialize TokenManager

        // Initial check for authentication, redirect if needed (though SplashActivity handles primary)
        if (!tokenManager.isAuthenticated()) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            return
        }

        // Set default fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
            binding.bottomNavigation.selectedItemId = R.id.nav_home // Ensure correct ID
        }

        // Bottom navigation listener using item IDs for robustness
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            var toastMessage = ""

            when (item.itemId) {
                R.id.nav_home -> {
                    selectedFragment = HomeFragment()
                    toastMessage = "Navigating to Home"
                }
                R.id.nav_my_products -> {
                    selectedFragment = MyProductsFragment()
                    toastMessage = "Navigating to My Products"
                }
                R.id.nav_my_claims -> { // Renamed from R.string.my_claims
                    selectedFragment = MyClaimsFragment()
                    toastMessage = "Navigating to My Claims"
                }
                // R.id.nav_offers -> { /* REMOVED OFFERS TAB */ }
                R.id.nav_profile -> {
                    selectedFragment = ProfileFragment()
                    toastMessage = "Navigating to Profile"
                }
            }

            selectedFragment?.let {
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, it)
                    .commit()
            }
            true
        }
    }

    // Helper method for fragments to navigate through bottom navigation
    fun navigateToFragment(menuItemId: Int) {
        binding.bottomNavigation.selectedItemId = menuItemId
    }

    // No need for onSupportNavigateUp here if no toolbar back button
    // override fun onSupportNavigateUp(): Boolean {
    //     onBackPressed()
    //     return true
    // }
}