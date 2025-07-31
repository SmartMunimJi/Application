// app/src/main/java/com/project/smartmunimji/adapters/WarrantyClaimAdapter.java

package com.project.smartmunimji.adapters;

import android.graphics.Color; // For direct color setting
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.project.smartmunimji.R; // Ensure R.id for layout views
import com.project.smartmunimji.databinding.ItemWarrantyClaimBinding; // Using ViewBinding
import com.project.smartmunimji.model.response.ClaimListResponse; // Use the API response model

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class WarrantyClaimAdapter extends RecyclerView.Adapter<WarrantyClaimAdapter.ClaimViewHolder> {
    private List<ClaimListResponse> claimList; // List of ClaimListResponse
    // Optional listener if you want to make items clickable
    // private OnClaimClickListener listener;

    /*
    public interface OnClaimClickListener {
        void onClaimClick(ClaimListResponse claim);
    }
    */

    public WarrantyClaimAdapter(List<ClaimListResponse> claimList /*, OnClaimClickListener listener */) {
        this.claimList = claimList;
        // this.listener = listener;
    }

    @NonNull
    @Override
    public ClaimViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Use ViewBinding for inflating
        ItemWarrantyClaimBinding binding = ItemWarrantyClaimBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ClaimViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ClaimViewHolder holder, int position) {
        ClaimListResponse claim = claimList.get(position);
        holder.bind(claim);
    }

    @Override
    public int getItemCount() {
        return claimList.size();
    }

    /**
     * Updates the adapter's data set and refreshes the RecyclerView.
     * @param newClaims The new list of claims to display.
     */
    public void updateClaims(List<ClaimListResponse> newClaims) {
        this.claimList = newClaims;
        notifyDataSetChanged();
    }

    public static class ClaimViewHolder extends RecyclerView.ViewHolder {
        // Use ViewBinding to access views
        private final ItemWarrantyClaimBinding binding;

        public ClaimViewHolder(@NonNull ItemWarrantyClaimBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // If you need to make the entire item clickable
            /*
            binding.getRoot().setOnClickListener(v -> {
                int adapterPosition = getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION && listener != null) {
                    listener.onClaimClick(claimList.get(adapterPosition));
                }
            });
            */
        }

        public void bind(ClaimListResponse claim) {
            binding.claimId.setText("Claim ID: #" + claim.getClaimId());
            binding.productName.setText("Product: " + claim.getProductName());
            binding.issueDescription.setText("Issue: " + claim.getIssueDescription());

            // Format claimedAt date for display
            try {
                SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()); // Adjust if format differs
                SimpleDateFormat displayDateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
                String formattedDate = displayDateFormat.format(apiDateFormat.parse(claim.getClaimedAt()));
                binding.claimedAt.setText("Claimed: " + formattedDate);
            } catch (Exception e) {
                binding.claimedAt.setText("Claimed: " + claim.getClaimedAt()); // Fallback
            }

            binding.claimStatus.setText("Status: " + claim.getClaimStatus());

            // Set status color based on claimStatus
            int statusColor;
            switch (claim.getClaimStatus()) {
                case "ACCEPTED":
                case "RESOLVED":
                    statusColor = binding.getRoot().getContext().getResources().getColor(R.color.green_warranty);
                    break;
                case "DENIED":
                    statusColor = binding.getRoot().getContext().getResources().getColor(R.color.red_warranty_expired);
                    break;
                case "IN_PROGRESS":
                    statusColor = Color.parseColor("#007bff"); // A standard blue
                    break;
                case "REQUESTED":
                default:
                    statusColor = binding.getRoot().getContext().getResources().getColor(android.R.color.darker_gray);
                    break;
            }
            binding.claimStatus.setTextColor(statusColor);

            // Show seller response notes if available
            if (claim.getSellerResponseNotes() != null && !claim.getSellerResponseNotes().isEmpty()) {
                binding.sellerNotes.setText("Seller Notes: " + claim.getSellerResponseNotes());
                binding.sellerNotes.setVisibility(View.VISIBLE);
            } else {
                binding.sellerNotes.setVisibility(View.GONE);
            }
        }
    }
}