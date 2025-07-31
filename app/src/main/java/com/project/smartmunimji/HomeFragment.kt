// app/src/main/java/com/project/smartmunimji/HomeFragment.kt

package com.project.smartmunimji

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.project.smartmunimji.databinding.FragmentHomeBinding
import com.project.smartmunimji.network.RetrofitClient
import com.project.smartmunimji.repository.AppRepository
import com.project.smartmunimji.utils.TokenManager
import com.project.smartmunimji.viewmodel.HomeViewModel
import com.project.smartmunimji.viewmodel.HomeViewModelFactory
import com.project.smartmunimji.viewmodel.Status

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var tokenManager: TokenManager

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tokenManager = TokenManager(requireContext())

        // Initialize ViewModel
        val apiService = RetrofitClient.getApiService(tokenManager)
        val repository = AppRepository(apiService)
        val viewModelFactory = HomeViewModelFactory(repository)
        homeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        // --- Observe LiveData from ViewModel ---

        // Observe customer name
        homeViewModel.customerName.observe(viewLifecycleOwner) { name ->
                binding.welcomeMessage.text = getString(R.string.welcome_message_format, name)
        }

        // Observe fetch status for profile (to show loading/error for welcome message)
        homeViewModel.fetchStatus.observe(viewLifecycleOwner) { status ->
                when (status) {
            is Status.Loading -> {
                binding.homeProgressBar.visibility = View.VISIBLE // Show specific progress bar
                binding.homeContentGroup.visibility = View.GONE // Hide content
                binding.welcomeMessage.text = getString(R.string.welcome_message_loading)
            }
            is Status.Success -> {
                binding.homeProgressBar.visibility = View.GONE
                binding.homeContentGroup.visibility = View.VISIBLE // Show content
            }
            is Status.Error -> {
                binding.homeProgressBar.visibility = View.GONE
                Toast.makeText(context, status.message, Toast.LENGTH_LONG).show()
                binding.homeContentGroup.visibility = View.VISIBLE // Still show content, with fallback name
                binding.welcomeMessage.text = getString(R.string.welcome_message_default)
            }
        }
        }

        // --- Set up navigation buttons ---
        binding.registerProductButton.setOnClickListener {
            startActivity(Intent(context, AddProductActivity::class.java))
        }

        binding.viewMyProductsButton.setOnClickListener {
            startActivity(Intent(context, ViewAllProductsActivity::class.java))
        }

        binding.viewMyClaimsButton.setOnClickListener {
            (activity as? MainActivity)?.navigateToFragment(R.id.nav_my_claims)
        }

        binding.manageProfileButton.setOnClickListener {
            (activity as? MainActivity)?.navigateToFragment(R.id.nav_profile)
        }
    }

    override fun onResume() {
        super.onResume()
        // Re-fetch profile on resume to ensure name is up-to-date (e.g., after profile update)
        homeViewModel.fetchCustomerProfile()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}