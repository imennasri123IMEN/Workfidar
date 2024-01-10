package com.pointage1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pointage1.Model.Utilisateur;
import com.squareup.picasso.Picasso;

public class UtilisateurDetailActivity extends AppCompatActivity {


    //private Button buttonLogin;
    //private Button buttonRegister;
    private LinearLayout profileLinearLayout;
    //private LinearLayout loginAcceuilLinearLayout;
    private TextView textViewNomEtPrenom;
    private ImageView imageViewImageProfile;
    private ImageView imageViewCover;
    private TextView textViewEmail;
    private TextView textViewTelephone;
    private TextView textViewCin;
    private TextView textViewMatricule;
    private TextView textViewDateDeNaissance;
    private TextView textViewDateInscription;
    private LinearLayout matriculeLinearLayout;
    private Button ajouterButton;
    private Button modifierButton;
    private Button supprimerButton;
    private ProgressDialog pd;


    private String id_Utilisateur;
    private String email;
    private String nom;
    private String prenom;
    private String cin;
    private String matricule;
    private String motdepasse;
    private String telephone;
    private String image;
    private String cover;
    private String date_naissance;
    private String date_inscription;
    private String role;



    private boolean connectedUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersDBRef;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilisateur_detail);


        //buttonLogin = findViewById(R.id.buttonLogin);
        //buttonRegister = findViewById(R.id.buttonRegister);
        profileLinearLayout = findViewById(R.id.profileLinearLayout);
        //loginAcceuilLinearLayout = findViewById(R.id.loginAcceuilLinearLayout);
        textViewNomEtPrenom =(TextView) findViewById(R.id.textViewNomEtPrenom);
        imageViewImageProfile =(ImageView) findViewById(R.id.imageViewImageProfile);
        imageViewCover =(ImageView) findViewById(R.id.imageViewCover);
        textViewEmail =(TextView) findViewById(R.id.textViewEmail);
        textViewTelephone =(TextView) findViewById(R.id.textViewTelephone);
        textViewCin =(TextView) findViewById(R.id.textViewCin);
        textViewMatricule =(TextView) findViewById(R.id.textViewMatricule);
        textViewDateDeNaissance =(TextView) findViewById(R.id.textViewDateDeNaissance);
        textViewDateInscription =(TextView) findViewById(R.id.textViewDateInscription);
        ajouterButton =(Button) findViewById(R.id.ajouterButton);
        modifierButton =(Button) findViewById(R.id.modifierButton);
        supprimerButton =(Button) findViewById(R.id.supprimerButton);
        matriculeLinearLayout =(LinearLayout) findViewById(R.id.matriculeLinearLayout);










        //Cette partie si l'utilisateur est connecté
        profileLinearLayout.setVisibility(View.VISIBLE);
        //loginAcceuilLinearLayout.setVisibility(View.GONE);
        // To retrieve object in second Activity
        id_Utilisateur = (String) getIntent().getStringExtra("id_Utilisateur");
        email = (String) getIntent().getStringExtra("email");
        nom = (String) getIntent().getStringExtra("nom");
        prenom = (String) getIntent().getStringExtra("prenom");
        cin = (String) getIntent().getStringExtra("cin");
        matricule = (String) getIntent().getStringExtra("matricule");
        motdepasse = (String) getIntent().getStringExtra("motdepasse");
        telephone = (String) getIntent().getStringExtra("telephone");
        image = (String) getIntent().getStringExtra("image");
        cover = (String) getIntent().getStringExtra("cover");
        date_naissance = (String) getIntent().getStringExtra("date_naissance");
        date_inscription = (String) getIntent().getStringExtra("date_inscription");
        role = (String) getIntent().getStringExtra("role");


        System.out.println("UtilisateurDetailActivity:id_Utilisateur:"+id_Utilisateur);
        System.out.println("UtilisateurDetailActivity:nom:"+nom);


        //Afficher le button Ajouter, Modifier et Supprimer dans la ligne de la liste des utilisateurs
        try{
            if (role!=null){
                if (!role.equals("")){
                    if (role.equals("Invite")) {
                        ajouterButton.setVisibility(View.VISIBLE);
                        modifierButton.setVisibility(View.GONE);
                        supprimerButton.setVisibility(View.GONE);
                    } else if (role.equals("Employee")) {
                        ajouterButton.setVisibility(View.GONE);
                        modifierButton.setVisibility(View.VISIBLE);
                        supprimerButton.setVisibility(View.VISIBLE);
                    }
                }
            }
        }catch (Exception e){
            ajouterButton.setVisibility(View.VISIBLE);
            modifierButton.setVisibility(View.GONE);
            supprimerButton.setVisibility(View.GONE);
        }


        setMyTextView(nom+" "+prenom,textViewNomEtPrenom);
        setMyTextView(email,textViewEmail);
        setMyTextView(telephone,textViewTelephone);
        setMyTextView(cin,textViewCin);
        setMyTextView(matricule,textViewMatricule);
        setMyTextView(date_naissance,textViewDateDeNaissance);
        setMyTextView(date_inscription,textViewDateInscription);


        if(!(image==null)) {
            if (!image.equals("")) {
                try {
                    //if image is received then set
                    Picasso.get().load(image).into(imageViewImageProfile);
                } catch (Exception e) {
                    e.printStackTrace();
                    //if there is any exception while getting image then set default
                    Picasso.get().load(R.drawable.ic_baseline_photo_camera_24).into(imageViewImageProfile);
                }
            }
        }

        if(!(cover==null)) {
            if (!cover.equals("")) {
                try {
                    //if image is received then set
                    Picasso.get().load(cover).into(imageViewCover);
                } catch (Exception e) {
                    e.printStackTrace();
                    //if there is any exception while getting image then set default
                    //Picasso.get().load(R.drawable.ic_baseline_face_white_24).into(coverIv);
                }
            }
        }


        DatabaseReference reference_utilisateur = FirebaseDatabase.getInstance().getReference("users");
        ajouterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Modifier la valeur de "role" de "Invite" en "Employee"
                //custom dialog
                //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                //AlertDialog.Builder builder = new AlertDialog.Builder(context);
                AlertDialog.Builder builder = new AlertDialog.Builder(UtilisateurDetailActivity.this);
                builder.setTitle("Ajouter Employé");
                builder.setMessage("Voulez vous ajouter l'utilisateur "+nom+" "+prenom+" à la liste des employés.");
                //set  layout of dialog
                //LinearLayout linearLayout = new LinearLayout(context);
                //LinearLayout linearLayout = new LinearLayout(getActivity());
                //LinearLayout linearLayout = new LinearLayout(this);
                LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setPadding(10,10,10,10);
                //add edit text
                //final EditText editText = new EditText(this);
                //final EditText editText = new EditText(context);
                //final EditText editText = new EditText(getActivity());
                final EditText editText = new EditText(getApplicationContext());
                //editText.setHint("Enter "+key);
                editText.setHint("Entrer la matricule de l'employé");
                linearLayout.addView(editText);/*final TextView textView = new TextView(context);
               textView.setText("Voulez vous approuver le congé de "+show_name);
               linearLayout.addView(textView);*/
                builder.setView(linearLayout);
                //add button in dialog to update
                builder.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //input text from edit text
                        final String valueMatricule = editText.getText().toString().trim();
                        //Validate if user has entered something or not
                        if(!TextUtils.isEmpty(valueMatricule)){
                            //reference_utilisateur.addValueEventListener(new ValueEventListener(){
                            reference_utilisateur.addListenerForSingleValueEvent(new ValueEventListener(){
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                                        reference_utilisateur.child(id_Utilisateur).child("role").setValue("Employee");
                                        reference_utilisateur.child(id_Utilisateur).child("matricule").setValue(valueMatricule);
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError)    {
                                    //Toast.makeText(getActivity(),"Error"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(context,"Erreur"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                    Toast.makeText(UtilisateurDetailActivity.this,"Error"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                            finish();
                            //((Activity)context).finish();
                            //getActivity().finish();
                            //context.startActivity(getIntent());
                            //startActivity(getActivity().getIntent());
                            //context.startActivity(((Activity) context).getIntent());
                            startActivity(getIntent());
                        }
                        else {
                            //Toast.makeText(ProfileUtilisateurActivity.this,"Please enter "+key,Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getActivity(),"S'il vous plait entrer "+key,Toast.LENGTH_SHORT).show();
                            //Toast.makeText(ProfileActivity.this,"S'il vous plait entrer "+key,Toast.LENGTH_SHORT).show();
                            //Toast.makeText(context,"S'il vous plait entrer la matricule",Toast.LENGTH_SHORT).show();
                            Toast.makeText(UtilisateurDetailActivity.this,"S'il vous plait entrer la matricule",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //add button in dialog to cancel
                //builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                //create and show dialog
                builder.create().show();
            }
        });


        modifierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Modifier la valeur de "role" de "Invite" en "Employee"
                //custom dialog
                //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                //AlertDialog.Builder builder = new AlertDialog.Builder(context);
                AlertDialog.Builder builder = new AlertDialog.Builder(UtilisateurDetailActivity.this);
                builder.setTitle("Modifier Employé");
                builder.setMessage("Voulez vous modifier la matricule de l'utilisateur "+nom+" "+prenom+".");
                //set  layout of dialog
                //LinearLayout linearLayout = new LinearLayout(context);
                //LinearLayout linearLayout = new LinearLayout(getActivity());
                //LinearLayout linearLayout = new LinearLayout(this);
                LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setPadding(10,10,10,10);
                //add edit text
                //final EditText editText = new EditText(this);
                //final EditText editText = new EditText(context);
                //final EditText editText = new EditText(getActivity());
                final EditText editText = new EditText(getApplicationContext());
                //editText.setHint("Enter "+key);
                editText.setHint("Entrer la matricule de l'employé");
                linearLayout.addView(editText);/*final TextView textView = new TextView(context);
               textView.setText("Voulez vous approuver le congé de "+show_name);
               linearLayout.addView(textView);*/
                builder.setView(linearLayout);
                //add button in dialog to update
                builder.setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //input text from edit text
                        final String valueMatricule = editText.getText().toString().trim();
                        //Validate if user has entered something or not
                        if(!TextUtils.isEmpty(valueMatricule)){
                            //reference_utilisateur.addValueEventListener(new ValueEventListener(){
                            reference_utilisateur.addListenerForSingleValueEvent(new ValueEventListener(){
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                                        //reference_utilisateur.child(id_Utilisateur).child("role").setValue("Employee");
                                        reference_utilisateur.child(id_Utilisateur).child("matricule").setValue(valueMatricule);
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError)    {
                                    //Toast.makeText(getActivity(),"Error"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(context,"Erreur"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                    Toast.makeText(UtilisateurDetailActivity.this,"Error"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                            finish();
                            //((Activity)context).finish();
                            //getActivity().finish();
                            //context.startActivity(getIntent());
                            //startActivity(getActivity().getIntent());
                            //context.startActivity(((Activity) context).getIntent());
                            startActivity(getIntent());
                        }
                        else {
                            //Toast.makeText(ProfileUtilisateurActivity.this,"Please enter "+key,Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getActivity(),"S'il vous plait entrer "+key,Toast.LENGTH_SHORT).show();
                            //Toast.makeText(ProfileActivity.this,"S'il vous plait entrer "+key,Toast.LENGTH_SHORT).show();
                            //Toast.makeText(context,"S'il vous plait entrer la matricule",Toast.LENGTH_SHORT).show();
                            Toast.makeText(UtilisateurDetailActivity.this,"S'il vous plait entrer la matricule",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //add button in dialog to cancel
                //builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                //create and show dialog
                builder.create().show();
            }
        });


        supprimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Modifier la valeur de "role" de "Employee" en "Invite"
                //custom dialog
                //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                //AlertDialog.Builder builder = new AlertDialog.Builder(context);
                AlertDialog.Builder builder = new AlertDialog.Builder(UtilisateurDetailActivity.this);
                builder.setTitle("Supprimer Employé");
                builder.setMessage("Voulez vous supprimer l'utilisateur "+nom+" "+prenom+" de la liste des employés.");
                //set  layout of dialog
                //LinearLayout linearLayout = new LinearLayout(context);
                //LinearLayout linearLayout = new LinearLayout(getActivity());
                //LinearLayout linearLayout = new LinearLayout(this);
                LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setPadding(10,10,10,10);/*//add edit text
               //final EditText editText = new EditText(this);
               final EditText editText = new EditText(context);
               //final EditText editText = new EditText(getActivity());
               //editText.setHint("Enter "+key);
               editText.setHint("Entrer la matricule de l'employé");
               linearLayout.addView(editText);
               final TextView textView = new TextView(context);
               textView.setText("Voulez vous approuver le congé de "+show_name);
               linearLayout.addView(textView);*/
                builder.setView(linearLayout);
                //add button in dialog to update
                builder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //reference_utilisateur.addValueEventListener(new ValueEventListener(){
                        reference_utilisateur.addListenerForSingleValueEvent(new ValueEventListener(){
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                                for(DataSnapshot ds:dataSnapshot.getChildren()){
                                    System.out.println("UtilisateurDetailActivity111");
                                    reference_utilisateur.child(id_Utilisateur).child("role").setValue("Invite");
                                    reference_utilisateur.child(id_Utilisateur).child("matricule").setValue("");
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError)    {
                                //Toast.makeText(getActivity(),"Error"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                //Toast.makeText(context,"Erreur"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                Toast.makeText(UtilisateurDetailActivity.this,"Error"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                        finish();
                        //((Activity)context).finish();
                        //getActivity().finish();
                        //context.startActivity(getIntent());
                        //startActivity(getActivity().getIntent());
                        //context.startActivity(((Activity) context).getIntent());
                        startActivity(getIntent());
                    }
                });
                //add button in dialog to cancel
                //builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                //create and show dialog
                builder.create().show();
            }
        });





    }



    public void setMyTextView(String myString,TextView myTextview) {
        try{
            if (myString!=null){
                if (!myString.equals("")){
                    myTextview.setText(myString);
                }else{
                    myTextview.setText("");
                }
            }
        }catch (Exception e){
            myTextview.setText("");
        }
    }




}