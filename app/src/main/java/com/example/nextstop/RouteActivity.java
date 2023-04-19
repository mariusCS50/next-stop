package com.example.nextstop;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.Html;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RouteActivity extends AppCompatActivity {

    LinearLayout[] layouts = new LinearLayout[7];
    TextView[] descriptions = new TextView[7];
    View[] lines = new View[7];

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traseele_rutelor);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        ImageButton myButton = (ImageButton) findViewById(R.id.back_button);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        for (int id = 1; id <= 6; id++) {
            int layoutId = getResources().getIdentifier("layout" + id, "id", getPackageName());
            int lineId = getResources().getIdentifier("line" + id, "id", getPackageName());
            int descriptionId = getResources().getIdentifier("description" + id, "id", getPackageName());
            int descriptionTextId = getResources().getIdentifier("route_" + id + "_description", "string", getPackageName());

            layouts[id] = findViewById(layoutId);
            lines[id] = findViewById(lineId);
            descriptions[id] = findViewById(descriptionId);
            descriptions[id].setText(Html.fromHtml(getString(descriptionTextId)));
        }
    }

    public void expand1(View view){
        int v = (descriptions[1].getVisibility() == View.GONE)? View.VISIBLE : View.GONE;
        TransitionManager.beginDelayedTransition(layouts[1], new AutoTransition());
        descriptions[1].setVisibility(v);
        lines[1].setVisibility(v);
    }

    public void expand2(View view){
        int v = (descriptions[2].getVisibility() == View.GONE)? View.VISIBLE : View.GONE;
        TransitionManager.beginDelayedTransition(layouts[2], new AutoTransition());
        descriptions[2].setVisibility(v);
        lines[2].setVisibility(v);
    }

    public void expand3(View view){
        int v = (descriptions[3].getVisibility() == View.GONE)? View.VISIBLE : View.GONE;
        TransitionManager.beginDelayedTransition(layouts[3], new AutoTransition());
        descriptions[3].setVisibility(v);
        lines[3].setVisibility(v);
    }

    public void expand4(View view){
        int v = (descriptions[4].getVisibility() == View.GONE)? View.VISIBLE : View.GONE;
        TransitionManager.beginDelayedTransition(layouts[4], new AutoTransition());
        descriptions[4].setVisibility(v);
        lines[4].setVisibility(v);
    }

    public void expand5(View view){
        int v = (descriptions[5].getVisibility() == View.GONE)? View.VISIBLE : View.GONE;
        TransitionManager.beginDelayedTransition(layouts[5], new AutoTransition());
        descriptions[5].setVisibility(v);
        lines[5].setVisibility(v);
    }

    public void expand6(View view){
        int v = (descriptions[6].getVisibility() == View.GONE)? View.VISIBLE : View.GONE;
        TransitionManager.beginDelayedTransition(layouts[6], new AutoTransition());
        descriptions[6].setVisibility(v);
        lines[6].setVisibility(v);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}