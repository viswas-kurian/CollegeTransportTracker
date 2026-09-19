package com.example.collegetransporttracker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddRouteActivity extends AppCompatActivity {

    EditText etRouteName, etStops;
    Button btnAddRoute;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);

        etRouteName = findViewById(R.id.etRouteName);
        etStops = findViewById(R.id.etStops);
        btnAddRoute = findViewById(R.id.btnAddRoute);

        db = new DatabaseHelper(this);

        btnAddRoute.setOnClickListener(v -> {

            String routeName = etRouteName.getText().toString();
            String stops = etStops.getText().toString();

            if (routeName.isEmpty() || stops.isEmpty()) {

                Toast.makeText(this,
                        "Please fill all fields",
                        Toast.LENGTH_SHORT).show();

            } else {

                boolean inserted =
                        db.addRoute(routeName, stops);

                if (inserted) {

                    Toast.makeText(this,
                            "Route added successfully",
                            Toast.LENGTH_SHORT).show();

                    finish();

                } else {

                    Toast.makeText(this,
                            "Failed to add route",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}