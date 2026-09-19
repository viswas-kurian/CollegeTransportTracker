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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class DriverActivity extends AppCompatActivity {

    LinearLayout driverContainer;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_driver);

        driverContainer = findViewById(R.id.driverContainer);
        db = new DatabaseHelper(this);
        loadDrivers();
    }

    private void loadDrivers() {

        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT bus_number, driver, phone FROM buses", null
        );

        if (cursor.getCount() == 0) {

            // ── Empty state ────────────────────────────────────────────────
            LinearLayout emptyState = new LinearLayout(this);
            emptyState.setOrientation(LinearLayout.VERTICAL);
            emptyState.setGravity(Gravity.CENTER);
            emptyState.setPadding(0, 80, 0, 80);
            LinearLayout.LayoutParams ep = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            );
            emptyState.setLayoutParams(ep);

            TextView icon = new TextView(this);
            icon.setText("👨‍✈️");
            icon.setTextSize(48);
            icon.setGravity(Gravity.CENTER);
            icon.setAlpha(0.35f);
            emptyState.addView(icon);

            TextView msg = new TextView(this);
            msg.setText("No driver information available");
            msg.setTextSize(17);
            msg.setTextColor(Color.parseColor("#94A3B8"));
            msg.setGravity(Gravity.CENTER);
            msg.setPadding(0, 12, 0, 4);
            emptyState.addView(msg);

            TextView hint = new TextView(this);
            hint.setText("Add a bus with driver details to see contacts");
            hint.setTextSize(13);
            hint.setTextColor(Color.parseColor("#CBD5E1"));
            hint.setGravity(Gravity.CENTER);
            emptyState.addView(hint);

            driverContainer.addView(emptyState);

        } else {

            int index = 0;

            while (cursor.moveToNext()) {

                String bus    = cursor.getString(cursor.getColumnIndexOrThrow("bus_number"));
                String driver = cursor.getString(cursor.getColumnIndexOrThrow("driver"));
                String phone  = cursor.getString(cursor.getColumnIndexOrThrow("phone"));

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
                content.setOrientation(LinearLayout.HORIZONTAL);
                content.setGravity(Gravity.CENTER_VERTICAL);
                content.setPadding(dpToPx(20), dpToPx(20), dpToPx(20), dpToPx(20));

                // Avatar circle
                TextView avatar = new TextView(this);
                avatar.setText("👨‍✈️");
                avatar.setTextSize(24);
                avatar.setGravity(Gravity.CENTER);
                avatar.setBackgroundResource(R.drawable.bg_badge_indigo);
                avatar.setAlpha(0.85f);
                LinearLayout.LayoutParams avatarParams = new LinearLayout.LayoutParams(
                        dpToPx(56), dpToPx(56)
                );
                avatar.setLayoutParams(avatarParams);
                content.addView(avatar);

                // Text block
                LinearLayout textBlock = new LinearLayout(this);
                textBlock.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                        0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
                );
                textParams.setMarginStart(dpToPx(14));
                textBlock.setLayoutParams(textParams);

                // Driver name
                TextView nameText = new TextView(this);
                nameText.setText(driver);
                nameText.setTextSize(17);
                nameText.setTextColor(Color.parseColor("#0F172A"));
                nameText.setTypeface(null, Typeface.BOLD);
                textBlock.addView(nameText);

                // Bus sub-label
                TextView busText = new TextView(this);
                busText.setText("Bus " + bus);
                busText.setTextSize(13);
                busText.setTextColor(Color.parseColor("#64748B"));
                LinearLayout.LayoutParams btParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                );
                btParams.setMargins(0, dpToPx(3), 0, dpToPx(6));
                busText.setLayoutParams(btParams);
                textBlock.addView(busText);

                // Phone number
                TextView phoneText = new TextView(this);
                phoneText.setText("📞  " + phone);
                phoneText.setTextSize(14);
                phoneText.setTextColor(Color.parseColor("#4F46E5"));
                phoneText.setTypeface(null, Typeface.BOLD);
                textBlock.addView(phoneText);

                content.addView(textBlock);

                // Call button
                TextView callBtn = new TextView(this);
                callBtn.setText("Call");
                callBtn.setTextSize(13);
                callBtn.setTextColor(Color.WHITE);
                callBtn.setTypeface(null, Typeface.BOLD);
                callBtn.setBackgroundResource(R.drawable.bg_button_primary);
                callBtn.setPadding(dpToPx(16), dpToPx(10), dpToPx(16), dpToPx(10));
                callBtn.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams callParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                );
                callParams.setMarginStart(dpToPx(10));
                callBtn.setLayoutParams(callParams);

                callBtn.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                    startActivity(intent);
                });
                phoneText.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                    startActivity(intent);
                });

                content.addView(callBtn);
                card.addView(content);
                driverContainer.addView(card);

                animateCardIn(card, index);
                index++;
            }
        }

        cursor.close();
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