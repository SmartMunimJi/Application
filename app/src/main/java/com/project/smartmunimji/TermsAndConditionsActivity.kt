package com.project.smartmunimji

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.smartmunimji.databinding.ActivityTermsAndConditionsBinding

class TermsAndConditionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTermsAndConditionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsAndConditionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set T&C text
        binding.termsText.text = getString(R.string.terms_and_conditions)

        // OK button to return
        binding.okButton.setOnClickListener {
            finish()
        }
    }
}