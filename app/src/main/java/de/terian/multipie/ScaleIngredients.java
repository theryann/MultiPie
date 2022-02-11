package de.terian.multipie;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ScaleIngredients extends AppCompatActivity implements Savior {

    private ArrayList<Recipe> cookBook = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scale_ingredients);

        cookBook = loadData();

    }
}