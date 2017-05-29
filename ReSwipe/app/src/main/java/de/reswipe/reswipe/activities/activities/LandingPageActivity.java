package de.reswipe.reswipe.activities.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koushikdutta.ion.Ion;

import de.reswipe.reswipe.activities.models.Recipe;

import de.reswipe.reswipe.R;

public class LandingPageActivity extends AppCompatActivity {

    private Recipe recipe;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        imageView = (ImageView)this.findViewById(R.id.landingPageRecipeImage);
        imageView.setVisibility(View.GONE);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("recipes");

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recipe = dataSnapshot.getValue(Recipe.class);
                setImage();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void setImage() {
        Ion.with(imageView)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .animateLoad(spinAnimation)
                .animateIn(fadeInAnimation)
                .load("http://example.com/image.png");
        recipe.loadImage();mageView.setImageBitmap(recipe.downloadedImage);
        i
    }
}
