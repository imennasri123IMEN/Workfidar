package com.pointage1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.pointage1.activite.activiteutil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pointage1.Calendrier.CalendrierActivity;

public class AcceuilActivity extends AppCompatActivity {

    private Button buttonLogin;
    private Button buttonRegister;
    private LinearLayout menuAcceuilLinearLayout;
    private LinearLayout loginAcceuilLinearLayout;
    private CardView cardviewConge;
    private Button btnconge;
    private Button btnCalendrier;
    private Button btnProfile;
    private Button btnacti;
    private Button btnslide;
    //Firebase
    private boolean connectedUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersDBRef;

    //String myUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);


        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        menuAcceuilLinearLayout = findViewById(R.id.menuAcceuilLinearLayout);
        loginAcceuilLinearLayout = findViewById(R.id.loginAcceuilLinearLayout);
        cardviewConge = findViewById(R.id.cardviewConge);
        btnconge = findViewById(R.id.btnconge);
        btnCalendrier = findViewById(R.id.btnCalendrier);
        btnProfile = findViewById(R.id.btnProfile);
        btnacti = findViewById(R.id.acti);
        btnslide = findViewById(R.id.slide);





        firebaseAuth=FirebaseAuth.getInstance();
        checkUserStatus();
        System.out.println("3");
        System.out.println("connectedUser"+connectedUser);
        if (connectedUser==false) {
            //Cette partie si l'utilisateur n'est pas connecté
            menuAcceuilLinearLayout.setVisibility(View.GONE);
            loginAcceuilLinearLayout.setVisibility(View.VISIBLE);


            buttonRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AcceuilActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });
            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AcceuilActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });




        }else {
            //Cette partie si l'utilisateur est connecté
            menuAcceuilLinearLayout.setVisibility(View.VISIBLE);
            loginAcceuilLinearLayout.setVisibility(View.GONE);



            btnconge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(AcceuilActivity.this,CongeListEmployeActivity.class));
                }
            });

            btnCalendrier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(AcceuilActivity.this, CalendrierActivity.class));
                }
            });

            btnProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(AcceuilActivity.this, ProfileActivity.class));
                }
            });
            btnacti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(AcceuilActivity.this, activiteutil.class));
                }
            });

        } btnslide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AcceuilActivity.this, slide.class));
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            // do stuff
            switch (item.getItemId()) {
                case R.id.page_1_acceuil:
                    //Intent im0 = new Intent(AcceuilActivity.this, HomeActivity.class);
                    //im0.putExtra("mycard", "mycard0");
                    //startActivity(im0);
                    return true;
                case R.id.page_2_profile:
                    startActivity(new Intent(AcceuilActivity.this, ProfileActivity.class));
                    return true;

            }
            return true;
        });

    }

    private void checkUserStatus(){
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            //user is signed in stay here
            connectedUser=true;
            //myUID=user.getUid();
        }else{
            //user not signed in, go to MainActivity
            //startActivity(new Intent(this, MainActivity.class));
            //finish();

            connectedUser=false;
        }
    }


}