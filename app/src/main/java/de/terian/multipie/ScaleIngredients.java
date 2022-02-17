package de.terian.multipie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ScaleIngredients extends AppCompatActivity implements Savior, OnMenuItemClickListener {

    private ArrayList<Recipe> cookBook = new ArrayList<>();
    private String recipeName;
    Recipe selectedRecipe;
    ImageView iv_settings;
    TextView tv_scale_recipe_name;
    EditText et_scale_person_count;
    ImageButton bt_scale_minus;
    ImageButton bt_scale_plus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_ingredients);

        // load Data
        cookBook = loadData();
        Intent intent = getIntent();
        recipeName = intent.getStringExtra(MainActivity.EXTRA_TEXT);
        for (Recipe recipe : cookBook) {
            if (recipe.getName().equals(recipeName)) {
                selectedRecipe = recipe;
                break;
            }
        }

        // Prepare Activity
        tv_scale_recipe_name = findViewById(R.id.tv_scale_recipe_name);
        et_scale_person_count = findViewById(R.id.et_scale_person_count);
        et_scale_person_count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (et_scale_person_count.getText().toString().equals("")) {return;}
                if (Integer.valueOf(et_scale_person_count.getText().toString()) < 1) {return;}
                int oldPersonAmount = selectedRecipe.getPersonAmount();
                selectedRecipe.setPersonAmount(Integer.valueOf(et_scale_person_count.getText().toString()));
                calculateNewQuantity(oldPersonAmount);
                renderIngredients();
            }
        });

        iv_settings = findViewById(R.id.iv_settings);
        iv_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }

            public void showMenu(View v) {
                PopupMenu popup = new PopupMenu(getBaseContext(), v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.scale_menue, popup.getMenu());
                popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit_ingredients:
                                Intent add_ing_intent = new Intent(v.getContext(), AddIngredients.class);
                                add_ing_intent.putExtra(MainActivity.EXTRA_TEXT, selectedRecipe.getName());    // gibt der neuen activity den recipeName mit
                                startActivity(add_ing_intent);
                                finish();
                                return true;
                            case R.id.save_quantities:
                                saveData(cookBook);
                                cookBook = loadData();
                                renderIngredients();
                                Toast.makeText(v.getContext(), "saved new quantities", Toast.LENGTH_LONG).show();
                                return true;
                            case R.id.export_recipe:
                                Intent share_intent = new Intent(Intent.ACTION_SEND);
                                share_intent.setType("text/plain");
                                String recipeText = recipeName + " (" + Integer.toString(selectedRecipe.getPersonAmount())+ " " + Languages.DE_PERSONS.getString() + ")\n\n";
                                recipeText += selectedRecipe.toExportString();
                                share_intent.putExtra(Intent.EXTRA_TEXT, recipeText);
                                startActivity(share_intent);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();
            }
        });
        bt_scale_minus = findViewById(R.id.bt_scale_minus);
        bt_scale_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedRecipe.getPersonAmount() == 1) { return; }
                int oldPersonAmount = selectedRecipe.getPersonAmount();
                selectedRecipe.setPersonAmount(selectedRecipe.getPersonAmount() - 1);
                et_scale_person_count.setText(String.valueOf(selectedRecipe.getPersonAmount()));
                calculateNewQuantity(oldPersonAmount);
                renderIngredients();
            }
        });
        bt_scale_plus = findViewById(R.id.bt_scale_plus);
        bt_scale_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int oldPersonAmount = selectedRecipe.getPersonAmount();
                selectedRecipe.setPersonAmount(selectedRecipe.getPersonAmount() + 1);
                et_scale_person_count.setText(String.valueOf(selectedRecipe.getPersonAmount()));
                calculateNewQuantity(oldPersonAmount);
                renderIngredients();
            }
        });

        tv_scale_recipe_name.setText(recipeName);
        et_scale_person_count.setText(Integer.toString(selectedRecipe.getPersonAmount()));
        renderIngredients();

    }

    @SuppressLint({"SetTextI18n", "RtlHardcoded"})

    public void renderIngredients() {
        // Tabellen layout
        TableLayout layout = findViewById(R.id.tl_ingredients_table);
        layout.removeAllViews();

        for (Ingredient ingredient : selectedRecipe.getIngredients()) {
            // neue TabellenZeile
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

            // Menge
            TextView tv_quantity = new TextView(this);
            tv_quantity.setLayoutParams(new TableRow.LayoutParams(300, TableRow.LayoutParams.MATCH_PARENT));
            tv_quantity.setTextSize(20);
            tv_quantity.setGravity(Gravity.RIGHT);

            tv_quantity.setText(convertAmount(ingredient) + " " + ingredient.getAbbreviation(convertUnit(ingredient)));

            // Space zwischen Menge und Name
            Space space = new Space(this);
            space.setLayoutParams(new TableRow.LayoutParams(40, TableRow.LayoutParams.MATCH_PARENT));

            // Name
            TextView tv_name = new TextView(this);
            tv_name.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));
            tv_name.setTextSize(20);
            tv_name.setGravity(Gravity.LEFT);
            tv_name.setText(ingredient.getName() + " " + getEmoji(ingredient.getName()));

            // zum Row hinzufügen
            row.addView(tv_quantity);
            row.addView(space);
            row.addView(tv_name);

            layout.addView(row);
        }



    }

    public static String convertAmount(Ingredient ingredient) {
        double amount = ingredient.getAmount();
        Ingredient.Unit unit = ingredient.getUnit();
        String quantity;
        if (unit == Ingredient.Unit.GRAMM && amount > 1000) {
            amount = (double) (amount / 1000.0);
        }
        if (unit == Ingredient.Unit.MILLI_GRAMM && amount > 1000) {
            amount = (double) (amount / 1000.0);
        }
        if (unit == Ingredient.Unit.MILLI_LITER && amount > 1000) {
            amount = (double) (amount / 1000.0);
        }

        // Darstellung
        if (amount % 1 == 0) {                                          // ganze zahlen ohne komma
            quantity = String.valueOf((int) amount);
        } else {
            DecimalFormat decFormat = new DecimalFormat("##.##"); // kommazahlen zwei Nachkommastellen
            quantity = String.valueOf(decFormat.format(amount));
        }
        return quantity;
    }

    public static Ingredient.Unit convertUnit(Ingredient ingredient) {
        double amount = ingredient.getAmount();
        Ingredient.Unit unit = ingredient.getUnit();
        String quantity;
        if (unit == Ingredient.Unit.GRAMM && amount > 1000) {
            unit = Ingredient.Unit.KILO;
        }
        if (unit == Ingredient.Unit.MILLI_GRAMM && amount > 1000) {
            unit = Ingredient.Unit.GRAMM;
        }
        if (unit == Ingredient.Unit.MILLI_LITER && amount > 1000) {
            unit = Ingredient.Unit.LITER;
        }
        return unit;
    }

    public void calculateNewQuantity(int oldAmount) {
        for (Ingredient selectedIngredient : selectedRecipe.getIngredients()) {
            selectedIngredient.setAmount( (selectedIngredient.getAmount() / oldAmount) * selectedRecipe.getPersonAmount());
        }
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) { return false; }


}