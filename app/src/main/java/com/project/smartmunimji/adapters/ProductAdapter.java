// app/src/main/java/com/project/smartmunimji/adapters/ProductAdapter.java

package com.project.smartmunimji.adapters; // Corrected package name

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.project.smartmunimji.R; // Ensure R.color is accessible
import com.project.smartmunimji.databinding.ItemProductBinding;
import com.project.smartmunimji.model.response.ProductListResponse; // Use the API response model

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<ProductListResponse> products; // Use ProductListResponse
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(ProductListResponse product); // Listener takes ProductListResponse
        void onClaimWarrantyClick(ProductListResponse product); // New listener for claim button
    }

    public ProductAdapter(List<ProductListResponse> products, OnProductClickListener listener) {
        this.products = products;
        this.listener = listener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        ProductListResponse product = products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void updateProducts(List<ProductListResponse> newProducts) {
        this.products = newProducts;
        notifyDataSetChanged();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ItemProductBinding binding;

        ProductViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // Set listener for the entire item view (e.g., for showing details)
            // binding.getRoot().setOnClickListener(v -> listener.onProductClick(products.get(getAdapterPosition())));

            // Set listener specifically for the claim warranty button
            binding.claimWarrantyButton.setOnClickListener(v -> {
                // Ensure position is valid
                int adapterPosition = getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onClaimWarrantyClick(products.get(adapterPosition));
                }
            });
        }

        void bind(ProductListResponse product) {
            binding.productName.setText(product.getProductName());
            binding.shopName.setText(product.getShopName()); // Assuming you have shopName TextView
            binding.purchaseDate.setText("Purchased: " + product.getDateOfPurchase());
            binding.warrantyEndDate.setText("Warranty Ends: " + product.getWarrantyValidUntil());

            // Set warranty status text and color
            if (product.isWarrantyEligible()) {
                binding.warrantyStatus.setText("Eligible for Warranty");
                binding.warrantyStatus.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.green_warranty));
                binding.claimWarrantyButton.setVisibility(View.VISIBLE); // Show button if eligible
                binding.claimWarrantyButton.setEnabled(true);
            } else {
                binding.warrantyStatus.setText("Warranty Expired");
                binding.warrantyStatus.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.red_warranty_expired));
                binding.claimWarrantyButton.setVisibility(View.GONE); // Hide button if not eligible
                binding.claimWarrantyButton.setEnabled(false);
            }

            // Hide/show days remaining if not applicable or show a message for expired
            if (product.isWarrantyEligible() && product.getDaysRemaining() > 0) {
                binding.daysRemaining.setText(product.getDaysRemaining() + " days remaining");
                binding.daysRemaining.setVisibility(View.VISIBLE);
            } else if (!product.isWarrantyEligible()) {
                binding.daysRemaining.setText("Warranty expired on " + product.getWarrantyValidUntil());
                binding.daysRemaining.setVisibility(View.VISIBLE);
            } else {
                binding.daysRemaining.setVisibility(View.GONE);
            }
        }
    }
}