package de.terian.multipie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.text.DecimalFormat;

public class AddIngredients extends AppCompatActivity implements Savior, AdapterView.OnItemSelectedListener {

    private ArrayList<Recipe> cookBook = new ArrayList<>();
    private String recipeName;
    private String currentSelectedUnit;
    private String ingredientTextShow;
    TextView tv_show_ingredients;
    ImageView iv_add_ingredient;
    EditText et_ingredient_name;
    EditText en_ingredient_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredients);

        Spinner spn_choose_unit = findViewById(R.id.spn_choose_unit);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.units, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_choose_unit.setAdapter(adapter);
        spn_choose_unit.setOnItemSelectedListener(this);

        tv_show_ingredients = findViewById(R.id.tv_show_ingredients);
        et_ingredient_name = findViewById(R.id.et_ingredient_name);
        en_ingredient_amount = findViewById(R.id.en_ingredient_amount);

        iv_add_ingredient = findViewById(R.id.iv_add_ingredient);
        iv_add_ingredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // abbruch falls keine name oder menge vorhanden ist
                if (en_ingredient_amount.getText().toString().length() == 0 || et_ingredient_name.getText().toString().length() == 0) {
                    return;
                }

                // get values
                String name = et_ingredient_name.getText().toString();
                Double amount = Double.parseDouble(en_ingredient_amount.getText().toString());
                Ingredient.Unit unit = Ingredient.Unit.getUnitFromString(currentSelectedUnit);

                for (Recipe recipe : cookBook) {
                    if (recipe.getName().equals(recipeName)) {
                        Ingredient newIngredient = new Ingredient(name);
                        newIngredient.setAmount(amount);
                        newIngredient.setUnit(unit);
                        recipe.addIngredient(newIngredient);
                        break;
                    }
                }

                saveData(cookBook);
                cookBook = loadData();

                setIngredientsTextView();
//                en_ingredient_amount.setText("");
//                et_ingredient_name.setText("");


            }
        });

        // load all saved recipes
        cookBook = loadData();

        // load the recipe the ingredients are added to
        Intent intent = getIntent();
        recipeName = intent.getStringExtra(MainActivity.EXTRA_TEXT);
        setIngredientsTextView();

    }

    private void setIngredientsTextView() {
        // format für 3.0 -> 3
        DecimalFormat decFormat = new DecimalFormat();
        decFormat.setDecimalSeparatorAlwaysShown(false);

        ingredientTextShow = recipeName + "\n\n";
        for (Recipe recipe : cookBook) {
            if (recipe.getName().equals(recipeName)) {
                for (Ingredient ingredient : recipe.getIngredients()) {
                    ingredientTextShow += decFormat.format(ingredient.getAmount()) + " "
                                        + ingredient.getAbbreviation(ingredient.getUnit()) + " "
                                        + ingredient.getName()
                                        + "\n";
                }
            }
        }
        tv_show_ingredients.setText(ingredientTextShow);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentSelectedUnit = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}