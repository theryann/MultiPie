package de.terian.multipie;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ScaleIngredients extends AppCompatActivity implements Savior, OnMenuItemClickListener {

    private ArrayList<Recipe> cookBook = new ArrayList<>();
    private String recipeName;
    Recipe selectedRecipe;
    private String recipeAsString;
    ImageView iv_settings;
    TextView tv_scale_recipe_name;

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
                                add_ing_intent.putExtra(Intent.EXTRA_TEXT, recipeName);    // gibt der neuen activity den recipeName mit
                                startActivity(add_ing_intent);
                                return true;
                            case R.id.save_quantities:
                                Toast.makeText(v.getContext(), "save", Toast.LENGTH_LONG).show();
                                return true;
                            case R.id.export_recipe:
                                cookBook = loadData();
                                Intent share_intent = new Intent(Intent.ACTION_SEND);
                                share_intent.setType("text/plain");
                                for (Recipe recipe : cookBook) {
                                    if (recipe.getName().equals(recipeName)) {
                                        String recipeText = recipeName + " (" + Integer.toString(recipe.getPersonAmount())+ " " + Languages.DE_PERSONS.getString() + ")\n";
                                        recipeText += recipeAsString;
                                        share_intent.putExtra(Intent.EXTRA_TEXT, recipeText);
                                        startActivity(share_intent);
                                    }
                                }
                                return true;
                            default:
                                Toast.makeText(v.getContext(), "default", Toast.LENGTH_LONG).show();
                                return false;
                        }
                    }
                });
                popup.show();
            }
        });



        tv_scale_recipe_name.setText(recipeName);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {

        return false;
    }
}