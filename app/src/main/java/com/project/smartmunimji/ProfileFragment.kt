// app/src/main/java/com/project/smartmunimji/ProfileFragment.kt

package com.project.smartmunimji

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.project.smartmunimji.databinding.FragmentProfileBinding
import com.project.smartmunimji.network.RetrofitClient
import com.project.smartmunimji.repository.AppRepository
import com.project.smartmunimji.utils.TokenManager
import com.project.smartmunimji.viewmodel.ProfileStatus
import com.project.smartmunimji.viewmodel.ProfileViewModel
import com.project.smartmunimji.viewmodel.ProfileViewModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tokenManager = TokenManager(requireContext())

        // Initialize ViewModel
        val apiService = RetrofitClient.getApiService(tokenManager)
        val repository = AppRepository(apiService)
        val viewModelFactory = ProfileViewModelFactory(repository)
        profileViewModel = ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)

        // Observe profile data
        profileViewModel.profile.observe(viewLifecycleOwner) { profile ->
            profile?.let {
                binding.nameText.text = getString(R.string.name_format, it.getName())
                binding.emailText.text = getString(R.string.email_format, it.getEmail())
                binding.phoneText.text = getString(R.string.phone_number_format, it.getPhoneNumber())
                binding.addressText.text = getString(R.string.address_format, it.getAddress())
            }
        }

        // Observe profile fetch status
        profileViewModel.profileFetchStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is ProfileStatus.Loading -> {
                    // Show loading indicator
                    binding.progressBar.visibility = View.VISIBLE
                    binding.profileContentGroup.visibility = View.GONE // Hide content
                }
                is ProfileStatus.Success -> {
                    // Hide loading, show content
                    binding.progressBar.visibility = View.GONE
                    binding.profileContentGroup.visibility = View.VISIBLE
                }
                is ProfileStatus.Error -> {
                    // Hide loading, show error message
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, status.message, Toast.LENGTH_LONG).show()
                    binding.profileContentGroup.visibility = View.GONE
                }
            }
        }

        // Trigger profile fetch when fragment is created
        if (profileViewModel.profile.value == null) { // Only fetch if not already loaded
            profileViewModel.fetchCustomerProfile()
        }


        binding.editProfileButton.setOnClickListener {
            // Pass current profile data to ProfileUpdateFragment
            val currentProfile = profileViewModel.profile.value
            val fragment = ProfileUpdateFragment().apply {
                arguments = Bundle().apply {
                    putString("name", currentProfile?.getName())
                    putString("email", currentProfile?.getEmail())
                    putString("phoneNumber", currentProfile?.getPhoneNumber())
                    putString("address", currentProfile?.getAddress())
                }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null) // Allows going back to ProfileFragment
                .commit()
        }

        binding.logoutButton.setOnClickListener {
            tokenManager.clearAuthToken() // Clear token
            Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear back stack
            startActivity(intent)
            activity?.finish() // Finish MainActivity
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        // Re-fetch profile data when returning to this fragment (e.g., after an update)
        profileViewModel.fetchCustomerProfile()
    }
}