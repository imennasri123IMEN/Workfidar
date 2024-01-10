package com.pointage1.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pointage1.Model.Utilisateur;
import com.pointage1.R;
import com.pointage1.UtilisateurDetailActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class AdapterListUtilisateur extends RecyclerView.Adapter<AdapterListUtilisateur.ViewHolder> {

    //private String[] mData;
    List<Utilisateur> utilisateurList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;


    private Utilisateur utilisateurChoisi=null;
    private Context context;

    private String id_Utilisateur;
    private String email;
    private String nom;
    private String prenom;
    private String cin;
    private String matricule;
    private String motdepasse;
    private String telephone;
    private String image;
    private String cover;
    private String date_naissance;
    private String date_inscription;
    private String role;

    private String nometprenom="";

    //private final View.OnClickListener mOnClickListener = new MyOnClickListener();

    // data is passed into the constructor
    public AdapterListUtilisateur(Context context, List<Utilisateur> objects) {
        this.mInflater = LayoutInflater.from(context);
        //this.mData = data;
        utilisateurList = objects;
        this.context = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = mInflater.inflate(R.layout.activity_product_details_only_layout_2, parent, false);
        View view = mInflater.inflate(R.layout.list_utilisateurs_item, parent, false);
        //view.setOnClickListener(mOnClickListener);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //utilisateurList=orderListByDate(utilisateurList);

        Utilisateur myUtilisateur = utilisateurList.get(position);

        System.out.println("AdapterListUtilisateur:utilisateurList.get(position).getEmail():"+utilisateurList.get(position).getEmail());

        id_Utilisateur=utilisateurList.get(position).getId();
        email=utilisateurList.get(position).getEmail();
        nom=utilisateurList.get(position).getNom();
        prenom=utilisateurList.get(position).getPrenom();
        cin=utilisateurList.get(position).getCin();
        matricule=utilisateurList.get(position).getMatricule();
        motdepasse=utilisateurList.get(position).getMotdepasse();
        telephone=utilisateurList.get(position).getTelephone();
        image=utilisateurList.get(position).getImage();
        cover=utilisateurList.get(position).getCover();
        date_naissance=utilisateurList.get(position).getDate_naissance();
        date_inscription=utilisateurList.get(position).getDate_inscription();
        role=utilisateurList.get(position).getRole();

        System.out.println("AdapterListUtilisateur:utilisateurList.get(position).getEmail():"+utilisateurList.get(position).getEmail());

        System.out.println("AdapterListUtilisateur:email1:"+email);

        System.out.println("AdapterListUtilisateur:nom:"+nom);

        nometprenom=nom+" "+prenom;







        //Afficher le nom et prenom du l'utilisateur dans la ligne de la liste des utilisateurs
        try{
            if (nometprenom!=null){
                if (!nometprenom.equals("")){
                    holder.myTextViewnom_et_prenom.setText(nometprenom);
                }else{
                    holder.myTextViewnom_et_prenom.setText("");
                }
            }
        }catch (Exception e){
            holder.myTextViewnom_et_prenom.setText("");
        }


        //Afficher la date d'inscription du l'utilisateur dans la ligne de la liste des utilisateurs
        try{
            if (date_inscription!=null){
                if (!date_inscription.equals("")){
                    holder.myTextViewdateInscription.setText(date_inscription);
                }else{
                    holder.myTextViewdateInscription.setText("");
                }
            }
        }catch (Exception e){
            holder.myTextViewdateInscription.setText("");
        }


        //Afficher le button Ajouter, Modifier et Supprimer dans la ligne de la liste des utilisateurs
        try{
            if (role!=null){
                if (!role.equals("")){
                    if (role.equals("Invite")) {
                        holder.myButtonajouter.setVisibility(View.VISIBLE);
                        holder.myButtonmodifier.setVisibility(View.GONE);
                        holder.myButtonsupprimer.setVisibility(View.GONE);
                    } else if (role.equals("Employee")) {
                        holder.myButtonajouter.setVisibility(View.GONE);
                        holder.myButtonmodifier.setVisibility(View.VISIBLE);
                        holder.myButtonsupprimer.setVisibility(View.VISIBLE);
                    }
                }
            }
        }catch (Exception e){
            holder.myButtonajouter.setVisibility(View.VISIBLE);
            holder.myButtonmodifier.setVisibility(View.GONE);
            holder.myButtonsupprimer.setVisibility(View.GONE);
        }




        //String finalShow_leaves = myTextViewnom_et_prenom;
        holder.myLinearLayoutText.setTag(position);
        holder.myLinearLayoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //context.startActivity(new Intent(context, utilisateurDetailActivity.class));
                Intent intent = new Intent(context, UtilisateurDetailActivity.class);

                //intent.putExtra("KEY_NAME", myutilisateur);
                intent.putExtra("nom", utilisateurList.get((Integer) v.getTag()).getNom());
                intent.putExtra("id_Utilisateur", utilisateurList.get((Integer) v.getTag()).getId());
                intent.putExtra("email", utilisateurList.get((Integer) v.getTag()).getEmail());
                intent.putExtra("prenom", utilisateurList.get((Integer) v.getTag()).getPrenom());
                intent.putExtra("cin", utilisateurList.get((Integer) v.getTag()).getCin());
                intent.putExtra("matricule", utilisateurList.get((Integer) v.getTag()).getMatricule());
                intent.putExtra("motdepasse", utilisateurList.get((Integer) v.getTag()).getMotdepasse());
                intent.putExtra("telephone", utilisateurList.get((Integer) v.getTag()).getTelephone());
                intent.putExtra("image", utilisateurList.get((Integer) v.getTag()).getImage());
                intent.putExtra("cover", utilisateurList.get((Integer) v.getTag()).getCover());
                intent.putExtra("date_naissance", utilisateurList.get((Integer) v.getTag()).getDate_naissance());
                intent.putExtra("date_inscription", utilisateurList.get((Integer) v.getTag()).getDate_inscription());
                intent.putExtra("role", utilisateurList.get((Integer) v.getTag()).getRole());





                context.startActivity(intent);
            }
        });



        DatabaseReference reference_utilisateur = FirebaseDatabase.getInstance().getReference("users");

        holder.myButtonajouter.setTag(position);
        holder.myButtonajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Modifier la valeur de "role" de "Invite" en "Employee"


                //custom dialog
                //AlertDialog.Builder builder = new AlertDialog.Builder(ProfileUtilisateurActivity.this);
                //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                //AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Ajouter Employé");
                String message=utilisateurList.get((Integer) v.getTag()).getNom()+" "+utilisateurList.get((Integer) v.getTag()).getPrenom();
                builder.setMessage("Voulez vous ajouter l'utilisateur "+message+" à la liste des employés.");


                //set  layout of dialog
                //LinearLayout linearLayout = new LinearLayout(this);
                LinearLayout linearLayout = new LinearLayout(context);
                //LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setPadding(10,10,10,10);


                //add edit text
                //final EditText editText = new EditText(this);
                final EditText editText = new EditText(context);
                //final EditText editText = new EditText(getActivity());
                //editText.setHint("Enter "+key);
                editText.setHint("Entrer la matricule de l'employé");
                linearLayout.addView(editText);
                /*final TextView textView = new TextView(context);
                textView.setText("Voulez vous approuver le congé de "+show_name);
                linearLayout.addView(textView);*/




                builder.setView(linearLayout);

                //add button in dialog to update
                builder.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //input text from edit text
                        final String valueMatricule = editText.getText().toString().trim();
                        //Validate if user has entered something or not
                        if(!TextUtils.isEmpty(valueMatricule)){


                            //reference_utilisateur.addValueEventListener(new ValueEventListener(){
                            reference_utilisateur.addListenerForSingleValueEvent(new ValueEventListener(){
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                                    for(DataSnapshot ds:dataSnapshot.getChildren()){

                                        String id=utilisateurList.get((Integer) v.getTag()).getId();
                                        reference_utilisateur.child(id).child("role").setValue("Employee");
                                        reference_utilisateur.child(id).child("matricule").setValue(valueMatricule);
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError)    {
                                    //Toast.makeText(ProfileUtilisateurActivity.this,"Error"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(getActivity(),"Error"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                    Toast.makeText(context,"Erreur"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                            //finish();
                            ((Activity)context).finish();
                            //getActivity().finish();
                            //context.startActivity(getIntent());
                            context.startActivity(((Activity) context).getIntent());
                            //startActivity(getActivity().getIntent());


                        }
                        else {
                            //Toast.makeText(ProfileUtilisateurActivity.this,"Please enter "+key,Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getActivity(),"S'il vous plait entrer "+key,Toast.LENGTH_SHORT).show();
                            //Toast.makeText(ProfileActivity.this,"S'il vous plait entrer "+key,Toast.LENGTH_SHORT).show();
                            Toast.makeText(context,"S'il vous plait entrer la matricule",Toast.LENGTH_SHORT).show();
                        }

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


        holder.myButtonmodifier.setTag(position);
        holder.myButtonmodifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Modifier la valeur de "role" de "Invite" en "Employee"


                //custom dialog
                //AlertDialog.Builder builder = new AlertDialog.Builder(ProfileUtilisateurActivity.this);
                //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                //AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Modifier Employé");
                String message=utilisateurList.get((Integer) v.getTag()).getNom()+" "+utilisateurList.get((Integer) v.getTag()).getPrenom();
                builder.setMessage("Voulez vous modifier la matricule de l'utilisateur "+message+".");


                //set  layout of dialog
                //LinearLayout linearLayout = new LinearLayout(this);
                LinearLayout linearLayout = new LinearLayout(context);
                //LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setPadding(10,10,10,10);


                //add edit text
                //final EditText editText = new EditText(this);
                final EditText editText = new EditText(context);
                //final EditText editText = new EditText(getActivity());
                //editText.setHint("Enter "+key);
                editText.setHint("Entrer la matricule de l'employé");
                linearLayout.addView(editText);
                /*final TextView textView = new TextView(context);
                textView.setText("Voulez vous approuver le congé de "+show_name);
                linearLayout.addView(textView);*/




                builder.setView(linearLayout);

                //add button in dialog to update
                builder.setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //input text from edit text
                        final String valueMatricule = editText.getText().toString().trim();
                        //Validate if user has entered something or not
                        if(!TextUtils.isEmpty(valueMatricule)){


                            //reference_utilisateur.addValueEventListener(new ValueEventListener(){
                            reference_utilisateur.addListenerForSingleValueEvent(new ValueEventListener(){
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                                    for(DataSnapshot ds:dataSnapshot.getChildren()){

                                        String id=utilisateurList.get((Integer) v.getTag()).getId();
                                        //reference_utilisateur.child(id).child("role").setValue("Employee");
                                        reference_utilisateur.child(id).child("matricule").setValue(valueMatricule);

                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError)    {
                                    //Toast.makeText(ProfileUtilisateurActivity.this,"Error"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(getActivity(),"Error"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                    Toast.makeText(context,"Erreur"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                            //finish();
                            ((Activity)context).finish();
                            //getActivity().finish();
                            //context.startActivity(getIntent());
                            context.startActivity(((Activity) context).getIntent());
                            //startActivity(getActivity().getIntent());


                        }
                        else {
                            //Toast.makeText(ProfileUtilisateurActivity.this,"Please enter "+key,Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getActivity(),"S'il vous plait entrer "+key,Toast.LENGTH_SHORT).show();
                            //Toast.makeText(ProfileActivity.this,"S'il vous plait entrer "+key,Toast.LENGTH_SHORT).show();
                            Toast.makeText(context,"S'il vous plait entrer la matricule",Toast.LENGTH_SHORT).show();
                        }

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


        holder.myButtonsupprimer.setTag(position);
        holder.myButtonsupprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Modifier la valeur de "role" de "Employee" en "Invite"


                //custom dialog
                //AlertDialog.Builder builder = new AlertDialog.Builder(ProfileUtilisateurActivity.this);
                //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                //AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Supprimer Employé");
                String message=utilisateurList.get((Integer) v.getTag()).getNom()+" "+utilisateurList.get((Integer) v.getTag()).getPrenom();
                builder.setMessage("Voulez vous supprimer l'utilisateur "+message+" de la liste des employés.");


                //set  layout of dialog
                //LinearLayout linearLayout = new LinearLayout(this);
                LinearLayout linearLayout = new LinearLayout(context);
                //LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setPadding(10,10,10,10);


                /*//add edit text
                //final EditText editText = new EditText(this);
                final EditText editText = new EditText(context);
                //final EditText editText = new EditText(getActivity());
                //editText.setHint("Enter "+key);
                editText.setHint("Entrer la matricule de l'employé");
                linearLayout.addView(editText);
                final TextView textView = new TextView(context);
                textView.setText("Voulez vous approuver le congé de "+show_name);
                linearLayout.addView(textView);*/




                builder.setView(linearLayout);

                //add button in dialog to update
                builder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {




                            //reference_utilisateur.addValueEventListener(new ValueEventListener(){
                            reference_utilisateur.addListenerForSingleValueEvent(new ValueEventListener(){
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                                    for(DataSnapshot ds:dataSnapshot.getChildren()){

                                        String id=utilisateurList.get((Integer) v.getTag()).getId();
                                        reference_utilisateur.child(id).child("role").setValue("Invite");
                                        reference_utilisateur.child(id).child("matricule").setValue("");

                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError)    {
                                    //Toast.makeText(ProfileUtilisateurActivity.this,"Error"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(getActivity(),"Error"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                    Toast.makeText(context,"Erreur"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                            //finish();
                            ((Activity)context).finish();
                            //getActivity().finish();
                            //context.startActivity(getIntent());
                            context.startActivity(((Activity) context).getIntent());
                            //startActivity(getActivity().getIntent());




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



    }


    /*public List<Utilisateur> orderListByDate(List<Utilisateur> myList){


        for(int i=0;i<myList.size();i++){

            for(int j=i+1;j<myList.size();j++){

                Utilisateur tempUserI=myList.get(i);
                Utilisateur tempUserJ=myList.get(j);

                String dateUserI=tempUserI.getDate_inscription();
                String dateUserJ=tempUserJ.getDate_inscription();

                *//*if(tempI>tempJ){
                    list[i]=tempJ;
                    list[j]= tempI;

                }*//*

                if(dateJ_Is_Before_DateI(dateUserI,dateUserJ)){
                    myList.add(i, tempUserJ);
                    myList.add(j, tempUserI);
                }

            }
        }


        return myList;
    }


    public boolean dateJ_Is_Before_DateI(String dateUserI,String dateUserJ){
        Date dateI=null;
        Date dateJ=null;
        try {
            dateI=new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse(dateUserI);
            dateJ=new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse(dateUserJ);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateJ.before(dateI);
    }*/





    //List inverse
    //@Override
    public Utilisateur getItem(int position) {
        //return utilisateurList.get(getCount()-position-1);
        return utilisateurList.get(position);
    }
    //@Override
    public int getCount() {
        return utilisateurList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    // total number of cells
    @Override
    public int getItemCount() {
        //return mData.length;
        return utilisateurList.size();
    }



    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextViewnom_et_prenom;
        TextView myTextViewdateInscription;
        Button myButtonajouter;
        Button myButtonmodifier;
        Button myButtonsupprimer;
        LinearLayout myLinearLayoutText;

        ViewHolder(View itemView) {
            super(itemView);
            myTextViewnom_et_prenom = itemView.findViewById(R.id.nom_et_prenom);

            myButtonajouter = itemView.findViewById(R.id.ajouter);
            myButtonmodifier = itemView.findViewById(R.id.modifier);
            myButtonsupprimer = itemView.findViewById(R.id.supprimer);
            myLinearLayoutText = itemView.findViewById(R.id.show_text_LinearLayout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    /*String getItem(int id) {
        //return mData[id];
        return matchList.get(id);
    }*/

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
