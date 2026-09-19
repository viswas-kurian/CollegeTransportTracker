package com.example.collegetransporttracker;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ScheduleActivity extends AppCompatActivity {

    LinearLayout scheduleContainer;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_schedule);

        scheduleContainer = findViewById(R.id.scheduleContainer);
        db = new DatabaseHelper(this);
        loadSchedules();
    }

    private void loadSchedules() {

        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM schedules", null);

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
            icon.setText("🕐");
            icon.setTextSize(48);
            icon.setGravity(Gravity.CENTER);
            icon.setAlpha(0.35f);
            emptyState.addView(icon);

            TextView msg = new TextView(this);
            msg.setText("No schedules added yet");
            msg.setTextSize(17);
            msg.setTextColor(Color.parseColor("#94A3B8"));
            msg.setGravity(Gravity.CENTER);
            msg.setPadding(0, 12, 0, 4);
            emptyState.addView(msg);

            TextView hint = new TextView(this);
            hint.setText("Go back and add a schedule to get started");
            hint.setTextSize(13);
            hint.setTextColor(Color.parseColor("#CBD5E1"));
            hint.setGravity(Gravity.CENTER);
            emptyState.addView(hint);

            scheduleContainer.addView(emptyState);

        } else {

            int index = 0;

            while (cursor.moveToNext()) {

                String bus       = cursor.getString(cursor.getColumnIndexOrThrow("bus_number"));
                String route     = cursor.getString(cursor.getColumnIndexOrThrow("route"));
                String departure = cursor.getString(cursor.getColumnIndexOrThrow("departure"));
                String arrival   = cursor.getString(cursor.getColumnIndexOrThrow("arrival"));

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

                // Top row: bus badge + route label
                LinearLayout topRow = new LinearLayout(this);
                topRow.setOrientation(LinearLayout.HORIZONTAL);
                topRow.setGravity(Gravity.CENTER_VERTICAL);

                TextView busBadge = new TextView(this);
                busBadge.setText("🚌  Bus " + bus);
                busBadge.setTextSize(12);
                busBadge.setTextColor(Color.WHITE);
                busBadge.setTypeface(null, Typeface.BOLD);
                busBadge.setBackgroundResource(R.drawable.bg_badge_indigo);
                busBadge.setPadding(dpToPx(10), dpToPx(6), dpToPx(12), dpToPx(6));

                LinearLayout.LayoutParams badgeParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                );
                busBadge.setLayoutParams(badgeParams);
                topRow.addView(busBadge);

                TextView routeLabel = new TextView(this);
                routeLabel.setText("  📍 " + route);
                routeLabel.setTextSize(13);
                routeLabel.setTextColor(Color.parseColor("#64748B"));
                LinearLayout.LayoutParams routeParams = new LinearLayout.LayoutParams(
                        0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
                );
                routeLabel.setLayoutParams(routeParams);
                routeLabel.setMaxLines(1);
                routeLabel.setEllipsize(android.text.TextUtils.TruncateAt.END);
                topRow.addView(routeLabel);

                content.addView(topRow);

                // Divider
                View divider = new View(this);
                divider.setBackgroundColor(Color.parseColor("#F1F5F9"));
                LinearLayout.LayoutParams divParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(1)
                );
                divParams.setMargins(0, dpToPx(14), 0, dpToPx(14));
                divider.setLayoutParams(divParams);
                content.addView(divider);

                // ── Timetable row: departure → arrow → arrival ────────────
                LinearLayout timingRow = new LinearLayout(this);
                timingRow.setOrientation(LinearLayout.HORIZONTAL);
                timingRow.setGravity(Gravity.CENTER_VERTICAL);

                // Departure block
                LinearLayout deptBlock = timingBlock(
                        "DEPARTURE", departure, "#F59E0B", "#92400E"
                );
                LinearLayout.LayoutParams deptParams = new LinearLayout.LayoutParams(
                        0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
                );
                deptBlock.setLayoutParams(deptParams);
                timingRow.addView(deptBlock);

                // Arrow
                TextView arrow = new TextView(this);
                arrow.setText("→");
                arrow.setTextSize(20);
                arrow.setTextColor(Color.parseColor("#CBD5E1"));
                arrow.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams arrowParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                );
                arrowParams.setMargins(dpToPx(8), 0, dpToPx(8), 0);
                arrow.setLayoutParams(arrowParams);
                timingRow.addView(arrow);

                // Arrival block
                LinearLayout arrBlock = timingBlock(
                        "ARRIVAL", arrival, "#10B981", "#065F46"
                );
                LinearLayout.LayoutParams arrParams = new LinearLayout.LayoutParams(
                        0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
                );
                arrBlock.setLayoutParams(arrParams);
                timingRow.addView(arrBlock);

                content.addView(timingRow);
                card.addView(content);
                scheduleContainer.addView(card);

                animateCardIn(card, index);
                index++;
            }
        }

        cursor.close();
    }

    private LinearLayout timingBlock(String label, String time,
                                     String labelColor, String timeColor) {
        LinearLayout block = new LinearLayout(this);
        block.setOrientation(LinearLayout.VERTICAL);

        TextView lbl = new TextView(this);
        lbl.setText(label);
        lbl.setTextSize(9);
        lbl.setTextColor(Color.parseColor(labelColor));
        lbl.setTypeface(null, Typeface.BOLD);
        lbl.setLetterSpacing(0.1f);
        block.addView(lbl);

        TextView val = new TextView(this);
        val.setText(time);
        val.setTextSize(22);
        val.setTextColor(Color.parseColor(timeColor));
        val.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        vp.setMargins(0, dpToPx(2), 0, 0);
        val.setLayoutParams(vp);
        block.addView(val);

        return block;
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