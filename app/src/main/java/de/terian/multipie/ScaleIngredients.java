package de.terian.multipie;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ScaleIngredients extends AppCompatActivity implements Savior, OnMenuItemClickListener {

    private ArrayList<Recipe> cookBook = new ArrayList<>();
    ImageView iv_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scale_ingredients);

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
                                Toast.makeText(v.getContext(), "edit", Toast.LENGTH_LONG).show();
                                return true;
                            case R.id.save_quantities:
                                Toast.makeText(v.getContext(), "save", Toast.LENGTH_LONG).show();
                                return true;
                            case R.id.export_recipe:
                                Toast.makeText(v.getContext(), "export", Toast.LENGTH_LONG).show();
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

        cookBook = loadData();

    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {

        return false;
    }
}