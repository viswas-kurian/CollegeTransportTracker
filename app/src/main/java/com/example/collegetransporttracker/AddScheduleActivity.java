package com.example.collegetransporttracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddScheduleActivity extends AppCompatActivity {

    Spinner spBus, spRoute;
    EditText etDeparture, etArrival;
    Button btnAddSchedule;

    DatabaseHelper db;

    ArrayList<String> busList;
    ArrayList<String> routeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_add_schedule);

        spBus = findViewById(R.id.spBus);
        spRoute = findViewById(R.id.spRoute);
        etDeparture = findViewById(R.id.etDeparture);
        etArrival = findViewById(R.id.etArrival);
        btnAddSchedule = findViewById(R.id.btnAddSchedule);

        db = new DatabaseHelper(this);

        loadBuses();
        loadRoutes();

        btnAddSchedule.setOnClickListener(v -> addSchedule());
    }

    private void loadBuses() {

        busList = new ArrayList<>();

        SQLiteDatabase database = db.getReadableDatabase();

        Cursor cursor = database.rawQuery(
                "SELECT bus_number FROM buses", null);

        while (cursor.moveToNext()) {

            busList.add(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("bus_number")
                    )
            );
        }

        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                busList
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spBus.setAdapter(adapter);
    }

    private void loadRoutes() {

        routeList = new ArrayList<>();

        SQLiteDatabase database = db.getReadableDatabase();

        Cursor cursor = database.rawQuery(
                "SELECT route_name FROM routes", null);

        while (cursor.moveToNext()) {

            routeList.add(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("route_name")
                    )
            );
        }

        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                routeList
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spRoute.setAdapter(adapter);
    }

    private void addSchedule() {

        if (busList.isEmpty() || routeList.isEmpty()) {

            Toast.makeText(this,
                    "Please add a bus and route first",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        String bus = spBus.getSelectedItem().toString();
        String route = spRoute.getSelectedItem().toString();

        String departure =
                etDeparture.getText().toString().trim();

        String arrival =
                etArrival.getText().toString().trim();

        if (departure.isEmpty() || arrival.isEmpty()) {

            Toast.makeText(this,
                    "Please enter departure and arrival time",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        boolean result = db.addSchedule(
                bus,
                route,
                departure,
                arrival
        );

        if (result) {

            Toast.makeText(this,
                    "Schedule added successfully",
                    Toast.LENGTH_SHORT).show();

            finish();
        }
    }
}