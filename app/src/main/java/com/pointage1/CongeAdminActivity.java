package com.pointage1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pointage1.Adapters.AdapterListConge;
import com.pointage1.Adapters.AdapterListUtilisateur;
import com.pointage1.Model.Conge;
import com.pointage1.Model.Utilisateur;

import java.util.ArrayList;
import java.util.List;

public class CongeAdminActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterListConge adapterListConge;
    List<Conge> congeList;
    DatabaseReference reference;

    List<Utilisateur> utilisateurList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conge_admin);

        recyclerView = findViewById(R.id.conge_recyclerView);

        //Layout (LinearLayout) for RecyclerView
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        // recycler view properties:
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        utilisateurList=getUtilisateurList();


        System.out.println("CongeAdmin900");
        congeList=getCongeList();

        System.out.println("CongeAdmin1200");

        /*//Adapter
        adapterListConge =new AdapterListConge(getApplicationContext(),congeList);
        //setAdapter
        recyclerView.setAdapter(adapterListConge);*/


    }

    public List<Conge> getCongeList() {
        congeList=new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("conge");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //congeList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    Conge conge=ds.getValue(Conge.class);

                    System.out.println("CongeAdmin1000");
                    //Avoir la liste des conges en demande
                    /*if(conge.getStatusConge().equals("En demande")) {
                        System.out.println("CongeAdmin1001");
                        congeList.add(conge);
                    }*/

                    congeList.add(conge);


                    //adapter
                    //adapterListConge=new AdapterListConge(getApplicationContext(),congeList);
                    adapterListConge=new AdapterListConge(CongeAdminActivity.this,congeList,utilisateurList);
                    //Pour faire mettre à jour le recyclerview si il ya des nouveaux éléments:
                    adapterListConge.notifyDataSetChanged();
                    //set Adapter to recycler view;
                    recyclerView.setAdapter(adapterListConge);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return congeList;
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

                    System.out.println("CongeAdmin1010");
                    //Ajouter la ligne dans firebase dans la liste des utilisateurs
                    utilisateurList.add(utilisateur);



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return utilisateurList;
    }




}