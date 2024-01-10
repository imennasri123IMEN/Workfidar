package com.pointage1;


import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.net.URI;

public class slide extends AppCompatActivity {
     ImageView facebbok,web;
    SliderView sliderView;
    RatingBar ratingbar;
    Button react;
    int[] images = {
            R.drawable.neo1,
            R.drawable.neo2,
            R.drawable.neo5,
            R.drawable.neo4,
          };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);
        addListenerOnButtonClick();
        facebbok = findViewById(R.id.facebook);
        web = findViewById(R.id.web);
        sliderView = findViewById(R.id.image_slider);



        SliderAdapter sliderAdapter = new SliderAdapter(images);

        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();
        facebbok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sapplink="https://www.facebook.com/";
                String sapppakage="https://www.facebook.com/";
                String sappweblink="https://www.facebook.com/";
                openLink(sapplink,sapppakage,sapplink);

            }
        });
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sapplink="https://www.neoweb.tn/";
                String sapppakage="https://www.neoweb.tn/";
                String sappweblink="https://www.neoweb.tn/";
                openLink(sapplink,sapppakage,sapplink);

            }
        });



    }

    public void addListenerOnButtonClick() {
        ratingbar = (RatingBar) findViewById(R.id.ratingBar);
        react = (Button) findViewById(R.id.react);
        //Performing action on Button Click
        react.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //Getting the rating and displaying it on the toast
                String rating = String.valueOf(ratingbar.getRating());
                Toast.makeText(getApplicationContext(), rating, Toast.LENGTH_LONG).show();
            }

        });
    }

    private void openLink(String sapplink,String sapppakage,String sappweblink) {
    try {
        Uri uri = Uri.parse(sapplink);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(sapppakage);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    } catch (ActivityNotFoundException activityNotFoundException) {
        Uri uri = Uri.parse(sappweblink);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

}}

