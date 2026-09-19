package com.example.collegetransporttracker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddBusActivity extends AppCompatActivity {

    EditText etBusNumber, etRoute, etDriver, etPhone;
    Button btnAddBus;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);

        etBusNumber = findViewById(R.id.etBusNumber);
        etRoute = findViewById(R.id.etRoute);
        etDriver = findViewById(R.id.etDriver);
        etPhone = findViewById(R.id.etPhone);
        btnAddBus = findViewById(R.id.btnAddBus);

        db = new DatabaseHelper(this);

        btnAddBus.setOnClickListener(v -> {

            String busNumber = etBusNumber.getText().toString();
            String route = etRoute.getText().toString();
            String driver = etDriver.getText().toString();
            String phone = etPhone.getText().toString();

            if (busNumber.isEmpty() || route.isEmpty() ||
                    driver.isEmpty() || phone.isEmpty()) {

                Toast.makeText(this,
                        "Please fill all fields",
                        Toast.LENGTH_SHORT).show();

            } else {

                boolean inserted = db.addBus(
                        busNumber,
                        route,
                        driver,
                        phone
                );

                if (inserted) {
                    Toast.makeText(this,
                            "Bus added successfully",
                            Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    Toast.makeText(this,
                            "Failed to add bus",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}