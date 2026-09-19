package com.example.collegetransporttracker;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class RoutesActivity extends AppCompatActivity {

    LinearLayout routeContainer;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_routes);

        routeContainer = findViewById(R.id.routeContainer);
        db = new DatabaseHelper(this);
        loadRoutes();
    }

    private void loadRoutes() {

        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM routes", null);

        if (cursor.getCount() == 0) {

            // ── Empty state ────────────────────────────────────────────────
            LinearLayout emptyState = new LinearLayout(this);
            emptyState.setOrientation(LinearLayout.VERTICAL);
            emptyState.setGravity(android.view.Gravity.CENTER);
            emptyState.setPadding(0, 80, 0, 80);
            LinearLayout.LayoutParams ep = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            );
            emptyState.setLayoutParams(ep);

            TextView icon = new TextView(this);
            icon.setText("📍");
            icon.setTextSize(48);
            icon.setGravity(android.view.Gravity.CENTER);
            icon.setAlpha(0.35f);
            emptyState.addView(icon);

            TextView msg = new TextView(this);
            msg.setText("No routes added yet");
            msg.setTextSize(17);
            msg.setTextColor(Color.parseColor("#94A3B8"));
            msg.setGravity(android.view.Gravity.CENTER);
            msg.setPadding(0, 12, 0, 4);
            emptyState.addView(msg);

            TextView hint = new TextView(this);
            hint.setText("Go back and add a route to get started");
            hint.setTextSize(13);
            hint.setTextColor(Color.parseColor("#CBD5E1"));
            hint.setGravity(android.view.Gravity.CENTER);
            emptyState.addView(hint);

            routeContainer.addView(emptyState);

        } else {

            int index = 0;

            while (cursor.moveToNext()) {

                String routeName = cursor.getString(cursor.getColumnIndexOrThrow("route_name"));
                String stops     = cursor.getString(cursor.getColumnIndexOrThrow("stops"));

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

                // Route name badge
                TextView routeBadge = new TextView(this);
                routeBadge.setText("📍  " + routeName);
                routeBadge.setTextSize(13);
                routeBadge.setTextColor(Color.WHITE);
                routeBadge.setTypeface(null, Typeface.BOLD);
                routeBadge.setBackgroundResource(R.drawable.bg_badge_emerald);
                routeBadge.setPadding(dpToPx(12), dpToPx(7), dpToPx(14), dpToPx(7));
                content.addView(routeBadge);

                // Divider
                View div = new View(this);
                div.setBackgroundColor(Color.parseColor("#F1F5F9"));
                LinearLayout.LayoutParams dp2 = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(1)
                );
                dp2.setMargins(0, dpToPx(14), 0, dpToPx(14));
                div.setLayoutParams(dp2);
                content.addView(div);

                // Stops section label
                TextView stopsLabel = new TextView(this);
                stopsLabel.setText("STOPS");
                stopsLabel.setTextSize(10);
                stopsLabel.setTextColor(Color.parseColor("#10B981"));
                stopsLabel.setTypeface(null, Typeface.BOLD);
                stopsLabel.setLetterSpacing(0.08f);
                content.addView(stopsLabel);

                // ── Route visual: parse stops and draw dot-line strip ──────
                String[] stopList = stops.split("[,\n]");
                LinearLayout stopsStrip = new LinearLayout(this);
                stopsStrip.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams stripParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                stripParams.setMargins(0, dpToPx(10), 0, 0);
                stopsStrip.setLayoutParams(stripParams);

                for (int i = 0; i < stopList.length; i++) {
                    String stop = stopList[i].trim();
                    if (stop.isEmpty()) continue;

                    LinearLayout stopRow = new LinearLayout(this);
                    stopRow.setOrientation(LinearLayout.HORIZONTAL);
                    stopRow.setGravity(android.view.Gravity.CENTER_VERTICAL);
                    LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    rowParams.setMargins(0, 0, 0, dpToPx(2));
                    stopRow.setLayoutParams(rowParams);

                    // Vertical connector column (dot + line)
                    LinearLayout connector = new LinearLayout(this);
                    connector.setOrientation(LinearLayout.VERTICAL);
                    connector.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
                    LinearLayout.LayoutParams connParams = new LinearLayout.LayoutParams(
                            dpToPx(18), ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    connector.setLayoutParams(connParams);

                    // Top connector line (not for first stop)
                    View topLine = new View(this);
                    LinearLayout.LayoutParams topLineParams = new LinearLayout.LayoutParams(
                            dpToPx(2), dpToPx(i == 0 ? 0 : 10)
                    );
                    topLineParams.gravity = android.view.Gravity.CENTER_HORIZONTAL;
                    topLine.setLayoutParams(topLineParams);
                    topLine.setBackgroundColor(Color.parseColor("#D1FAE5"));
                    connector.addView(topLine);

                    // Dot
                    View dot = new View(this);
                    LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(
                            dpToPx(10), dpToPx(10)
                    );
                    dotParams.gravity = android.view.Gravity.CENTER_HORIZONTAL;
                    dot.setLayoutParams(dotParams);

                    // First = emerald filled, last = emerald filled, rest = outline-style
                    if (i == 0 || i == stopList.length - 1) {
                        dot.setBackgroundResource(R.drawable.bg_badge_emerald);
                    } else {
                        dot.setBackgroundResource(R.drawable.bg_route_dot);
                        dot.setAlpha(0.5f);
                    }
                    connector.addView(dot);

                    // Bottom connector line (not for last stop)
                    View bottomLine = new View(this);
                    LinearLayout.LayoutParams bottomLineParams = new LinearLayout.LayoutParams(
                            dpToPx(2), dpToPx(i == stopList.length - 1 ? 0 : 10)
                    );
                    bottomLineParams.gravity = android.view.Gravity.CENTER_HORIZONTAL;
                    bottomLine.setLayoutParams(bottomLineParams);
                    bottomLine.setBackgroundColor(Color.parseColor("#D1FAE5"));
                    connector.addView(bottomLine);

                    stopRow.addView(connector);

                    // Stop name label
                    TextView stopName = new TextView(this);
                    stopName.setText(stop);
                    stopName.setTextSize(14);
                    stopName.setTextColor(Color.parseColor(
                            (i == 0 || i == stopList.length - 1) ? "#0F172A" : "#475569"
                    ));
                    if (i == 0 || i == stopList.length - 1) {
                        stopName.setTypeface(null, Typeface.BOLD);
                    }
                    LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    nameParams.setMarginStart(dpToPx(10));
                    stopName.setLayoutParams(nameParams);
                    stopRow.addView(stopName);

                    stopsStrip.addView(stopRow);
                }

                content.addView(stopsStrip);
                card.addView(content);
                routeContainer.addView(card);

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