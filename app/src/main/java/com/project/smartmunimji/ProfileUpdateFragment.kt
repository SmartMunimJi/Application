package com.project.smartmunimji

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.project.smartmunimji.databinding.FragmentProfileUpdateBinding

class ProfileUpdateFragment : Fragment() {

    private var _binding: FragmentProfileUpdateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mock pre-filled user data
        binding.nameInput.setText("John Doe")
        binding.emailInput.setText("john.doe@example.com")
        binding.phoneInput.setText("+1234567890")
        binding.addressInput.setText("123 Main St, City")

        binding.saveChangesButton.setOnClickListener {
            val name = binding.nameInput.text.toString()
            val email = binding.emailInput.text.toString()
            val phone = binding.phoneInput.text.toString()
            val address = binding.addressInput.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty() && address.isNotEmpty()) {
                Toast.makeText(context, "Saving profile changes...", Toast.LENGTH_SHORT).show()
                // Mock save delay
                Handler(Looper.getMainLooper()).postDelayed({
                    Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_LONG).show()
                    parentFragmentManager.popBackStack()
                }, 2000)
            } else {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}