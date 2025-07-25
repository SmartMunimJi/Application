package com.project.smartmunimji;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.project.smartmunimji.databinding.ItemSellerBinding;
import com.project.smartmunimji.model.Seller;

import java.util.List;

public class SellerAdapter extends RecyclerView.Adapter<SellerAdapter.SellerViewHolder> {

    private List<Seller> sellers;
    private OnSellerClickListener listener;

    public interface OnSellerClickListener {
        void onSellerClick(Seller seller);
    }

    public SellerAdapter(List<Seller> sellers, OnSellerClickListener listener) {
        this.sellers = sellers;
        this.listener = listener;
    }

    @Override
    public SellerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSellerBinding binding = ItemSellerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SellerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(SellerViewHolder holder, int position) {
        Seller seller = sellers.get(position);
        holder.bind(seller);
    }

    @Override
    public int getItemCount() {
        return sellers.size();
    }

    class SellerViewHolder extends RecyclerView.ViewHolder {
        private ItemSellerBinding binding;

        SellerViewHolder(ItemSellerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> listener.onSellerClick(sellers.get(getAdapterPosition())));
        }

        void bind(Seller seller) {
            binding.sellerName.setText(seller.getName());
        }
    }
}