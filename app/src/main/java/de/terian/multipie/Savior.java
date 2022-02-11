package de.terian.multipie;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.zip.Inflater;

import static android.content.Context.MODE_PRIVATE;

public interface Savior {

    SharedPreferences getSharedPreferences(String shared_preferences, int modePrivate);
    WindowManager getWindowManager();
    Context getApplicationContext();

    default void saveData(ArrayList<Recipe> cookBook) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(cookBook);
        editor.putString("recipes", json);
        editor.apply();
    }

    default ArrayList loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        ArrayList<Recipe> cookBook;
        Gson gson = new Gson();
        String json = sharedPreferences.getString("recipes", null);
        Type type = new TypeToken<ArrayList<Recipe>>() {}.getType();
        cookBook = gson.fromJson(json, type);
        if (cookBook == null) {
            cookBook = new ArrayList<>();
        }
        return cookBook;
    }

    default void deleteAllData() {
        ArrayList<Recipe> emptyList = new ArrayList<>();
        saveData(emptyList);
    }


}
