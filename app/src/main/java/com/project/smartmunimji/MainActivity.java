package com.project.smartmunimji;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.smartmunimji.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
            binding.bottomNavigation.setSelectedItemId(R.id.nav_home);
        }

        // Bottom navigation listener using title comparison
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            String toastMessage = "";
            String itemTitle = item.getTitle().toString();

            if (itemTitle.equals(getString(R.string.home))) {
                selectedFragment = new HomeFragment();
                toastMessage = "Navigating to Home";
            } else if (itemTitle.equals(getString(R.string.my_products))) {
                Toast.makeText(this, "Navigating to My Products", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ViewAllProductsActivity.class));
                return true;
            } else if (itemTitle.equals(getString(R.string.offers))) {
                selectedFragment = new OffersFragment();
                toastMessage = "Navigating to Offers";
            } else if (itemTitle.equals(getString(R.string.profile))) {
                selectedFragment = new ProfileFragment();
                toastMessage = "Navigating to Profile";
            }

            if (selectedFragment != null) {
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });
    }
}