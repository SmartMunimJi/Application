package com.project.smartmunimji;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.project.smartmunimji.databinding.FragmentOffersBinding;
import com.project.smartmunimji.model.Offer;

import java.util.ArrayList;
import java.util.List;

public class OffersFragment extends Fragment {

    private FragmentOffersBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOffersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Mock offers
        List<Offer> offers = new ArrayList<>();
        offers.add(new Offer("1", "Laptop Discount", "20% off on all laptops", "TechMart"));
        offers.add(new Offer("2", "Blender Sale", "Buy one get one free", "HomeGoods"));
        offers.add(new Offer("3", "Book Offer", "Free shipping on books", "Book Nook"));
        offers.add(new Offer("4", "Mouse Deal", "10% off on mice", "TechMart"));
        offers.add(new Offer("5", "Toaster Offer", "15% off on toasters", "HomeGoods"));

        OfferAdapter adapter = new OfferAdapter(offers, offer -> {
            Toast.makeText(getContext(), "Offer clicked: " + offer.getTitle(), Toast.LENGTH_SHORT).show();
        });
        binding.offersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.offersRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}