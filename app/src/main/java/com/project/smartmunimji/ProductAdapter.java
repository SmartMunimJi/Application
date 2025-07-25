package com.project.smartmunimji;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.project.smartmunimji.databinding.ItemProductBinding;
import com.project.smartmunimji.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> products;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public ProductAdapter(List<Product> products, OnProductClickListener listener) {
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
        Product product = products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ItemProductBinding binding;

        ProductViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.claimWarrantyButton.setOnClickListener(v -> listener.onProductClick(products.get(getAdapterPosition())));
        }

        void bind(Product product) {
            binding.productName.setText(product.getName());
            binding.purchaseDate.setText("Purchased: " + product.getPurchaseDate());
            binding.warrantyEndDate.setText("Warranty Ends: " + product.getWarrantyEndDate());
            binding.warrantyStatus.setText(product.isWarrantyValid() ? "Eligible for Warranty" : "Warranty Expired");
            binding.warrantyStatus.setTextColor(product.isWarrantyValid() ?
                    binding.getRoot().getContext().getResources().getColor(R.color.green_warranty) :
                    binding.getRoot().getContext().getResources().getColor(R.color.red_warranty_expired));
            if (product.getClaimStatus() != null) {
                binding.warrantyStatus.setText("Claim Status: " + product.getClaimStatus());
            }
        }
    }
}