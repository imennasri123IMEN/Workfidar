package com.pointage1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pointage1.Model.Invite;
import com.pointage1.Utilities.ValidationUtilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextNom;
    private EditText editTextPrenom;
    private EditText editTextEmail;
    private EditText editTextMotDePasse;
    private EditText editTextMotDePasseConfirmer;
    private Button buttonRegister;
    private Button buttonSuivant;
    private Button buttonSuivantStep2;
    private LinearLayout linearLayoutStep1;
    private LinearLayout linearLayoutStep2;
    private LinearLayout linearLayoutStep3;
    private TextView textViewStep1;
    private TextView textViewStep2;
    private TextView textViewStep3;
    private ImageView checkCircleImageViewStep1;
    private ImageView checkCircleImageViewStep2;
    private ImageView checkCircleImageViewStep3;
    //private TextView textViewAvoirUnCompte;
    //private EditText editTextBirthDate;
    private Calendar myCalendar= Calendar.getInstance();

    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private String motDePasseConfirmer;
    private String dateDeNaissance;
    private String datedinscription;
    private String cin;
    private String matricule;
    private String telephone;
    private String image;
    private String cover;



    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference usersReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        editTextNom = findViewById(R.id.editTextNom);
        editTextPrenom = findViewById(R.id.editTextPrenom);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextMotDePasse = findViewById(R.id.editTextMotDePasse);
        editTextMotDePasseConfirmer = findViewById(R.id.editTextMotDePasseConfirmer);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonSuivant = findViewById(R.id.buttonSuivant);
        buttonSuivantStep2 = findViewById(R.id.buttonSuivantStep2);
        linearLayoutStep1 = findViewById(R.id.linearLayoutStep1);
        linearLayoutStep2 = findViewById(R.id.linearLayoutStep2);
        linearLayoutStep3 = findViewById(R.id.linearLayoutStep3);
        textViewStep1 = findViewById(R.id.textViewStep1);
        textViewStep2 = findViewById(R.id.textViewStep2);
        textViewStep3 = findViewById(R.id.textViewStep3);
        checkCircleImageViewStep1 = findViewById(R.id.checkCircleImageViewStep1);
        checkCircleImageViewStep2 = findViewById(R.id.checkCircleImageViewStep2);
        checkCircleImageViewStep3 = findViewById(R.id.checkCircleImageViewStep3);
        //textViewAvoirUnCompte = findViewById(R.id.textViewAvoirUnCompte);





        /*textViewAvoirUnCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });*/




        // Firebase Authentication and Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        usersReference = mDatabase.getReference("users");



        buttonSuivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step2();
            }
        });
        buttonSuivantStep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step3();
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerOnClick();
            }
        });

    }


    public void step2() {

        if (validRegisterForm1()) {
            linearLayoutStep1.setVisibility(View.GONE);
            linearLayoutStep2.setVisibility(View.VISIBLE);
            linearLayoutStep3.setVisibility(View.GONE);

            textViewStep1.setVisibility(View.GONE);
            checkCircleImageViewStep1.setVisibility(View.VISIBLE);

            textViewStep2.setTextColor(Color.parseColor("#ef4737"));
            textViewStep2.setBackground(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.circle));

        }

    }
    public void step3() {

        if (validRegisterForm2()) {
            linearLayoutStep1.setVisibility(View.GONE);
            linearLayoutStep2.setVisibility(View.GONE);
            linearLayoutStep3.setVisibility(View.VISIBLE);

            textViewStep2.setVisibility(View.GONE);
            checkCircleImageViewStep2.setVisibility(View.VISIBLE);

            textViewStep3.setTextColor(Color.parseColor("#ef4737"));
            textViewStep3.setBackground(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.circle));
        }
    }


    public void registerOnClick() {

        // Fetch all field values
        nom = editTextNom.getText().toString().trim();
        prenom = editTextPrenom.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();
        motDePasse = editTextMotDePasse.getText().toString().trim();
        motDePasseConfirmer = editTextMotDePasseConfirmer.getText().toString().trim();
        //dateDeNaissance = editTextBirthDate.getText().toString().trim();


        dateDeNaissance="";

        //Date du jour
        //initialisation pour ne pas être null
        //datedinscription="2022/03/12 00:00:00";
        datedinscription="2022/03/12";
        DateTimeFormatter dtf = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            System.out.println("yyyy/MM/dd -> "+dtf.format(LocalDateTime.now()));

            datedinscription=dtf.format(LocalDateTime.now());
        }

        //initialisation
        cin="";
        matricule="";
        telephone="";
        image="";
        cover="";



        if (validRegisterForm3()) {
            mAuth.createUserWithEmailAndPassword(email, motDePasse)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                final String id = mAuth.getCurrentUser().getUid();
                                //final Utilisateur nouveauUtilisateur = new Utilisateur(id, email, nom, prenom,cin,motDePasse,telephone,image,cover,dateDeNaissance,datedinscription);
                                final Invite nouveauUtilisateur = new Invite(id, email, nom, prenom,cin,matricule,motDePasse,telephone,image,cover,dateDeNaissance,datedinscription);

                                //cover image
                                usersReference
                                        .child(mAuth.getCurrentUser().getUid()).setValue(nouveauUtilisateur)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {


                                                    //Go To Login Page
                                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                    startActivity(intent);
                                                    //finish();
                                                    Toast.makeText(getApplicationContext(), "Successfully registered account!", Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Firebase Database Error!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthWeakPasswordException e) {
                                    editTextMotDePasse.setError(e.getMessage());
                                    editTextMotDePasse.requestFocus();
                                } catch (FirebaseAuthUserCollisionException e) {
                                    editTextEmail.setError(e.getMessage());
                                    editTextEmail.requestFocus();
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });


        }




    }


    public boolean validRegisterForm1() {
        boolean valid = true;


        String email = editTextEmail.getText().toString().trim();



        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Le champ email ne peut pas être vide.");
            valid = false;
        } else if (!ValidationUtilities.isValidEmail(email)) {
            editTextEmail.setError("L'email donné est invalide.");
            valid = false;
        }

        //Le champ date de naissance n'est pas obligatoire

        return valid;
    }

    public boolean validRegisterForm2() {
        boolean valid = true;

        String nom = editTextNom.getText().toString().trim();
        String prenom = editTextPrenom.getText().toString().trim();

        if (TextUtils.isEmpty(nom)) {
            editTextNom.setError("Le champ nom ne peut pas être vide.");
            valid = false;
        } else if (!ValidationUtilities.isValidName(nom)) {
            editTextNom.setError("Le nom donné est invalide.");
            valid = false;
        }


        if (TextUtils.isEmpty(prenom)) {
            editTextPrenom.setError("Le champ prénom ne peut pas être vide.");
            valid = false;
        } else if (!ValidationUtilities.isValidName(prenom)) {
            editTextPrenom.setError("Le prénom donné est invalide.");
            valid = false;
        }



        return valid;
    }

    public boolean validRegisterForm3() {
        boolean valid = true;


        String motDePasse = editTextMotDePasse.getText().toString().trim();
        String motDePasseConfirmer = editTextMotDePasseConfirmer.getText().toString().trim();


        if (TextUtils.isEmpty(motDePasse)) {
            editTextMotDePasse.setError("Le champ mot de passe ne peut pas être vide.");
            valid = false;
        } else if (motDePasse.length() < 6) {
            editTextMotDePasse.setError("Le mot de passe donné est invalide. [Le mot de passe doit comporter au moins 6 caractères]");
            valid = false;
        }

        if (TextUtils.isEmpty(motDePasseConfirmer)) {
            editTextMotDePasseConfirmer.setError("Le champ confirme mot de passe ne peut pas être vide.");
            valid = false;
        } else if (!motDePasseConfirmer.equals(motDePasse)) {
            //editTextMotDePasse.setError("Les deux champs 'mot de passe' et 'confirme mot de passe' ne sont pas identiques");
            editTextMotDePasseConfirmer.setError("Les deux champs 'mot de passe' et 'confirme mot de passe' ne sont pas identiques");
            valid = false;
        }


        return valid;
    }


}