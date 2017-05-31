package de.reswipe.reswipe.activities.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.reswipe.reswipe.activities.models.Recipe;

import de.reswipe.reswipe.R;
import de.reswipe.reswipe.activities.util.DownloadImageTask;

public class LandingPageActivity extends AppCompatActivity {

    public static final String RECIPE = "com.reswipe.reswipeapp.RECIPE";

    private Recipe recipe;
    private int recipeCount;

    private boolean firstEvent = true;

    private Button like;
    private Button dislike;
    private Button kochliste;

    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        this.database = FirebaseDatabase.getInstance();
        this.myRef = database.getReference("recipes");
        
        this.addMockRecipes();
        this.subscribeRecipeCount();
    }

    public void like (View view) {
        myRef.getRoot().child("likes").push().setValue(recipe);
        this.subscribeRecipe(null);
    }

    public void dislike (View view) {
        myRef.getRoot().child("dislikes").push().setValue(recipe);
        this.subscribeRecipe(null);
    }

    public void kochliste (View view) {
        myRef.getRoot().child("kochliste").push().setValue(recipe);
        this.subscribeRecipe(null);
    }

    public void subscribeRecipeCount() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial recipes and again
                // whenever data at this location is updated.
                long count = dataSnapshot.getChildrenCount();
                recipeCount = (int) count;
                if (firstEvent) {
                    subscribeRecipe(null);
                    firstEvent = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void subscribeRecipe(View view) {
        Random random = new Random();
        final String randomNumber = Integer.toString(random.nextInt(this.recipeCount)+1);
        myRef.child(randomNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial recipes and again
                // whenever data at this location is updated.
                recipe = dataSnapshot.getValue(Recipe.class);
                TextView textViewRecipeName = (TextView) findViewById(R.id.textViewValue);
                textViewRecipeName.setText(recipe.name);
                new DownloadImageTask((ImageView) findViewById(R.id.LandingPageImage))
                        .execute(recipe.image);
                myRef.child(randomNumber).removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    /** Called when the user taps the Send button */
    public void showRecipeDetails(View view) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra(RECIPE, recipe);
        startActivity(intent);
    }

    public void addMockRecipes() {
        List<String> ingredients1 = new ArrayList<>();
        List<String> ingredients2 = new ArrayList<>();
        ingredients1.add("250g Spaghetti");
        ingredients1.add("100g Pesto");
        ingredients2.add("250g Fleisch");
        ingredients2.add("100g Pesto");

        Recipe recipe1 = new Recipe("Spaghetti mit Pesto", "20 min", "Wasser zum kochen bringen, " +
                "Salz + Spaghetti ins Wasser geben, 10 minuten warten, " +
                "Spaghetti vom Wasser trennen, " +
                "Pesto mit Wasser verdünnen und zu Spaghettis geben", ingredients1, "https://firebasestorage.googleapis.com/v0/b/reswipe-db3a4.appspot.com/o/images%2FSpaghettiBolognese.jpg?alt=media&token=e59605b1-6f4c-422b-a6f5-c450368c05bc");

        Recipe recipe2 = new Recipe("Fleisch mit Pesto", "20 min", "Pfanne erhitzen, " +
                "Fleisch anbraten, Fleisch mit Pesto einreiben", ingredients2, "https://firebasestorage.googleapis.com/v0/b/reswipe-db3a4.appspot.com/o/images%2F278319-960x720-rinderfilet-unter-tomaten-pesto-kruste-mit-pfannengemuese.jpg?alt=media&token=b402242f-549a-4907-b56c-e40c932defd7");

        myRef.child("1").setValue(recipe1);
        myRef.child("2").setValue(recipe2);

    }

}
