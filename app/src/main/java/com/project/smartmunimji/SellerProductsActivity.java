package com.project.smartmunimji;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.project.smartmunimji.databinding.ActivitySellerProductsBinding;
import com.project.smartmunimji.model.Product;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SellerProductsActivity extends AppCompatActivity {

    private ActivitySellerProductsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySellerProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String sellerId = getIntent().getStringExtra("SELLER_ID");
        String sellerName = getIntent().getStringExtra("SELLER_NAME");
        binding.sellerProductsLabel.setText(getString(R.string.products_from_seller, sellerName));

        // Mock products based on seller
        List<Product> products = new ArrayList<>();
        if ("1".equals(sellerId)) { // TechMart
            products.add(new Product("Laptop", "2023-01-15", "2024-01-15", true));
            products.add(new Product("Mouse", "2023-06-10", "2023-12-10", false));
        } else if ("2".equals(sellerId)) { // HomeGoods
            products.add(new Product("Blender", "2023-03-20", "2024-03-20", true));
            products.add(new Product("Toaster", "2022-11-05", "2023-11-05", false));
        } else { // Book Nook
            products.add(new Product("Book", "2023-04-01", "2024-04-01", true));
        }

        ProductAdapter adapter = new ProductAdapter(products, product -> {
            Toast.makeText(this, "Claim Warranty clicked for " + product.getName(), Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Claim Warranty");
            android.widget.EditText input = new android.widget.EditText(this);
            input.setHint(R.string.issue_description);
            builder.setView(input);
            builder.setPositiveButton(R.string.submit, (dialog, which) -> {
                Toast.makeText(this, "Claim submitted. Seller notified!", Toast.LENGTH_LONG).show();
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        });
        binding.productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.productsRecyclerView.setAdapter(adapter);
    }
}