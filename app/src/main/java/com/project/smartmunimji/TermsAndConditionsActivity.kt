// app/src/main/java/com/project/smartmunimji/TermsAndConditionsActivity.kt

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

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.termsText.text = getString(R.string.terms_and_conditions)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}