package de.reswipe.reswipe.activities.activities;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import de.reswipe.reswipe.R;
import de.reswipe.reswipe.activities.models.Recipe;
import de.reswipe.reswipe.activities.util.DownloadImageTask;


public class RecipeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        Recipe recipe = (Recipe) intent.getExtras().getSerializable(LandingPageActivity.RECIPE);

        // Capture the layout's TextView and set the string as its text
        TextView recipeNameValueView = (TextView) findViewById(R.id.textViewNameValue);
        recipeNameValueView.setText(recipe.name);

        TextView recipeTimeValueView = (TextView) findViewById(R.id.textViewTimeValue);
        recipeTimeValueView.setText(recipe.time);

        TextView recipeDescriptionValueView = (TextView) findViewById(R.id.textViewDescriptionValue);
        recipeDescriptionValueView.setText(recipe.description);

        TextView recipeIngridentsValueView = (TextView) findViewById(R.id.textViewIngredientsValue);
        recipeIngridentsValueView.setText(recipe.ingredients.toString());

        new DownloadImageTask((ImageView) findViewById(R.id.RecipeDetailsImageView))
                .execute(recipe.image);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
