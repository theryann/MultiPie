package de.terian.multipie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ingredient {

    public static enum Unit {
        LITER("Liter"),
        MILLI_LITER("Milliliter"),
        CENTI_LITER("Centiliter"),
        GRAMM("Gramm"),
        KILO("Kilogramm"),
        PFUND("Pfund"),
        MILLI_GRAMM("Milligram"),
        TL("Teelöffel"),
        EL("Esslöffel"),
        PRISE("Prise"),
        TASSE("Tasse"),
        PACKUNG("Packung"),
        STUECK("Stück");

        private String unit;
        private Unit(String unit) {
            this.unit = unit;
        }
        public String getDisplayText() {
            return this.unit;
        }
        public static Unit getUnitFromString(String unitString) {
            for (Unit u : Unit.values()) {
                if (u.getDisplayText().equalsIgnoreCase(unitString)) {
                    return u;
                }
            }
            return null;
        }
    }

    private String abbreviations[][] = {
            {"Stück", "Stk."},
            {"Liter", "l"},
            {"Milliliter", "ml"},
            {"Centiliter", "cl"},
            {"Kilogramm", "kg"},
            {"Gramm", "g"},
            {"Milligram", "mg"},
            {"Pfund", "Pf"},
            {"Teelöffel", "TL"},
            {"Esslöffel", "EL"},
            {"Prise", "pr."},
            {"Tasse", "Tas."},
            {"Packung", "Pkg."}
    };



    private String name;
    private double amount;
    private Unit unit;
    private boolean editModeOn = false;

    Ingredient(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
    public double getAmount() {
        return this.amount;
    }
    public Unit getUnit() {
        return unit;
    }
    public String getAbbreviation(Unit u) {
        for (String abb[] : abbreviations) {
            if (abb[0].equalsIgnoreCase(u.getDisplayText())) {
                return abb[1];
            }
        }
        return null;
    }
    public boolean getEditModeOn () {
        return editModeOn;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setUnit(Unit unit) {
        this.unit = unit;
    }
    public void setEditModeOn (Boolean bool) {
        editModeOn = bool;
    }
}

