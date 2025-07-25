package com.project.smartmunimji;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.project.smartmunimji.databinding.ItemOfferBinding;
import com.project.smartmunimji.model.Offer;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {

    private List<Offer> offers;
    private OnOfferClickListener listener;

    public interface OnOfferClickListener {
        void onOfferClick(Offer offer);
    }

    public OfferAdapter(List<Offer> offers, OnOfferClickListener listener) {
        this.offers = offers;
        this.listener = listener;
    }

    @Override
    public OfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemOfferBinding binding = ItemOfferBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OfferViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(OfferViewHolder holder, int position) {
        Offer offer = offers.get(position);
        holder.bind(offer);
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    class OfferViewHolder extends RecyclerView.ViewHolder {
        private ItemOfferBinding binding;

        OfferViewHolder(ItemOfferBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> listener.onOfferClick(offers.get(getAdapterPosition())));
        }

        void bind(Offer offer) {
            binding.offerTitle.setText(offer.getTitle());
            binding.offerDescription.setText(offer.getDescription());
            binding.sellerName.setText(offer.getSellerName());
        }
    }
}