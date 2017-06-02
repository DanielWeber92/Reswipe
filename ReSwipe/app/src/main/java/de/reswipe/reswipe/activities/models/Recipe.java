package de.reswipe.reswipe.activities.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wedan on 29.05.2017.
 */

public class Recipe implements Serializable {

    public String name;
    public String time;
    public String description;
    public List<String> ingredients;
    public String image;

    public Recipe(String name, String time, String description, List<String> ingredients, String image) {
        this.name = name;
        this.time = time;
        this.description = description;
        this.ingredients = ingredients;
        this.image = image;
    }

    public Recipe () { }

    public String getName() {
        return this.name;
    }
}
