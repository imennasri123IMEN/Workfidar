package com.pointage1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pointage1.Model.Conge;

public class CongeDetailActivity extends AppCompatActivity {

    private TextView nomTextView;
    private TextView prenomTextView;
    private TextView editnbjoursTextView;
    private TextView dateDeDébutTextView;
    private TextView dateDeFinTextView;
    private TextView causeTextView;
    private Button approuverButton;
    private Button rejecterButton;
    private LinearLayout approuverRejeterLinearLayout;
    private LinearLayout statusCongeLinearLayout;
    private ImageView statusCongeApprouveImageView;
    private ImageView statusCongeRejeteImageView;
    private TextView statusCongeTextView;

    //Conge myConge;
    private String nom;
    private String prenom;
    private String id_Utilisateur;
    private String id_Conge;
    private String show_reason;
    private String dateDeDébut;
    private String dateDeFin;
    private String show_leaves;
    private String statusConge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conge_detail);


        nomTextView =(TextView) findViewById(R.id.nomTextView);
        prenomTextView =(TextView) findViewById(R.id.prenomTextView);
        editnbjoursTextView =(TextView) findViewById(R.id.editnbjoursTextView);
        dateDeDébutTextView =(TextView) findViewById(R.id.dateDeDébutTextView);
        dateDeFinTextView =(TextView) findViewById(R.id.dateDeFinTextView);
        causeTextView =(TextView) findViewById(R.id.causeTextView);
        approuverButton =(Button) findViewById(R.id.approuverButton);
        rejecterButton =(Button) findViewById(R.id.rejecterButton);
        approuverRejeterLinearLayout =(LinearLayout) findViewById(R.id.approuverRejeterLinearLayout);
        statusCongeLinearLayout =(LinearLayout) findViewById(R.id.statusCongeLinearLayout);
        statusCongeApprouveImageView =(ImageView) findViewById(R.id.statusCongeApprouveImageView);
        statusCongeRejeteImageView =(ImageView) findViewById(R.id.statusCongeRejeteImageView);
        statusCongeTextView =(TextView) findViewById(R.id.statusCongeTextView);


        // To retrieve object in second Activity
        //myConge = (Conge) getIntent().getSerializableExtra("KEY_NAME");
        nom = (String) getIntent().getStringExtra("nom");
        prenom = (String) getIntent().getStringExtra("prenom");
        id_Utilisateur = (String) getIntent().getStringExtra("id_Utilisateur");
        id_Conge = (String) getIntent().getStringExtra("id_Conge");
        show_reason = (String) getIntent().getStringExtra("show_reason");
        dateDeDébut = (String) getIntent().getStringExtra("dateDeDébut");
        dateDeFin = (String) getIntent().getStringExtra("dateDeFin");
        show_leaves = (String) getIntent().getStringExtra("show_leaves");
        statusConge = (String) getIntent().getStringExtra("statusConge");


        setMyTextView(nom,nomTextView);
        setMyTextView(prenom,prenomTextView);
        setMyTextView(show_reason,causeTextView);
        setMyTextView(dateDeDébut,dateDeDébutTextView);
        setMyTextView(dateDeFin,dateDeFinTextView);
        setMyTextView(show_leaves,editnbjoursTextView);


        System.out.println("CongeDetailActivity:nom:"+nom);
        System.out.println("CongeDetailActivity:prenom:"+prenom);
        System.out.println("CongeDetailActivity:show_reason:"+show_reason);



        approuverRejeterLinearLayout =(LinearLayout) findViewById(R.id.approuverRejeterLinearLayout);
        statusCongeLinearLayout =(LinearLayout) findViewById(R.id.statusCongeLinearLayout);
        statusCongeApprouveImageView =(ImageView) findViewById(R.id.statusCongeApprouveImageView);
        statusCongeRejeteImageView =(ImageView) findViewById(R.id.statusCongeRejeteImageView);
        statusCongeTextView =(TextView) findViewById(R.id.statusCongeTextView);


        try{
            if (statusConge!=null){
                if (statusConge.equals("En demande")){

                    statusCongeLinearLayout.setVisibility(View.GONE);
                    statusCongeTextView.setVisibility(View.GONE);
                    approuverRejeterLinearLayout.setVisibility(View.VISIBLE);

                }else if(statusConge.equals("Approuver")){

                    statusCongeTextView.setText("Approuvé");

                    statusCongeLinearLayout.setVisibility(View.VISIBLE);
                    statusCongeApprouveImageView.setVisibility(View.VISIBLE);
                    statusCongeRejeteImageView.setVisibility(View.GONE);

                    statusCongeTextView.setVisibility(View.VISIBLE);
                    approuverRejeterLinearLayout.setVisibility(View.GONE);

                }else if(statusConge.equals("Rejecter")){
                    statusCongeTextView.setText("Rejeté");

                    statusCongeLinearLayout.setVisibility(View.VISIBLE);
                    statusCongeApprouveImageView.setVisibility(View.GONE);
                    statusCongeRejeteImageView.setVisibility(View.VISIBLE);

                    statusCongeTextView.setVisibility(View.VISIBLE);
                    approuverRejeterLinearLayout.setVisibility(View.GONE);
                }
            }
        }catch (Exception e){

            statusCongeLinearLayout.setVisibility(View.GONE);
            statusCongeTextView.setVisibility(View.GONE);
            approuverRejeterLinearLayout.setVisibility(View.VISIBLE);

        }







        DatabaseReference reference_conge = FirebaseDatabase.getInstance().getReference("conge");
        approuverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Modifier la valeur de "statusConge" de "Conge" en "Approuver"


                //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                //AlertDialog.Builder builder = new AlertDialog.Builder(context);
                AlertDialog.Builder builder = new AlertDialog.Builder(CongeDetailActivity.this);
                builder.setTitle("Approuver Congé");
                builder.setMessage("Voulez vous approuver le congé de "+nom+" "+prenom);


                //set  layout of dialog
                //LinearLayout linearLayout = new LinearLayout(context);
                //LinearLayout linearLayout = new LinearLayout(getActivity());
                LinearLayout linearLayout = new LinearLayout(CongeDetailActivity.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setPadding(10,10,10,10);





                builder.setView(linearLayout);

                //add button in dialog to update
                builder.setPositiveButton("Approuver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //reference_conge.addValueEventListener(new ValueEventListener(){
                        reference_conge.addListenerForSingleValueEvent(new ValueEventListener(){
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                                for(DataSnapshot ds:dataSnapshot.getChildren()){
                                    reference_conge.child(id_Conge).child("statusConge").setValue("Approuver");
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError)    {
                                //Toast.makeText(getActivity(),"Error"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                //Toast.makeText(context,"Error"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                Toast.makeText(CongeDetailActivity.this,"Error"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });

                        //((Activity)context).finish();
                        //getActivity().finish();
                        finish();
                        //context.startActivity(getIntent());
                        //context.startActivity(((Activity) context).getIntent());
                        //startActivity(getActivity().getIntent());
                        //startActivity(getIntent());
                        Intent intent = new Intent(CongeDetailActivity.this, CongeAdminActivity.class);
                        startActivity(intent);


                    }
                });

                builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                //create and show dialog
                builder.create().show();
            }
        });



        rejecterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Modifier la valeur de "statusConge" de "Conge" en "Rejecter"


                //custom dialog
                //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                //AlertDialog.Builder builder = new AlertDialog.Builder(context);
                AlertDialog.Builder builder = new AlertDialog.Builder(CongeDetailActivity.this);
                builder.setTitle("Rejecter Congé");

                builder.setMessage("Voulez vous rejecter le congé de "+nom+" "+prenom);

                //set  layout of dialog
                //LinearLayout linearLayout = new LinearLayout(context);
                //LinearLayout linearLayout = new LinearLayout(getActivity());
                LinearLayout linearLayout = new LinearLayout(CongeDetailActivity.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setPadding(10,10,10,10);





                builder.setView(linearLayout);

                //add button in dialog to update
                //builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                builder.setPositiveButton("Rejecter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //reference_conge.addValueEventListener(new ValueEventListener(){
                        reference_conge.addListenerForSingleValueEvent(new ValueEventListener(){
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                                for(DataSnapshot ds:dataSnapshot.getChildren()){
                                    reference_conge.child(id_Conge).child("statusConge").setValue("Rejecter");
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError)    {
                                //Toast.makeText(getActivity(),"Error"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                //Toast.makeText(context,"Error"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                Toast.makeText(CongeDetailActivity.this,"Error"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                        //((Activity)context).finish();
                        //getActivity().finish();
                        finish();
                        //context.startActivity(getIntent());
                        //context.startActivity(((Activity) context).getIntent());
                        //startActivity(getActivity().getIntent());
                        //startActivity(getIntent());
                        Intent intent = new Intent(CongeDetailActivity.this, CongeAdminActivity.class);
                        startActivity(intent);


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




        /*try{
            if (nom!=null){
                if (!nom.equals("")){
                    nomTextView.setText(nom);
                }else{
                    nomTextView.setText("");
                }
            }
        }catch (Exception e){
            nomTextView.setText("");
        }

        try{
            if (prenom!=null){
                if (!prenom.equals("")){
                    prenomTextView.setText(prenom);
                }else{
                    prenomTextView.setText("");
                }
            }
        }catch (Exception e){
            prenomTextView.setText("");
        }


        try{
            if (show_reason!=null){
                if (!show_reason.equals("")){
                    causeTextView.setText(show_reason);
                }else{
                    causeTextView.setText("");
                }
            }
        }catch (Exception e){
            causeTextView.setText("");
        }


        try{
            if (dateDeDébut!=null){
                if (!dateDeDébut.equals("")){
                    dateDeDébutTextView.setText(dateDeDébut);
                }else{
                    dateDeDébutTextView.setText("");
                }
            }
        }catch (Exception e){
            dateDeDébutTextView.setText("");
        }


        try{
            if (dateDeFin!=null){
                if (!dateDeFin.equals("")){
                    dateDeFinTextView.setText(dateDeFin);
                }else{
                    dateDeFinTextView.setText("");
                }
            }
        }catch (Exception e){
            dateDeFinTextView.setText("");
        }


        try{
            if (show_leaves!=null){
                if (!show_leaves.equals("")){
                    editnbjoursTextView.setText(show_leaves);
                }else{
                    editnbjoursTextView.setText("");
                }
            }
        }catch (Exception e){
            editnbjoursTextView.setText("");
        }*/




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