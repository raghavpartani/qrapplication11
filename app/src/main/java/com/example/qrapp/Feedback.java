package com.example.qrapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class Feedback extends AppCompatActivity {

    TextView textView;
    RatingBar ratingBar;
    Button submit_btn;

    float myrating = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView = findViewById(R.id.rate_text);
        ratingBar = findViewById(R.id.rating);
        submit_btn = findViewById(R.id.submit_btn);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                int star_rating = (int) rating;
                String message = null;

                myrating = ratingBar.getRating();

                switch (star_rating){
                    case 1:
                        message = "Sorry to hear that! :(";
                        break;
                    case 2:
                        message = "We always accept suggestions!";
                        break;
                    case 3:
                        message = "Good enough!";
                        break;
                    case 4:
                        message = "Great! Thank you!";
                        break;
                    case 5:
                        message = "Awesome! You are the best!";
                        break;
                }
                Toast.makeText(Feedback.this, message,Toast.LENGTH_SHORT).show();
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Feedback.this, "Your rating is:" + myrating ,Toast.LENGTH_SHORT).show();
            }
        });


    }
}