package com.pointage1.activite;


        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.RatingBar;
        import android.widget.Toast;

        import com.pointage1.R;
        import com.firebase.ui.database.FirebaseRecyclerOptions;
        import com.google.firebase.database.FirebaseDatabase;

public class activiteutil extends AppCompatActivity {
    RecyclerView recview2;

    adapterutil adapterutil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activteutil);

        recview2 = (RecyclerView) findViewById(R.id.recview2);

        recview2.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("activite"), model.class)
                        .build();


        adapterutil = new adapterutil(options);
        recview2.setAdapter(adapterutil);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterutil.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterutil.stopListening();
    }
}
