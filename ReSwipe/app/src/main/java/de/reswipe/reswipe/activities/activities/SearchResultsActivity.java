package de.reswipe.reswipe.activities.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import de.reswipe.reswipe.R;
import de.reswipe.reswipe.activities.models.Recipe;

import static de.reswipe.reswipe.activities.activities.LandingPageActivity.RECIPE;

public class SearchResultsActivity extends AppCompatActivity {

    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    private ArrayList<Recipe> recipes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        recipes = (ArrayList<Recipe>) intent.getExtras().getSerializable(LandingPageActivity.RECIPE_SEARCH_RESULT);

        mainListView = (ListView) findViewById( R.id.mainListView );

        ArrayList<String> recipeList = new ArrayList<String>();

        // Create ArrayAdapter using the planet list.
        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, recipeList);

        // Add more planets. If you passed a String[] instead of a List<String>
        // into the ArrayAdapter constructor, you must not add more items.
        // Otherwise an exception will occur.
        for (Recipe r: recipes) {
            listAdapter.add(r.getName());
        }
        // Set the ArrayAdapter as the ListView's adapter.
        mainListView.setAdapter( listAdapter );

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                showRecipeDetails(null, position);
            }

        });
    }

    public void showRecipeDetails(View view, int position) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra(RECIPE, recipes.get(position));
        startActivity(intent);
    }

}
