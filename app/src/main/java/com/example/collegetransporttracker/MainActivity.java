package com.example.collegetransporttracker;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper db = new DatabaseHelper(this);
        SQLiteDatabase database = db.getWritableDatabase();

        // ── Button navigation (all original IDs preserved) ──────────────────
        Button btnBusDetails = findViewById(R.id.btnBusDetails);
        btnBusDetails.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, BusDetailsActivity.class));
        });

        Button btnRoutes = findViewById(R.id.btnRoutes);
        btnRoutes.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RoutesActivity.class));
        });

        Button btnSchedule = findViewById(R.id.btnSchedule);
        btnSchedule.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ScheduleActivity.class));
        });

        Button btnAddBus = findViewById(R.id.btnAddBus);
        btnAddBus.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddBusActivity.class));
        });

        Button btnDrivers = findViewById(R.id.btnDrivers);
        btnDrivers.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, DriverActivity.class));
        });

        Button btnAddRoute = findViewById(R.id.btnAddRoute);
        btnAddRoute.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddRouteActivity.class));
        });

        Button btnAddSchedule = findViewById(R.id.btnAddSchedule);
        btnAddSchedule.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddScheduleActivity.class));
        });

        // Make cards clickable (whole card taps navigate too)
        CardView cardBusDetails = findViewById(R.id.cardBusDetails);
        if (cardBusDetails != null) {
            cardBusDetails.setOnClickListener(v ->
                    startActivity(new Intent(MainActivity.this, BusDetailsActivity.class)));
        }
        CardView cardRoutes = findViewById(R.id.cardRoutes);
        if (cardRoutes != null) {
            cardRoutes.setOnClickListener(v ->
                    startActivity(new Intent(MainActivity.this, RoutesActivity.class)));
        }
        CardView cardSchedule = findViewById(R.id.cardSchedule);
        if (cardSchedule != null) {
            cardSchedule.setOnClickListener(v ->
                    startActivity(new Intent(MainActivity.this, ScheduleActivity.class)));
        }
        CardView cardDrivers = findViewById(R.id.cardDrivers);
        if (cardDrivers != null) {
            cardDrivers.setOnClickListener(v ->
                    startActivity(new Intent(MainActivity.this, DriverActivity.class)));
        }

        // ── Animate hero on first open ───────────────────────────────────────
        runEntranceAnimations();
    }

    private void runEntranceAnimations() {

        // ── 1. Bus slides across the hero from left to right ─────────────────
        View bus = findViewById(R.id.imgAnimatedBus);
        if (bus != null) {
            bus.post(() -> {
                float screenWidth = bus.getRootView().getWidth();
                // Start from off-screen left, end at ~60% width
                float endX = screenWidth * 0.55f;

                bus.setTranslationX(-160f);
                bus.setAlpha(0f);

                ObjectAnimator busAlpha = ObjectAnimator.ofFloat(bus, "alpha", 0f, 1f);
                busAlpha.setDuration(300);

                ObjectAnimator busSlide = ObjectAnimator.ofFloat(bus, "translationX", -160f, endX);
                busSlide.setDuration(1100);
                busSlide.setInterpolator(new AccelerateDecelerateInterpolator());

                AnimatorSet busAnim = new AnimatorSet();
                busAnim.playTogether(busAlpha, busSlide);
                busAnim.setStartDelay(200);
                busAnim.start();
            });
        }

        // ── 2. Hero title fades in ────────────────────────────────────────────
        View heroTitle = findViewById(R.id.heroTitleGroup);
        if (heroTitle != null) {
            heroTitle.setAlpha(0f);
            ObjectAnimator titleFade = ObjectAnimator.ofFloat(heroTitle, "alpha", 0f, 1f);
            titleFade.setDuration(500);
            titleFade.setStartDelay(350);
            titleFade.setInterpolator(new DecelerateInterpolator());
            titleFade.start();
        }

        // ── 3. Cards slide up + fade in with stagger ─────────────────────────
        int[] cardIds = {
                R.id.cardBusDetails,
                R.id.cardRoutes,
                R.id.cardSchedule,
                R.id.cardDrivers,
                R.id.cardAddBus,
                R.id.cardAddRoute,
                R.id.cardAddSchedule
        };

        long baseDelay = 550L;
        long stagger   = 90L;

        for (int i = 0; i < cardIds.length; i++) {
            View card = findViewById(cardIds[i]);
            if (card == null) continue;

            card.setAlpha(0f);
            card.setTranslationY(40f);

            ObjectAnimator alpha = ObjectAnimator.ofFloat(card, "alpha", 0f, 1f);
            alpha.setDuration(320);

            ObjectAnimator translate = ObjectAnimator.ofFloat(card, "translationY", 40f, 0f);
            translate.setDuration(320);
            translate.setInterpolator(new DecelerateInterpolator());

            AnimatorSet set = new AnimatorSet();
            set.playTogether(alpha, translate);
            set.setStartDelay(baseDelay + (i * stagger));
            set.start();
        }
    }
}