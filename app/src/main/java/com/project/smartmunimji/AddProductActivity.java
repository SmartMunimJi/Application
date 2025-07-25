package com.project.smartmunimji;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.project.smartmunimji.databinding.ActivityAddProductBinding;
import java.util.Calendar;

public class AddProductActivity extends AppCompatActivity {

    private ActivityAddProductBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup spinner with mock sellers
        String[] sellers = {"TechMart", "Fashion Hub", "Book Nook", "HomeGoods"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sellers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.sellerSpinner.setAdapter(adapter);
        binding.sellerSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                Toast.makeText(AddProductActivity.this, "Seller " + sellers[position] + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        // Date picker
        binding.pickDateButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        Toast.makeText(this, "Date selected: " + date, Toast.LENGTH_SHORT).show();
                        binding.dateLabel.setText(getString(R.string.date_of_purchase) + ": " + date);
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        // Register product
        binding.registerProductButton.setOnClickListener(v -> {
            String orderId = binding.orderIdInput.getText().toString();
            if (orderId.isEmpty()) {
                Toast.makeText(this, "Registration failed: Invalid details.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Attempting to register product...", Toast.LENGTH_SHORT).show();
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    Toast.makeText(this, "Product registered successfully!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, ViewAllProductsActivity.class));
                    finish();
                }, 2000);
            }
        });
    }
}