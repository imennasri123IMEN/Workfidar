package com.pointage1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.pointage1.activite.activite;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pointage1.Calendrier.CalendrierActivity;

public class AdminMainActivity extends AppCompatActivity {

    private Button buttonLogin;
    private Button buttonRegister;
    private LinearLayout menuAcceuilLinearLayout;
    private LinearLayout loginAcceuilLinearLayout;
    private CardView cardviewConge;
    private Button btnconge;
    private Button btnemployes;
    private Button btnCalendrier;
    private Button actii;
    private Button slidee;
    private Button pointage;

    //Firebase
    private boolean connectedUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersDBRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);


        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        menuAcceuilLinearLayout = findViewById(R.id.menuAcceuilLinearLayout);
        loginAcceuilLinearLayout = findViewById(R.id.loginAcceuilLinearLayout);
        cardviewConge = findViewById(R.id.cardviewConge);
        btnconge = findViewById(R.id.btnconge);
        btnemployes = findViewById(R.id.btnemployes);
        btnCalendrier = findViewById(R.id.btnCalendrier);
        pointage = findViewById(R.id.pointage);
       actii = findViewById(R.id.actii);
        slidee= findViewById(R.id.slidee);






        //Cette partie si l'utilisateur est connectÃ©
        menuAcceuilLinearLayout.setVisibility(View.VISIBLE);
        loginAcceuilLinearLayout.setVisibility(View.GONE);



        btnconge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMainActivity.this,CongeAdminActivity.class));
            }
        });


        btnemployes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMainActivity.this,UtilisateursActivity.class));
            }
        });


        btnCalendrier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMainActivity.this, CalendrierActivity.class));
            }
        });
        actii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMainActivity.this, activite.class));
            }
        });
        slidee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMainActivity.this, slide.class));
            }
        });

        pointage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(AdminMainActivity.this, MainActivity.class));
            }
        });



    }






}