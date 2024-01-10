package com.pointage1;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pointage1.Model.Conge;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class CongeActivity extends AppCompatActivity {


    //private EditText edit_name;
    //private EditText edit_prenom ;
    private EditText edit_cause ;
    //private EditText edit_nbjours;
    //private EditText edit_crédit ;
    private EditText dateDeDébut ;
    private EditText dateDeFin ;

    private Calendar myCalendar_dateDeDébut= Calendar.getInstance();

    private Calendar myCalendar_dateDeFin= Calendar.getInstance();

    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private boolean connectedUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conge);
        //edit_name =findViewById(R.id.editTextNom);
        //edit_prenom =findViewById(R.id.editTextPrenom);
        edit_cause =findViewById(R.id.cause);
        //edit_nbjours =findViewById(R.id.editnbjours);
        //edit_crédit =findViewById(R.id.editTextcredit);
        dateDeDébut =findViewById(R.id.dateDeDébut);
        dateDeFin =findViewById(R.id.dateDeFin);


        // Firebase Authentication and Database
        mAuth = FirebaseAuth.getInstance();
        checkUserStatus();

        if (connectedUser==true) {
            DatePickerDialog.OnDateSetListener date_dateDeDébut = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                    myCalendar_dateDeDébut.set(Calendar.YEAR, year);
                    myCalendar_dateDeDébut.set(Calendar.MONTH, month);
                    myCalendar_dateDeDébut.set(Calendar.DAY_OF_MONTH, day);

                    updateLabelDateDeDébut();
                }
            };

            DatePickerDialog.OnDateSetListener date_dateDeFin = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                    myCalendar_dateDeFin.set(Calendar.YEAR, year);
                    myCalendar_dateDeFin.set(Calendar.MONTH, month);
                    myCalendar_dateDeFin.set(Calendar.DAY_OF_MONTH, day);
                    updateLabelDateDeFin();
                }
            };


            dateDeDébut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new DatePickerDialog(CongeActivity.this, date_dateDeDébut, myCalendar_dateDeDébut.get(Calendar.YEAR), myCalendar_dateDeDébut.get(Calendar.MONTH), myCalendar_dateDeDébut.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

            dateDeFin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new DatePickerDialog(CongeActivity.this, date_dateDeFin, myCalendar_dateDeFin.get(Calendar.YEAR), myCalendar_dateDeFin.get(Calendar.MONTH), myCalendar_dateDeFin.get(Calendar.DAY_OF_MONTH)).show();
                }
            });


            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel channel = new NotificationChannel("my notification ","my notification ", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);
            }



            Button btn = findViewById(R.id.souv);
            //Dao dao=new Dao();
            btn.setOnClickListener(v ->
            {

                if (validRegisterForm()) {

                    //id_Utilisateur est prise de l'uid du l'utilisateur connecté:
                    final String id_Utilisateur = mAuth.getCurrentUser().getUid();


                    //La fonction Random pour générer un id congé:
                    Random rand = new Random(); //instance of random class
                    int upperbound = 25000;
                    //generate random values from 0-25000
                    int int_random = rand.nextInt(upperbound);

                    String id_Conge = String.valueOf(int_random);

                    //initialisation pour ne pas être null
                    //String dateDeDemande="2022/03/12 00:00:00";
                    String dateDeDemande="2022/03/12";
                    DateTimeFormatter dtf = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        //dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                        System.out.println("yyyy/MM/dd -> "+dtf.format(LocalDateTime.now()));

                        dateDeDemande=dtf.format(LocalDateTime.now());
                    }

                    //statusConge peut être : En demande,Approuvé, Rejecté
                    String statusConge = "En demande";


                    //Conge nouveauConge = new Conge(id_Conge, edit_name.getText().toString(), edit_prenom.getText().toString(), edit_cause.getText().toString(), edit_nbjours.getText().toString(), edit_crédit.getText().toString(), statusConge);
                    //Conge nouveauConge = new Conge(id_Conge, edit_name.getText().toString(), edit_prenom.getText().toString(), edit_cause.getText().toString(),dateDeDébut.getText().toString() ,dateDeFin.getText().toString() , statusConge);
                    Conge nouveauConge = new Conge(id_Conge, id_Utilisateur, edit_cause.getText().toString(), dateDeDébut.getText().toString(), dateDeFin.getText().toString(),dateDeDemande, statusConge);

                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    databaseReference = db.getReference("conge");
                    //dao.add(conge)
                    databaseReference
                            .child(id_Conge).setValue(nouveauConge)
                            .addOnSuccessListener(suc ->
                            {

                                //Go To Login Page
                                Intent intent = new Intent(getApplicationContext(), AcceuilActivity.class);
                                startActivity(intent);


                                NotificationCompat.Builder builder = new NotificationCompat.Builder(CongeActivity.this,"my notification ");
                                builder.setContentTitle("Congé");
                                builder.setContentText("Votre demande de congé a été envoyé.");
                                builder.setSmallIcon(R.drawable.congee);
                                builder.setAutoCancel(true);
                                NotificationManagerCompat manageCompat = NotificationManagerCompat.from(CongeActivity.this);
                                manageCompat.notify(1,builder.build());



                                //Toast.makeText(this, "Record is inserted", Toast.LENGTH_SHORT).show();
                                Toast.makeText(this, "La demande du congé est envoyée", Toast.LENGTH_SHORT).show();
                            }).addOnFailureListener(er ->


                    {
                        //Toast.makeText(this, "Record is not inserted:" + er.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "Echec d'envoi de la demande du congé:" + er.getMessage(), Toast.LENGTH_SHORT).show();
                    });






                }

            });





        }

    }


    private void updateLabelDateDeDébut(){
        //String myFormat="MM/dd/yy";
        String myFormat="yyyy/MM/dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        dateDeDébut.setText(dateFormat.format(myCalendar_dateDeDébut.getTime()));
    }

    private void updateLabelDateDeFin(){
        //String myFormat="MM/dd/yy";
        String myFormat="yyyy/MM/dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        dateDeFin.setText(dateFormat.format(myCalendar_dateDeFin.getTime()));
    }



    private void checkUserStatus(){
        //get current user
        FirebaseUser user = mAuth.getCurrentUser();
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


    public boolean validRegisterForm() {
        boolean valid = true;



        /*String name = edit_name.getText().toString().trim();


        if (TextUtils.isEmpty(name)) {
            edit_name.setError("Le champ name ne peut pas être vide.");
            valid = false;
        }


        String prenom = edit_prenom.getText().toString().trim();

        if (TextUtils.isEmpty(prenom)) {
            edit_prenom.setError("Le champ prenom ne peut pas être vide.");
            valid = false;
        }*/


        String cause = edit_cause.getText().toString().trim();

        if (TextUtils.isEmpty(cause)) {
            edit_cause.setError("Le champ cause ne peut pas être vide.");
            valid = false;
        }


        /*String nbjours = edit_nbjours.getText().toString().trim();

        if (TextUtils.isEmpty(nbjours)) {
            edit_nbjours.setError("Le champ nombre de jour ne peut pas être vide.");
            valid = false;
        }


        String crédit = edit_crédit.getText().toString().trim();

        if (TextUtils.isEmpty(crédit)) {
            edit_crédit.setError("Le champ crédit ne peut pas être vide.");
            valid = false;
        }*/

        String nbjours = dateDeDébut.getText().toString().trim();

        if (TextUtils.isEmpty(nbjours)) {
            dateDeDébut.setError("Le champ date de début du congé ne peut pas être vide.");
            valid = false;
        }


        String crédit = dateDeFin.getText().toString().trim();

        if (TextUtils.isEmpty(crédit)) {
            dateDeFin.setError("Le champ date de fin du congé ne peut pas être vide.");
            valid = false;
        }


        //Vérifier si la date de début du congé n'est pas avant la date de fin de congé:
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy/MM/dd");
        Date d1 = null;
        Date d2 = null;
        /*Date d3 = null;
        String dateAujourdhui=null;
        DateTimeFormatter dtf = null;*/
        
        try {
            d1 = sdformat.parse(dateDeDébut.getText().toString());
            d2 = sdformat.parse(dateDeFin.getText().toString());

            System.out.println("The date 1 is: " + sdformat.format(d1));
            System.out.println("The date 2 is: " + sdformat.format(d2));
            if(d1.compareTo(d2) > 0) {
                System.out.println("Date 1 occurs after Date 2");
                dateDeFin.setError("La date de fin du congé ne doit pas être avant la date de début.");
                valid = false;
            } /*else if(d1.compareTo(d2) < 0) {
                System.out.println("Date 1 occurs before Date 2");
            } else if(d1.compareTo(d2) == 0) {
                System.out.println("Both dates are equal");
            }*/

            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dateAujourdhui=dtf.format(LocalDateTime.now());
            }
            d3=sdformat.parse(dateAujourdhui.toString());
            if(d3.compareTo(d2) > 0) {
                System.out.println("Date 3 occurs after Date 2");
                dateDeDébut.setError("La date de début du congé ne doit pas être avant la date de ce jour.");
                valid = false;
            }*/



        } catch (ParseException e) {
            e.printStackTrace();
        }





        return valid;
    }



}