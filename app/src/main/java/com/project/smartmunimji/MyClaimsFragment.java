// app/src/main/java/com/project/smartmunimji/MyClaimsFragment.java

package com.project.smartmunimji;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.smartmunimji.adapters.WarrantyClaimAdapter;
import com.project.smartmunimji.databinding.FragmentMyClaimsBinding;
import com.project.smartmunimji.model.WarrantyClaim; // Correct model
import com.project.smartmunimji.model.response.ClaimListResponse; // Correct response model
import com.project.smartmunimji.network.RetrofitClient;
import com.project.smartmunimji.repository.AppRepository;
import com.project.smartmunimji.utils.TokenManager;
import com.project.smartmunimji.viewmodel.MyClaimsViewModel;
import com.project.smartmunimji.viewmodel.MyClaimsViewModelFactory;
import com.project.smartmunimji.viewmodel.Status; // Import Status sealed class

import java.util.ArrayList;
import java.util.List;

public class MyClaimsFragment extends Fragment {

    private FragmentMyClaimsBinding binding;
    private MyClaimsViewModel myClaimsViewModel;
    private WarrantyClaimAdapter claimAdapter;
    private TokenManager tokenManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMyClaimsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        tokenManager = new TokenManager(requireContext());

        // Initialize ViewModel
        // FIX 1: getApiService is non-static and must be called on an instance of RetrofitClient
        // RetrofitClient.INSTANCE.getApiService(tokenManager) in Kotlin, but in Java it's RetrofitClient.INSTANCE.getApiService(tokenManager)
        // Or, more correctly for Java interop with Kotlin objects, use the object's instance directly
        AppRepository repository = new AppRepository(RetrofitClient.INSTANCE.getApiService(tokenManager)); // CORRECTED
        MyClaimsViewModelFactory factory = new MyClaimsViewModelFactory(repository);
        myClaimsViewModel = new ViewModelProvider(this, factory).get(MyClaimsViewModel.class);

        // Setup RecyclerView
        binding.claimsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        claimAdapter = new WarrantyClaimAdapter(new ArrayList<>());
        binding.claimsRecyclerView.setAdapter(claimAdapter);

        // --- Observe LiveData from ViewModel ---

        // FIX 2: claims is val in Kotlin ViewModel, which becomes a public getter `getClaims()` in Java.
        // It's `getClaims()` and `getFetchStatus()`
        myClaimsViewModel.getClaims().observe(getViewLifecycleOwner(), claims -> { // CORRECTED
            claimAdapter.updateClaims(claims);
            binding.emptyStateText.setVisibility(claims.isEmpty() ? View.VISIBLE : View.GONE);
        });

        // Observe fetch status
        myClaimsViewModel.getFetchStatus().observe(getViewLifecycleOwner(), status -> { // CORRECTED
            if (status instanceof Status.Loading) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.claimsRecyclerView.setVisibility(View.GONE);
                binding.emptyStateText.setVisibility(View.GONE);
            } else if (status instanceof Status.Success) {
                binding.progressBar.setVisibility(View.GONE);
                binding.claimsRecyclerView.setVisibility(View.VISIBLE);
            } else if (status instanceof Status.Error) {
                binding.progressBar.setVisibility(View.GONE);
                binding.claimsRecyclerView.setVisibility(View.GONE);
                binding.emptyStateText.setVisibility(View.VISIBLE);
                binding.emptyStateText.setText(((Status.Error) status).getMessage());
                Toast.makeText(getContext(), ((Status.Error) status).getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // --- Initial Data Fetch ---
        if (savedInstanceState == null) {
            myClaimsViewModel.fetchCustomerClaims();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        myClaimsViewModel.fetchCustomerClaims();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}