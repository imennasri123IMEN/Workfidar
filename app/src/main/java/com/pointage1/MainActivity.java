package com.pointage1;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pointage1.ImageAdapter;
import com.pointage1.Model.Utilisateur;
import com.pointage1.R;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    CircularProgressIndicator progress_circular;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    private Context mContext;
    private Activity mActivity;
    private ArrayList<Utilisateur> imagesList;
    private ImageAdapter imageAdapter = null;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActivity = MainActivity.this;
        mContext = getApplicationContext();
        FirebaseApp.initializeApp(this);

        recyclerView = findViewById(R.id.recycler_view);
        progress_circular = findViewById(R.id.progress_circular);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        imagesList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                imagesList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Utilisateur imagemodel = dataSnapshot.getValue(Utilisateur.class);
                    imagesList.add(imagemodel);
                }
                imageAdapter = new ImageAdapter(mContext,mActivity, (ArrayList<Utilisateur>) imagesList);
                recyclerView.setAdapter(imageAdapter);
                imageAdapter.notifyDataSetChanged();
                progress_circular.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MainActivity.this,"Error:" + error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

}