package com.project.smartmunimji;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.project.smartmunimji.databinding.ActivityViewAllProductsBinding;
import com.project.smartmunimji.model.Seller;

import java.util.ArrayList;
import java.util.List;

public class ViewAllProductsActivity extends AppCompatActivity {

    private ActivityViewAllProductsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewAllProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Mock sellers
        List<Seller> sellers = new ArrayList<>();
        sellers.add(new Seller("1", "TechMart"));
        sellers.add(new Seller("2", "HomeGoods"));
        sellers.add(new Seller("3", "Book Nook"));

        SellerAdapter adapter = new SellerAdapter(sellers, seller -> {
            Toast.makeText(this, "Showing products for " + seller.getName(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, SellerProductsActivity.class);
            intent.putExtra("SELLER_ID", seller.getId());
            intent.putExtra("SELLER_NAME", seller.getName());
            startActivity(intent);
        });
        binding.sellersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.sellersRecyclerView.setAdapter(adapter);
    }
}