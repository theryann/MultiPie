package de.terian.multipie;

import java.util.ArrayList;

public class Recipe {
    private String name;
    private int personAmount;
    private ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

    Recipe(String name) {
        this.name = name;
        personAmount = 2;
    }


    public String getName() {
        return name;
    }
    public int getPersonAmount() {
        return personAmount;
    }
    public ArrayList<Ingredient> getIngredients(){
        return ingredients;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
    public void setPersonAmount(int personAmount) {
        this.personAmount = personAmount;
    }
    public void addIngredient(Ingredient ingredient){
        this.ingredients.add(ingredient);
    }
    public void removeIngredient(Ingredient ingredientDel){
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getName().equals(ingredientDel.getName())){
                this.ingredients.remove(ingredient);
                break;
            }
        }
    }
    public void printRecipe() {
        for (Ingredient i : ingredients) {
            System.out.println(i.getName() + " " + i.getAmount() + " " + i.getUnit().getDisplayText());
        }
    }
    public String toExportString() {
        String export = "";
        for (Ingredient ing : ingredients) {
            Ingredient.Unit unit = ScaleIngredients.convertUnit(ing);
            String quantity = ScaleIngredients.convertAmount(ing);

            export += quantity + " "
                    + ing.getAbbreviation(unit) + " "
                    + ing.getName() + "\n";
        }
        return export;
    }
}