package com.project.smartmunimji

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.project.smartmunimji.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mock user data
        binding.nameText.text = getString(R.string.name, "John Doe")
        binding.emailText.text = getString(R.string.email, "john.doe@example.com")
        binding.phoneText.text = getString(R.string.phone_number, "+1234567890")
        binding.addressText.text = getString(R.string.address, "123 Main St, City")

        binding.editProfileButton.setOnClickListener {
            Toast.makeText(context, "Edit Profile clicked", Toast.LENGTH_SHORT).show()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProfileUpdateFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.logoutButton.setOnClickListener {
            Toast.makeText(context, "Logging out...", Toast.LENGTH_SHORT).show()
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}