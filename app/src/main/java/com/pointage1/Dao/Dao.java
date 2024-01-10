package com.pointage1.Dao;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pointage1.Model.Conge;

public class Dao {

    private DatabaseReference databaseReference;
    public Dao(){

        FirebaseDatabase db= FirebaseDatabase.getInstance();
        databaseReference =db.getReference(Conge.class.getSimpleName());

    }
    public Task<Void> add(Conge conge){

        return   databaseReference.push().setValue(conge);
    }

}
