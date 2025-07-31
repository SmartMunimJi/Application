// app/src/main/java/com/project/smartmunimji/ProfileUpdateFragment.kt

package com.project.smartmunimji

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.project.smartmunimji.databinding.FragmentProfileUpdateBinding
import com.project.smartmunimji.network.RetrofitClient
import com.project.smartmunimji.repository.AppRepository
import com.project.smartmunimji.utils.TokenManager
import com.project.smartmunimji.viewmodel.ProfileViewModel
import com.project.smartmunimji.viewmodel.ProfileViewModelFactory
import com.project.smartmunimji.viewmodel.UpdateStatus // Import the UpdateStatus sealed class

class ProfileUpdateFragment : Fragment() {

    private var _binding: FragmentProfileUpdateBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize TokenManager for RetrofitClient
        val tokenManager = TokenManager(requireContext())
        val apiService = RetrofitClient.getApiService(tokenManager)
        val repository = AppRepository(apiService)
        val viewModelFactory = ProfileViewModelFactory(repository)
        profileViewModel = ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)

        // Retrieve arguments passed from ProfileFragment
        val name = arguments?.getString("name")
        val email = arguments?.getString("email")
        val phoneNumber = arguments?.getString("phoneNumber")
        val address = arguments?.getString("address")

        // Pre-fill input fields with current data
        binding.nameInput.setText(name)
        binding.emailInput.setText(email) // Email is read-only
        binding.phoneInput.setText(phoneNumber) // Phone is read-only
        binding.addressInput.setText(address)

        // Email and Phone are read-only as per web UI specification
        binding.emailInput.isEnabled = false
        binding.phoneInput.isEnabled = false

        // Observe profile update status
        profileViewModel.profileUpdateStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is UpdateStatus.Loading -> {
                    binding.saveChangesButton.isEnabled = false
                    Toast.makeText(context, "Saving changes...", Toast.LENGTH_SHORT).show()
                }
                is UpdateStatus.Success -> {
                    Toast.makeText(context, status.message, Toast.LENGTH_LONG).show()
                    parentFragmentManager.popBackStack() // Go back to ProfileFragment on success
                }
                is UpdateStatus.Error -> {
                    Toast.makeText(context, status.message, Toast.LENGTH_LONG).show()
                    binding.saveChangesButton.isEnabled = true
                }
                is UpdateStatus.Idle -> {
                    // Do nothing for Idle state, it's just the initial state
                    binding.saveChangesButton.isEnabled = true
                }
            }
        }

        // Observe isLoading LiveData for general progress indication
        profileViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.saveChangesButton.isEnabled = !isLoading
            // You can also toggle visibility of a ProgressBar here if you have one in the layout
        }


        binding.saveChangesButton.setOnClickListener {
            val updatedName = binding.nameInput.text.toString().trim()
            val updatedAddress = binding.addressInput.text.toString().trim()

            // Basic validation
            if (updatedName.isEmpty() || updatedAddress.isEmpty()) {
                Toast.makeText(context, "Name and Address cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Trigger update in ViewModel
            profileViewModel.updateCustomerProfile(updatedName, updatedAddress)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}