package com.pointage1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pointage1.Utilities.ValidationUtilities;

public class LoginActivity extends AppCompatActivity {



    private EditText editTextEmail;
    private EditText editTextMotDePasse;
    private CheckBox checkboxEnregistrer;
    private TextView textViewMotDePasseOublie;
    private Button buttonLogin;
    private TextView textViewCreerUnCompte;



    private String email;
    private String motDePasse;
    private ProgressDialog mProgressDialog;


    //Enregistrer Données de connexion
    private SharedPreferences loginPref;
    private SharedPreferences.Editor prefs;
    private boolean check;


    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference usersReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);




        editTextEmail = findViewById(R.id.editTextEmail);
        editTextMotDePasse = findViewById(R.id.editTextMotDePasse);
        checkboxEnregistrer = findViewById(R.id.checkboxEnregistrer);
        textViewMotDePasseOublie = findViewById(R.id.textViewMotDePasseOublie);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewCreerUnCompte = findViewById(R.id.textViewCreerUnCompte);


        textViewCreerUnCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        usersReference = mDatabase.getReference("users");



        loginPref =getSharedPreferences("loginPrefs",MODE_PRIVATE);
        prefs = loginPref.edit();
        check =loginPref.getBoolean("savelogin",false);
        if(check==true){
            editTextEmail.setText(loginPref.getString("username",""));
            editTextMotDePasse.setText(loginPref.getString("password",""));
            checkboxEnregistrer.setChecked(true);
        }

        textViewMotDePasseOublie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPasswordDialog();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOnClick();
            }
        });


    }


    private void showRecoverPasswordDialog(){

        // AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Récupérer mot de passe");

        // set layout linear layout
        LinearLayout linearLayout =new LinearLayout(this);
        // views to set in dialog
        final EditText emailEt = new EditText(this);
        emailEt.setHint("Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);


        linearLayout.addView(emailEt);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);

        //Button recovery
        builder.setPositiveButton("Récupérer",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //input email
                String email = emailEt.getText().toString().trim();
                beginRecovery(email);
            }
        });


        //Button cancel
        builder.setNegativeButton("Annuler",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //dismiss dialog
                dialog.dismiss();
            }
        });
        //show dialog
        builder.create().show();
    }

    private void beginRecovery(String email){


        //pd.setMessage("Logging In ...");
        //pd.show();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>(){
            @Override
            public void onComplete(@NonNull Task<Void> task){

                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Email envoyé", Toast.LENGTH_SHORT).show();

                }else{

                    Toast.makeText(LoginActivity.this,"Échoué", Toast.LENGTH_SHORT).show();
                }
            }

        }).addOnFailureListener(new OnFailureListener(){
            @Override
            public void onFailure (@NonNull Exception e){
                Toast.makeText(LoginActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

        //pd.setMessage("Logging In ...");
        //pd.show();

    }


    private void setRememberMeMethod(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextEmail.getWindowToken(),0);
        email = editTextEmail.getText().toString();
        motDePasse = editTextMotDePasse.getText().toString();

        if(checkboxEnregistrer.isChecked()){
            prefs.putBoolean("savelogin",true);
            prefs.putString("username",email);
            prefs.putString("password",motDePasse);
            prefs.commit();

        }
        else{
            prefs.clear();
            prefs.commit();
        }


    }



    public void loginOnClick() {
        setRememberMeMethod();

        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextMotDePasse.getText().toString().trim();

        mProgressDialog=new ProgressDialog(this);
        //mProgressDialog.setTitle("Loading user data");
        mProgressDialog.setTitle("Authentification");
        //mProgressDialog.setMessage("Please wait while we load the user data");
        mProgressDialog.setMessage("Veuillez patienter pendant que l'utilisateur se connecte");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        if (validLoginForm()) {
            if (ValidationUtilities.isValidAdmin(email, password)) {
                Intent intent = new Intent(getApplicationContext(), AdminMainActivity.class);
                startActivity(intent);
                mProgressDialog.dismiss();
            } else {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull final Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    //if user is signing in first time then get and show user info from google account
                                    /*
                                    if(task.getResult().getAdditionalUserInfo().isNewUser()){

                                    }*/

                                    final String userId = task.getResult().getUser().getUid();
                                    usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            boolean userWasDeleted = true; //To solve not being able to delete user from admin account
                                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                if (data.getKey().equals(userId)) {
                                                    /*userWasDeleted = false;
                                                    final String prenom = data.child("prenom").getValue().toString();
                                                    Intent intent;
                                                    //intent = new Intent(getApplicationContext(), HomeActivity.class);
                                                    intent = new Intent(getApplicationContext(), AcceuilActivity.class);
                                                    intent.putExtra("userId", userId);
                                                    intent.putExtra("bienvenueMessage", "Bienvenue " + prenom + "!");
                                                    startActivity(intent);*/
                                                    userWasDeleted = false;
                                                    final String prenom = data.child("prenom").getValue().toString();
                                                    final String role = data.child("role").getValue().toString();
                                                    Intent intent;
                                                    if (role.equals("Employee")) {
                                                        intent = new Intent(getApplicationContext(), AcceuilActivity.class);
                                                        intent.putExtra("userId", userId);
                                                        intent.putExtra("bienvenueMessage", "Bienvenue " + prenom + "!");

                                                    } else {
                                                        //intent = new Intent(getApplicationContext(), AccueilInviteActivity.class);
                                                        intent = new Intent(getApplicationContext(), AccueilInviteActivity.class);
                                                        intent.putExtra("userId", userId);
                                                        intent.putExtra("bienvenueMessage", "Bienvenue " + prenom + "!");

                                                    }

                                                    startActivity(intent);
                                                }
                                            }
                                            if (userWasDeleted) {
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                user.delete()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(getApplicationContext(), "Cet utilisateur n'existe plus.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                            }

                                            mProgressDialog.dismiss();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            Toast.makeText(getApplicationContext(), "Erreur de base de données Firebase.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    try {
                                        throw task.getException();
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    mProgressDialog.dismiss();
                                }
                            }
                        });
            }
        }
    }



    public boolean validLoginForm() {

        boolean valid = true;

        String email = editTextEmail.getText().toString().trim();
        String password = editTextMotDePasse.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Le champ email ne peut pas être vide.");
            valid = false;
        } else if (!ValidationUtilities.isValidEmail(email)) {
            editTextEmail.setError("L'email donné est invalide.");
            valid = false;
        }

        if (TextUtils.isEmpty(password)) {
            editTextMotDePasse.setError("Le champ mot de passe ne peut pas être vide.");
            valid = false;
        } else if (password.length() < 6) {
            editTextMotDePasse.setError("Le mot de passe donné est invalide. [Le mot de passe doit comporter au moins 6 caractères]");
            valid = false;
        }

        return valid;
    }




}