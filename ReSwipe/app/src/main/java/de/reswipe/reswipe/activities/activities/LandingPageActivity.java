package de.reswipe.reswipe.activities.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;

import android.content.res.Configuration;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.reswipe.reswipe.activities.models.Recipe;

import de.reswipe.reswipe.R;
import de.reswipe.reswipe.activities.util.DownloadImageTask;
import de.reswipe.reswipe.activities.util.OnSwipeTouchListener;

public class LandingPageActivity extends AppCompatActivity {

    public static final String RECIPE = "com.reswipe.reswipeapp.RECIPE";
    public static final String RECIPE_SEARCH_RESULT = "com.reswipe.reswipeapp.RECIPE_SEARCH_RESULT";


    private Recipe recipe;
    private ArrayList<Recipe> recipes;
    private int recipeCount;

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private ImageView imageView;

    private boolean firstEvent = true;

    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.navDrawer);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        imageView = (ImageView) findViewById(R.id.LandingPageImage);
        imageView.setOnTouchListener(new OnSwipeTouchListener(LandingPageActivity.this) {
            public void onSwipeTop() {
                kochliste(null);
                Toast.makeText(LandingPageActivity.this, "saved", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                like(null);
                Toast.makeText(LandingPageActivity.this, "liked", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                dislike(null);
                Toast.makeText(LandingPageActivity.this, "disliked", Toast.LENGTH_SHORT).show();
            }

        });

        this.database = FirebaseDatabase.getInstance();
        this.myRef = database.getReference("recipes");
        
        this.addMockRecipes();
        this.subscribeRecipeCount();
        this.subscribeRecipes(null);
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void addDrawerItems() {
        String[] osArray = { "Recipe List", "Shopping List", "Connect Accounts"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent i = new Intent(LandingPageActivity.this, RecipeListActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(LandingPageActivity.this, "Not jet implemented!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item= menu.findItem(R.id.action_settings);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                mDrawerLayout.openDrawer(mDrawerList);
            }
        }

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Add SearchWidget.
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        final EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        searchEditText.setHint("Name...");

        searchEditText.setHintTextColor(getResources().getColor(R.color.white));
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(searchEditText.getText().toString().trim());
                }
                return false;
            }
        });

        return true;
    }

    private void search(String searchInput) {

        ArrayList<Recipe> searchResult = new ArrayList<>();


        for (Recipe r: this.recipes) {
            if (r != null && r.getName() != null && r.getName().toLowerCase().contains(searchInput.toLowerCase())) {
                searchResult.add(r);
            }
        }

        Intent intent = new Intent(this, SearchResultsActivity.class);
        intent.putExtra(RECIPE_SEARCH_RESULT, searchResult);
        startActivity(intent);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
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
        myRef.getRoot().child("tocook").push().setValue(recipe);
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

    public void subscribeRecipes(View view) {

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                GenericTypeIndicator<ArrayList<Recipe>> type = new GenericTypeIndicator<ArrayList<Recipe>>() {};

                recipes = dataSnapshot.getValue(type);

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void showRecipeDetails(View view) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra(RECIPE, recipe);
        startActivity(intent);
    }

    public void addMockRecipes() {
        List<String> ingredients1 = new ArrayList<>();
        ingredients1.add("250g Spaghetti");
        ingredients1.add("100g Pesto");
        Recipe recipe1 = new Recipe("Spaghetti mit Pesto", "20 min", "Wasser zum kochen bringen, " +
                "Salz + Spaghetti ins Wasser geben, 10 minuten warten, " +
                "Spaghetti vom Wasser trennen, " +
                "Pesto mit Wasser verdünnen und zu Spaghettis geben", ingredients1, "https://firebasestorage.googleapis.com/v0/b/reswipe-db3a4.appspot.com/o/images%2FSpaghettiBolognese.jpg?alt=media&token=e59605b1-6f4c-422b-a6f5-c450368c05bc");
        myRef.child("1").setValue(recipe1);


        List<String> ingredients2 = new ArrayList<>();
        ingredients2.add("250g Fleisch");
        ingredients2.add("100g Pesto");
        Recipe recipe2 = new Recipe("Fleisch mit Pesto", "20 min", "Pfanne erhitzen, " +
                "Fleisch anbraten, Fleisch mit Pesto einreiben", ingredients2, "https://firebasestorage.googleapis.com/v0/b/reswipe-db3a4.appspot.com/o/images%2F278319-960x720-rinderfilet-unter-tomaten-pesto-kruste-mit-pfannengemuese.jpg?alt=media&token=b402242f-549a-4907-b56c-e40c932defd7");
        myRef.child("2").setValue(recipe2);

    }

}
