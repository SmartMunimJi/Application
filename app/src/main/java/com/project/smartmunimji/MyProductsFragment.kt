// app/src/main/java/com/project/smartmunimji/MyProductsFragment.kt

package com.project.smartmunimji

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.smartmunimji.databinding.FragmentMyProductsBinding

class MyProductsFragment : Fragment() {

    private var _binding: FragmentMyProductsBinding? = null
    private val binding get() = _binding!!

            override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addProductButton.setOnClickListener {
            startActivity(Intent(context, AddProductActivity::class.java))
        }

        binding.viewAllButton.setOnClickListener {
            startActivity(Intent(context, ViewAllProductsActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}