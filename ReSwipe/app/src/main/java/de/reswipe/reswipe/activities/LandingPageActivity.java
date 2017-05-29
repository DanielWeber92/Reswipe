package de.reswipe.reswipe.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.reswipe.reswipe.R;

public class LandingPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("recipes");

        myRef.setValue("Hello, World!");
    }

}
