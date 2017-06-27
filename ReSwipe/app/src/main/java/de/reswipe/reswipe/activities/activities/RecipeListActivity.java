package de.reswipe.reswipe.activities.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import de.reswipe.reswipe.R;
import de.reswipe.reswipe.activities.models.Recipe;

import static de.reswipe.reswipe.activities.activities.LandingPageActivity.RECIPE;

public class RecipeListActivity extends AppCompatActivity {

    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    private Map<String,Recipe> likedRecipes;
    private Map<String,Recipe> dislikedRecipes;
    private Map<String,Recipe> toCookRecipes;
    FirebaseDatabase database;
    Button likeButton;
    Button dislikeButton;
    Button tocookButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        likeButton = (Button) findViewById(R.id.likeButton);
        dislikeButton = (Button) findViewById(R.id.dislikeButton);
        tocookButton = (Button) findViewById(R.id.tocookButton);

        mainListView = (ListView) findViewById( R.id.listView );
        this.database = FirebaseDatabase.getInstance();

        likeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
        dislikeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
        tocookButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));


        subscribeToCook(null);


        ArrayList<String> recipeList = new ArrayList<String>();

        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, recipeList);
        listAdapter.add("no entry");

        // Set the ArrayAdapter as the ListView's adapter.
        mainListView.setAdapter( listAdapter );

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(RecipeListActivity.this, "clicked!", Toast.LENGTH_SHORT).show();

            }

        });
    }

    private void setListAdapter( Map<String, Recipe> recipes) {

        ArrayList<String> recipeList = new ArrayList<String>();

        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, recipeList);
        if (recipes == null || recipes.size() == 0) {
            listAdapter.add("no entry");
        } else {
            for (Map.Entry<String, Recipe> entry : recipes.entrySet())
            {
                listAdapter.add(entry.getValue().getName());
            }
        }
        // Set the ArrayAdapter as the ListView's adapter.
        mainListView.setAdapter( listAdapter );

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(RecipeListActivity.this, "Not yet implemented!", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void subscribeLikes(View view) {
        DatabaseReference myRef = database.getReference("likes");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String,Recipe>> type = new GenericTypeIndicator<Map<String,Recipe>>() {
                };
                likedRecipes = dataSnapshot.getValue(type);
                setListAdapter(likedRecipes);
                likeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_grey)));
                dislikeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                tocookButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));

            }
            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    public void subscribeDislikes(View view) {
        DatabaseReference myRef = database.getReference("dislikes");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String,Recipe>> type = new GenericTypeIndicator<Map<String,Recipe>>() {
                };
                dislikedRecipes = dataSnapshot.getValue(type);
                setListAdapter(dislikedRecipes);
                likeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                dislikeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_grey)));
                tocookButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
            }
            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    public void subscribeToCook(View view) {
        DatabaseReference myRef = database.getReference("tocook");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String,Recipe>> type = new GenericTypeIndicator<Map<String,Recipe>>() {
                };
                toCookRecipes = dataSnapshot.getValue(type);
                setListAdapter(toCookRecipes);
                likeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                dislikeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                tocookButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_grey)));
            }
            @Override
            public void onCancelled(DatabaseError error) {}
        });
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

