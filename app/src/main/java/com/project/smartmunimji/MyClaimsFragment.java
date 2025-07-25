package com.project.smartmunimji;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.project.smartmunimji.databinding.FragmentMyClaimsBinding;
import com.project.smartmunimji.model.Product;

import java.util.ArrayList;
import java.util.List;

public class MyClaimsFragment extends Fragment {

    private FragmentMyClaimsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyClaimsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Mock claims
        List<Product> claims = new ArrayList<>();
        claims.add(new Product("Laptop", "2023-01-15", "2024-01-15", true, "Screen issue", "Requested"));
        claims.add(new Product("Blender", "2023-03-20", "2024-03-20", true, "Motor failure", "Accepted"));
        claims.add(new Product("Mouse", "2023-06-10", "2023-12-10", false, "Button not working", "Denied"));

        ProductAdapter adapter = new ProductAdapter(claims, product -> {
            Toast.makeText(getContext(), "Claim clicked for " + product.getName(), Toast.LENGTH_SHORT).show();
        });
        binding.claimsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.claimsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}