package com.example.nextstop;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

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
        
        initData();
        setRecycleView();
    }

    private void setRecycleView() {
        CardsAdapter cardsAdapter = new CardsAdapter(cardsList);
        recyclerView.setAdapter(cardsAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void initData() {
        cardsList = new ArrayList<>();
        cardsList.add(new Cards("Traseul Rutei Nr. 1", "Descriere ruta 1"));
        cardsList.add(new Cards("Traseul Rutei Nr. 2", "Descriere ruta 1"));
        cardsList.add(new Cards("Traseul Rutei Nr. 3", "Descriere ruta 1"));
        cardsList.add(new Cards("Traseul Rutei Nr. 4", "Descriere ruta 1"));
        cardsList.add(new Cards("Traseul Rutei Nr. 5", "Descriere ruta 1"));
        cardsList.add(new Cards("Traseul Rutei Nr. 6", "Descriere ruta 1"));
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}