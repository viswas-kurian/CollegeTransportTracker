package com.example.collegetransporttracker;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class BusDetailsActivity extends AppCompatActivity {

    LinearLayout busContainer;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_bus_details);

        busContainer = findViewById(R.id.busContainer);
        db = new DatabaseHelper(this);
        loadBuses();
    }

    private void loadBuses() {

        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM buses", null);

        if (cursor.getCount() == 0) {

            // ── Empty state ────────────────────────────────────────────────
            LinearLayout emptyState = new LinearLayout(this);
            emptyState.setOrientation(LinearLayout.VERTICAL);
            emptyState.setGravity(android.view.Gravity.CENTER);
            emptyState.setPadding(0, 80, 0, 80);

            LinearLayout.LayoutParams emptyParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            emptyState.setLayoutParams(emptyParams);

            TextView emptyIcon = new TextView(this);
            emptyIcon.setText("🚌");
            emptyIcon.setTextSize(48);
            emptyIcon.setGravity(android.view.Gravity.CENTER);
            emptyIcon.setAlpha(0.35f);
            emptyState.addView(emptyIcon);

            TextView emptyMessage = new TextView(this);
            emptyMessage.setText("No buses registered yet");
            emptyMessage.setTextSize(17);
            emptyMessage.setTextColor(Color.parseColor("#94A3B8"));
            emptyMessage.setGravity(android.view.Gravity.CENTER);
            emptyMessage.setPadding(0, 12, 0, 4);
            emptyState.addView(emptyMessage);

            TextView emptyHint = new TextView(this);
            emptyHint.setText("Go back and add a bus to get started");
            emptyHint.setTextSize(13);
            emptyHint.setTextColor(Color.parseColor("#CBD5E1"));
            emptyHint.setGravity(android.view.Gravity.CENTER);
            emptyState.addView(emptyHint);

            busContainer.addView(emptyState);

        } else {

            int index = 0;

            while (cursor.moveToNext()) {

                String busNumber = cursor.getString(cursor.getColumnIndexOrThrow("bus_number"));
                String route     = cursor.getString(cursor.getColumnIndexOrThrow("route"));
                String driver    = cursor.getString(cursor.getColumnIndexOrThrow("driver"));
                String phone     = cursor.getString(cursor.getColumnIndexOrThrow("phone"));

                // ── Card ──────────────────────────────────────────────────
                CardView card = new CardView(this);
                card.setRadius(dpToPx(20));
                card.setCardElevation(0);
                card.setCardBackgroundColor(Color.WHITE);

                LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                cardParams.setMargins(0, 0, 0, dpToPx(14));
                card.setLayoutParams(cardParams);

                // ── Card content ──────────────────────────────────────────
                LinearLayout content = new LinearLayout(this);
                content.setOrientation(LinearLayout.VERTICAL);
                content.setPadding(dpToPx(20), dpToPx(18), dpToPx(20), dpToPx(18));

                // Bus number badge row
                LinearLayout badgeRow = new LinearLayout(this);
                badgeRow.setOrientation(LinearLayout.HORIZONTAL);
                badgeRow.setGravity(android.view.Gravity.CENTER_VERTICAL);

                TextView busBadge = new TextView(this);
                busBadge.setText("🚌  BUS " + busNumber);
                busBadge.setTextSize(12);
                busBadge.setTextColor(Color.WHITE);
                busBadge.setTypeface(null, Typeface.BOLD);
                busBadge.setBackgroundColor(Color.parseColor("#4F46E5"));
                busBadge.setPadding(dpToPx(10), dpToPx(5), dpToPx(12), dpToPx(5));
                // rounded badge via tag
                busBadge.setBackgroundResource(R.drawable.bg_badge_indigo);
                busBadge.setPadding(dpToPx(12), dpToPx(6), dpToPx(14), dpToPx(6));
                badgeRow.addView(busBadge);
                content.addView(badgeRow);

                // Divider line
                View divider = new View(this);
                divider.setBackgroundColor(Color.parseColor("#F1F5F9"));
                LinearLayout.LayoutParams divParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(1)
                );
                divParams.setMargins(0, dpToPx(14), 0, dpToPx(14));
                divider.setLayoutParams(divParams);
                content.addView(divider);

                // Route info row
                addInfoRow(content, "📍  Route", route, "#0F172A", "#475569");

                // Driver info row
                addInfoRow(content, "👤  Driver", driver, "#0F172A", "#475569");

                // Phone — tappable
                LinearLayout phoneRow = new LinearLayout(this);
                phoneRow.setOrientation(LinearLayout.HORIZONTAL);
                phoneRow.setGravity(android.view.Gravity.CENTER_VERTICAL);
                LinearLayout.LayoutParams phoneRowParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                phoneRowParams.setMargins(0, dpToPx(10), 0, 0);
                phoneRow.setLayoutParams(phoneRowParams);

                TextView phoneLabel = new TextView(this);
                phoneLabel.setText("📞  " + phone);
                phoneLabel.setTextSize(15);
                phoneLabel.setTextColor(Color.parseColor("#4F46E5"));
                phoneLabel.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams phoneLabelParams = new LinearLayout.LayoutParams(
                        0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
                );
                phoneLabel.setLayoutParams(phoneLabelParams);
                phoneRow.addView(phoneLabel);

                TextView callBtn = new TextView(this);
                callBtn.setText("Call");
                callBtn.setTextSize(13);
                callBtn.setTextColor(Color.WHITE);
                callBtn.setTypeface(null, Typeface.BOLD);
                callBtn.setBackgroundResource(R.drawable.bg_button_primary);
                callBtn.setPadding(dpToPx(16), dpToPx(8), dpToPx(16), dpToPx(8));
                callBtn.setGravity(android.view.Gravity.CENTER);
                phoneRow.addView(callBtn);

                phoneRow.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                    startActivity(intent);
                });
                callBtn.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                    startActivity(intent);
                });
                phoneLabel.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                    startActivity(intent);
                });

                content.addView(phoneRow);
                card.addView(content);
                busContainer.addView(card);

                // Stagger animate each card
                animateCardIn(card, index);
                index++;
            }
        }

        cursor.close();
    }

    private void addInfoRow(LinearLayout parent, String label, String value,
                            String labelColor, String valueColor) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        rp.setMargins(0, 0, 0, dpToPx(8));
        row.setLayoutParams(rp);

        TextView lbl = new TextView(this);
        lbl.setText(label);
        lbl.setTextSize(13);
        lbl.setTextColor(Color.parseColor(labelColor));
        lbl.setTypeface(null, Typeface.BOLD);
        row.addView(lbl);

        TextView val = new TextView(this);
        val.setText(value);
        val.setTextSize(14);
        val.setTextColor(Color.parseColor(valueColor));
        LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        vp.setMargins(0, dpToPx(2), 0, 0);
        val.setLayoutParams(vp);
        row.addView(val);

        parent.addView(row);
    }

    private void animateCardIn(View card, int index) {
        card.setAlpha(0f);
        card.setTranslationY(30f);

        ObjectAnimator alpha = ObjectAnimator.ofFloat(card, "alpha", 0f, 1f);
        alpha.setDuration(300);

        ObjectAnimator translate = ObjectAnimator.ofFloat(card, "translationY", 30f, 0f);
        translate.setDuration(300);
        translate.setInterpolator(new DecelerateInterpolator());

        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translate);
        set.setStartDelay(index * 80L);
        set.start();
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}