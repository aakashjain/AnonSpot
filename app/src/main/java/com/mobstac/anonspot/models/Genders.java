package com.mobstac.anonspot.models;

/**
 * Created by aakash on 14/1/16.
 */
public class Genders {
    private int males;
    private int females;
    private int others;

    public Genders() {}

    public Genders(int males, int females, int others) {
        this.males = males;
        this.females = females;
        this.others = others;
    }

    public int getMales() {
        return males;
    }
    public int getFemales() {
        return females;
    }
    public int getOthers() {
        return others;
    }

    public float getRatio(String gender) {
        int total = males + females + others;
        if (total == 0) {
            return 0;
        }
        switch (gender) {
            case "Male":
                return (float) males/total;
            case "Female":
                return (float) females/total;
            default:
                return (float) others/total;
        }
    }

    public void increment(String gender) {
        switch (gender) {
            case "Male":
                males = males + 1;
                break;
            case "Female":
                females = females + 1;
                break;
            default:
                others = others + 1;
        }
    }

    public void decrement(String gender) {
        switch (gender) {
            case "Male":
                males = males - 1;
                break;
            case "Female":
                females = females - 1;
                break;
            default:
                others = others - 1;
        }
    }
}
