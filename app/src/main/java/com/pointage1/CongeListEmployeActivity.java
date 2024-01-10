package com.pointage1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pointage1.Adapters.AdapterListCongeEmploye;
import com.pointage1.Model.Conge;

import java.util.ArrayList;
import java.util.List;

public class CongeListEmployeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterListCongeEmploye adapterListCongeEmploye;
    List<Conge> congeList;
    DatabaseReference reference;

    private boolean connectedUser;
    private FirebaseAuth firebaseAuth;
    private String myUID;

    private Button btnajouterconge;
    //private LinearLayout conge_employe_recyclerView_LinearLayout;
    //private LinearLayout conge_employe_textView_LinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conge_list_employe);

        recyclerView = findViewById(R.id.conge_employe_recyclerView);
        btnajouterconge = findViewById(R.id.btnajouterconge);
        //conge_employe_recyclerView_LinearLayout = findViewById(R.id.conge_employe_recyclerView_LinearLayout);
        //conge_employe_textView_LinearLayout = findViewById(R.id.conge_employe_textView_LinearLayout);

        myUID="";
        firebaseAuth= FirebaseAuth.getInstance();
        checkUserStatus();


        //Layout (LinearLayout) for RecyclerView
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        // recycler view properties:
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        System.out.println("CongeEmploye900");
        congeList=getCongeList();

        /*if(congeList.size()==0){
            conge_employe_recyclerView_LinearLayout.setVisibility(View.GONE);
            conge_employe_textView_LinearLayout.setVisibility(View.VISIBLE);
        }else {
            conge_employe_recyclerView_LinearLayout.setVisibility(View.VISIBLE);
            conge_employe_textView_LinearLayout.setVisibility(View.GONE);
        }*/

        System.out.println("CongeEmploye1200");

        /*//Adapter
        adapterListConge =new AdapterListConge(getApplicationContext(),congeList);
        //setAdapter
        recyclerView.setAdapter(adapterListConge);*/



        btnajouterconge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CongeListEmployeActivity.this,CongeActivity.class));
            }
        });



    }


    private void checkUserStatus(){
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            //user is signed in stay here
            connectedUser=true;
            myUID=user.getUid();
        }else{
            //user not signed in, go to MainActivity
            //startActivity(new Intent(this, MainActivity.class));
            //finish();

            connectedUser=false;
        }
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

                    System.out.println("CongeEmploye1000");
                    //Avoir la liste des conges en demande
                    if(connectedUser){

                        if(conge.getId_Utilisateur().equals(myUID)) {
                            System.out.println("CongeEmploye1001");
                            congeList.add(conge);
                        }
                    }

                    //adapter
                    //adapterListConge=new AdapterListConge(getApplicationContext(),congeList);
                    adapterListCongeEmploye=new AdapterListCongeEmploye(CongeListEmployeActivity.this,congeList);
                    //Pour faire mettre à jour le recyclerview si il ya des nouveaux éléments:
                    adapterListCongeEmploye.notifyDataSetChanged();
                    //set Adapter to recycler view;
                    recyclerView.setAdapter(adapterListCongeEmploye);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return congeList;
    }



}