package de.terian.multipie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    private int btnCount;
    TextView tv_add_ingredients_recipe_name;
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

        tv_add_ingredients_recipe_name = findViewById(R.id.tv_add_ingredients_recipe_name);
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

                // add ingredient
                for (Recipe recipe : cookBook) {
                    if (recipe.getName().equals(recipeName)) {
                        // ingredient schon vorhanden? -> updaten
                        for (Ingredient ingredient : recipe.getIngredients()) {
                            if (ingredient.getName().equals(name)) {
                                if (ingredient.getEditModeOn()) {
                                    ingredient.setAmount(amount);
                                    ingredient.setUnit(unit);

                                    saveData(cookBook);
                                    cookBook = loadData();
                                    setIngredients();
                                    en_ingredient_amount.setText("");
                                    et_ingredient_name.setText("");
                                    break;
                                } else {
                                    Toast.makeText(AddIngredients.this, "There's already an ingredient called \""
                                                                                    + ingredient.getName()
                                                                                    + "\". Please delete it, select it to update it or choose another name.", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                        }
                        // keine ingredient mit dem namen gefunden -> neue ingredient
                        Ingredient newIngredient = new Ingredient(name);
                        newIngredient.setAmount(amount);
                        newIngredient.setUnit(unit);
                        recipe.addIngredient(newIngredient);

                        saveData(cookBook);
                        cookBook = loadData();
                        setIngredients();
                        en_ingredient_amount.setText("");
                        et_ingredient_name.setText("");
                        break;
                    }
                }
            }
        });

        // load all saved recipes
        cookBook = loadData();

        // load the recipe the ingredients are added to
        Intent intent = getIntent();
        recipeName = intent.getStringExtra(MainActivity.EXTRA_TEXT);
        tv_add_ingredients_recipe_name.setText(recipeName);
        setIngredients();
    }


    private void setIngredients () {
        LinearLayout layout = (LinearLayout) findViewById(R.id.vl_add_ingredients);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 20);
        layout.removeAllViews();

        DecimalFormat decFormat = new DecimalFormat();
        decFormat.setDecimalSeparatorAlwaysShown(false);

        btnCount = 0;

        for (Recipe recipe : cookBook) {
            if (recipe.getName().equals(recipeName)) {
                for (Ingredient ingredient : recipe.getIngredients()) {
                    String ingredientText = decFormat.format(ingredient.getAmount()) + " "
                                            + ingredient.getAbbreviation(ingredient.getUnit()) + " "
                                            + ingredient.getName();
                    btnCount += 1;
                    Button btnNew = new Button(getApplicationContext());
                    btnNew.setId(btnCount);
                    btnNew.setBackgroundColor(Color.parseColor(Colors.STANDARD_GREY.getDisplayText()));
                    btnNew.setTextColor(Color.WHITE);
                    btnNew.setAllCaps(false);
                    btnNew.setLayoutParams(layoutParams);
                    btnNew.setGravity(Gravity.CENTER_VERTICAL);
                    btnNew.setTextSize(21);
                    btnNew.setPadding(10,3,10,3);
                    btnNew.setText(ingredientText + " " +getEmoji(ingredient.getName()));
                    layout.addView(btnNew);

                    btnNew.setOnClickListener(new View.OnClickListener() {
                        long startTime;
                        @Override
                        public void onClick(View v) {
                            if (ingredient.getEditModeOn()) {                                           // Wenn editmode der Ingredient an
                                if (System.currentTimeMillis() - startTime < 2000) {                    // falls button vor weniger als 2 sekunden gedrückt
                                    recipe.removeIngredient(ingredient);
                                    saveData(cookBook);
                                    loadData();
                                    et_ingredient_name.setText("");
                                    setIngredients();
                                } else {                                                                // falls button vor weniger als 2 sekunden gewählt
                                    ingredient.setEditModeOn(false);
                                    btnNew.setBackgroundColor(Color.parseColor(Colors.STANDARD_GREY.getDisplayText()));
                                    et_ingredient_name.setText("");
                                    startTime = 0;
                                }
                            } else {                                                                    // Wenn editmode der Ingredient aus
                                startTime = System.currentTimeMillis();
                                ingredient.setEditModeOn(true);
                                btnNew.setBackgroundColor(Color.parseColor(Colors.STANDARD_RED.getDisplayText()));
                                et_ingredient_name.setText(ingredient.getName());
                            }
                        }
                    });
                }
                break;
            }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentSelectedUnit = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}