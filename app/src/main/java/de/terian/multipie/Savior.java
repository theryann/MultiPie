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

    enum Colors {
        STANDARD_RED("#FA4343"),
        STANDARD_GREY("#6C6A6A"),
        BG_BLACK("#303030");

        private String colorText;
        private Colors(String colorText) {
            this.colorText = colorText;
        }
        public String getDisplayText() {
            return colorText;
        }
    }
    enum Languages {
        DE_PERSONS("Personen"),
        EN_PERSONS("persons");

        private String text;
        private Languages(String text) {
            this.text = text;
        }
        public String getString() {
            return text;
        }
    }

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

    String[][] pics = {
            {"Ananas", "🍍"},
            {"Aubergine", "🍆"},
            {"Avocado", "🥑"},
            {"Babyflasche", "🍼"},
            {"Bagel", "🥯"},
            {"Baguette", "🥖"},
            {"Banane", "🍌"},
            {"Bento-Box", "🍱"},
            {"Bierkrug", "🍺"},
            {"Birne", "🍐"},
            {"Blattgrün", "🥬"},
            {"Blattgemüse", "🥬"},
            {"Brezel", "🥨"},
            {"Brokkoli", "🥦"},
            {"Brot", "🍞"},
            {"Burrito", "🌯"},
            {"Cocktail", "🍸"},
            {"Croissant", "🥐"},
            {"Cupcake", "🧁"},
            {"Curryreis", "🍛"},
            {"Dango", "🍡"},
            {"Dosen Essen", "🥫"},
            {"Milch", "🥛"},
            {"Eis", "🍨"},
            {"Erdbeere", "🍓"},
            {"Erdnüsse", "🥜"},
            {"Erdnuss", "🥜"},
            {"Strudel", "🍥"},
            {"Knochen", "🍖"},
            {"Fleisch", "🥩"},
            {"Garnelen", "🍤"},
            {"kuchen", "🎂"},
            {"keule", "🍗"},
            {"Fladenbrot", "🥙"},
            {"Reis", "🍚"},
            {"Eis", "🍧"},
            {"Süßkartoffel", "🍠"},
            {"Glückskeks", "🥠"},
            {"Apfel", "🍏"},
            {"Salat", "🥗"},
            {"Gurke", "🥒"},
            {"Mais", "🌽"},
            {"Maiskolben", "🌽"},
            {"Hamburger", "🍔"},
            {"Honigtopf", "🍯"},
            {"Honigmelone", "🍈"},
            {"Hotdog", "🌭"},
            {"Karotte", "🥕"},
            {"Möhre", "🥕"},
            {"Kartoffel", "🥔"},
            {"Käse", "🧀"},
            {"Kastanie", "🌰"},
            {"Kirschen", "🍒"},
            {"Kiwi", "🥝"},
            {"Bierkrüge", "🍻"},
            {"Gläser", "🥂"},
            {"Kochen", "🍳"},
            {"Kokosnuss", "🥥"},
            {"Kornähre", "🌽"},
            {"Krapfen", "🍩"},
            {"Kuchen", "🥧"},
            {"Mandarine", "🍊"},
            {"Mango", "🥭"},
            {"Melone", "🍈"},
            {"Mitnahmebox", "🥡"},
            {"Mondkuchen", "🥮"},
            {"Oden", "🍢"},
            {"Paprika", "🌶"},
            {"Pfannkuchen", "🥞"},
            {"Pfirsich", "🍑"},
            {"Pilz", "🍄"},
            {"Pizza", "🍕"},
            {"Plätzchen", "🍪"},
            {"Popcorn", "🍿"},
            {"Reis-Cracker", "🍘"},
            {"Reisbällchen", "🍙"},
            {"roter Apfel", "🍎"},
            {"Sake", "🍶"},
            {"Salz", "🧂"},
            {"Sandwich", "🥪"},
            {"Schüssel mit Löffel", "🥣"},
            {"Shortcake", "🍰"},
            {"Eis", "🍦"},
            {"Ei", "🥚"},
            {"Butter", "🧈"},
            {"Salat", "🥗"},
            {"Honig", "🍯"},
            {"Saft", "🧃"},
            {"Wasser", "💧"},
            {"Spaghetti", "🍝"},
            {"Nudeln", "🍝"},
            {"Speck", "🥓"},
            {"Sushi", "🍣"},
            {"Süßigkeiten", "🍬"},
            {"Taco", "🌮"},
            {"Olive","🫒"},
            {"Olivenöl","🫒"},
            {"Schokolade", "🍫"},
            {"Tafel Schokolade", "🍫"},
            {"Tee", "🍵"},
            {"Tomate", "🍅"},
            {"Trauben", "🍇"},
            {"Weintrauben", "🍇"},
            {"tropisches Getränk", "🍹"},
            {"Vanillesoße", "🍮"},
            {"Wassermelone", "🍉"},
            {"Weinglas", "🍷"},
            {"Öl", "🌻"},
            {"Sonnenblumenöl", "🌻"},
            {"Zwiebel", "🧅"},
            {"Zitrone", "🍋"}
    };

    default String getEmoji(String word) {
        if (word.length() == 1) { return ""; }
        for (String[] pair : pics) {
            if (        pair[0].equalsIgnoreCase(word)
                    || (pair[0] + "n").equalsIgnoreCase(word)
                    || (pair[0] + "en").equalsIgnoreCase(word)
                    || (pair[0] + "er").equalsIgnoreCase(word) ) {
                return pair[1];
            }
        }
        return "";
    }




}
