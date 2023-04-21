package com.example.nextstop;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;

import com.example.nextstop.CardsRecycleView.Cards;
import com.example.nextstop.CardsRecycleView.CardsAdapter;

import java.util.ArrayList;
import java.util.List;

public class RouteActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Cards> cardsList;

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

        recyclerView = findViewById(R.id.recyclerView);
        initListData();
        setRecycleView();
    }

    private void setRecycleView() {
        CardsAdapter cardsAdapter = new CardsAdapter(cardsList, recyclerView);
        recyclerView.setAdapter(cardsAdapter);
        recyclerView.setHasFixedSize(true);

    }

    private void initListData() {
        cardsList = new ArrayList<>();
        cardsList.add(new Cards("Traseul Rutei Nr.", Html.fromHtml(getString(R.string.route_1_description)), R.drawable.station1, R.drawable.route_1_pressed));
        cardsList.add(new Cards("Traseul Rutei Nr.", Html.fromHtml(getString(R.string.route_2_description)), R.drawable.station2, R.drawable.route_2_pressed));
        cardsList.add(new Cards("Traseul Rutei Nr.", Html.fromHtml(getString(R.string.route_3_description)), R.drawable.station3, R.drawable.route_3_pressed));
        cardsList.add(new Cards("Traseul Rutei Nr.", Html.fromHtml(getString(R.string.route_4_description)), R.drawable.station4, R.drawable.route_4_pressed));
        cardsList.add(new Cards("Traseul Rutei Nr.", Html.fromHtml(getString(R.string.route_5_description)), R.drawable.station5, R.drawable.route_5_pressed));
        cardsList.add(new Cards("Traseul Rutei Nr.", Html.fromHtml(getString(R.string.route_6_description)), R.drawable.station6, R.drawable.route_6_pressed));
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}