package com.pointage1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.pointage1.Adapters.AdapterListUtilisateur;
import com.pointage1.Model.Utilisateur;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class UtilisateursActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterListUtilisateur adapterListUtilisateur;
    List<Utilisateur> utilisateurList;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilisateurs);


        recyclerView = findViewById(R.id.utilisateur_recyclerView);

        //Layout (LinearLayout) for RecyclerView
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        // recycler view properties:
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        System.out.println("UtilisateurAdmin900");
        utilisateurList=getUtilisateurList();

        System.out.println("UtilisateurAdmin1200");

        /*//Adapter
        adapterListUtilisateur =new AdapterListUtilisateur(getApplicationContext(),utilisateurList);
        //setAdapter
        recyclerView.setAdapter(adapterListUtilisateur);*/


    }

    public List<Utilisateur> getUtilisateurList() {
        utilisateurList=new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //utilisateurList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    Utilisateur utilisateur=ds.getValue(Utilisateur.class);

                    System.out.println("UtilisateurAdmin1000");
                    //Ajouter la ligne dans firebase dans la liste des utilisateurs
                    utilisateurList.add(utilisateur);


                    //adapter
                    //adapterListUtilisateur=new AdapterListUtilisateur(getApplicationContext(),utilisateurList);
                    adapterListUtilisateur=new AdapterListUtilisateur(UtilisateursActivity.this,utilisateurList);
                    //Pour faire mettre à jour le recyclerview si il ya des nouveaux éléments:
                    adapterListUtilisateur.notifyDataSetChanged();
                    //set Adapter to recycler view;
                    recyclerView.setAdapter(adapterListUtilisateur);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return utilisateurList;
    }


}