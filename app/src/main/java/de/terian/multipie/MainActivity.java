package de.terian.multipie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Savior {

    public static final String EXTRA_TEXT = "de.terian.multipie.EXTRA_TEXT";

    Button btnAddRecipe;
    TextView tvScaleIngredients;
    Switch switch_delete_mode;
    Boolean deleteMode;


    ArrayList<Recipe> cookBook = new ArrayList<>();
    int btnCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switch_delete_mode = findViewById(R.id.switch_delete_mode);
        switch_delete_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                deleteMode = !switch_delete_mode.isChecked();
                updateList();
            }
        });
        deleteMode = !switch_delete_mode.isChecked();


        updateList();

        tvScaleIngredients = findViewById(R.id.tv_add_ingredients);

        btnAddRecipe = findViewById(R.id.buttonAddRecipe);
        btnAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!deleteMode) {
                    Toast.makeText(MainActivity.this, "Please uncheck Delete Mode first", Toast.LENGTH_LONG).show();
                    return;
                }

                // get Recipe name
                EditText EditTextRecipeName = findViewById(R.id.etRecipeName);
                String recipeName = EditTextRecipeName.getText().toString();
                EditTextRecipeName.setText("");

                // fall textfeld leer abbrechen
                if (recipeName.equals("")) {
                    return;
                }

                // falls recipe name schon vorhanden ist
                if (recipeNameTaken(recipeName)) {
                    Toast.makeText(MainActivity.this, "There's already a recipe called \"" + recipeName + "\". Please delete it or choose another name.", Toast.LENGTH_LONG).show();
                    return;

                }

                // create and save Recipe
                Recipe recipe = new Recipe(recipeName);
                cookBook.add(recipe);
                saveData(cookBook);

                // Open Ingredients Activity
                Intent intent = new Intent(v.getContext(), AddIngredients.class);
                intent.putExtra(EXTRA_TEXT, recipeName);    // gibt der neuen activity den recipeName mit
                startActivity(intent);
                updateList();
            }
        });
    }

    public void updateList() {
        String buttonColor;
        LinearLayout layout = (LinearLayout) findViewById(R.id.verticalLayout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 20);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        layout.removeAllViews();

        if (deleteMode) {
            buttonColor = Colors.BG_BLACK.getDisplayText();
        } else {
            buttonColor = Colors.STANDARD_RED.getDisplayText();
        }

        cookBook = loadData();
        saveData(cookBook);

        for (Recipe recipe : cookBook){
            btnCount += 1;
            Button btnNew = new Button(getApplicationContext());
            btnNew.setId(btnCount);
            btnNew.setBackgroundColor(Color.parseColor(buttonColor));
            btnNew.setTextColor(Color.WHITE);
            btnNew.setAllCaps(false);
            btnNew.setLayoutParams(layoutParams);
            btnNew.setGravity(Gravity.CENTER_VERTICAL);
            btnNew.setTextSize(21);
            btnNew.setPadding(10,3,10,3);
            btnNew.setText(recipe.getName());
            layout.addView(btnNew);

            //Bei click auf recipe
            btnNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!deleteMode) {
                        // delete Mode
                        for (Recipe recipe : cookBook) {
                            if (recipe.getName().equalsIgnoreCase(btnNew.getText().toString())) {
                                cookBook.remove(recipe);
                                break;
                            }
                        }
                        saveData(cookBook);
                        cookBook = loadData();
                        updateList();

                    } else {
                        // Open Scale Ingredients Activity
                        Intent intent = new Intent(v.getContext(), ScaleIngredients.class);
                        intent.putExtra(EXTRA_TEXT, btnNew.getText().toString());    // gibt der neuen activity den button text mit
                        startActivity(intent);
                        switch_delete_mode.setChecked(false);
                    }
                }
            });
        }
    }

    private boolean recipeNameTaken(String recipeNameContestant) {
        cookBook = loadData();
        for (Recipe recipe : cookBook) {
            if (recipe.getName().equals(recipeNameContestant)) {
                return true;
            }
        }
        return false;
    }



}