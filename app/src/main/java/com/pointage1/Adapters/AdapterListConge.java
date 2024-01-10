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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pointage1.CongeDetailActivity;
import com.pointage1.Model.Conge;
import com.pointage1.Model.Utilisateur;
import com.pointage1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;


public class AdapterListConge extends RecyclerView.Adapter<AdapterListConge.ViewHolder> {

    //private String[] mData;
    List<Conge> congeList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    private String nom="";
    private String prenom="";
    private String show_leaves="";
    private Utilisateur utilisateurChoisi=null;
    //List<Utilisateur > usersList;
    List<Utilisateur > utilisateurList;
    private Context context;
    private String show_name;

    //private final View.OnClickListener mOnClickListener = new MyOnClickListener();

    // data is passed into the constructor
    public AdapterListConge(Context context, List<Conge> objects,List<Utilisateur> utilisateurList) {
        this.mInflater = LayoutInflater.from(context);
        //this.mData = data;
        congeList = objects;
        this.utilisateurList=utilisateurList;
        this.context = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = mInflater.inflate(R.layout.activity_product_details_only_layout_2, parent, false);
        View view = mInflater.inflate(R.layout.demande_conge_item, parent, false);
        //view.setOnClickListener(mOnClickListener);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        //congeList=orderListByDate(congeList);

        System.out.println("AdapterListConge:position"+position);
        //String show_name=getNomEtPrenom(congeList.get(position).getId_Utilisateur());
        Conge myConge = congeList.get(position);
        String id_Utilisateur=congeList.get(position).getId_Utilisateur();
        String id_Conge=congeList.get(position).getId_Conge();
        String show_reason=congeList.get(position).getCause();
        String dateDeDébut=congeList.get(position).getDateDeDébut();
        String dateDeFin=congeList.get(position).getDateDeFin();
        String statusConge=congeList.get(position).getStatusConge();

        try{
            if (statusConge!=null){
                if (statusConge.equals("En demande")){
                    holder.myTextViewstatusConge.setVisibility(View.GONE);
                    holder.myButtonaccept.setVisibility(View.VISIBLE);
                    holder.myButtonreject.setVisibility(View.VISIBLE);
                }else if(statusConge.equals("Approuver")){
                    holder.myTextViewstatusConge.setText("Approuvé");

                    holder.myTextViewstatusConge.setVisibility(View.VISIBLE);
                    holder.myButtonaccept.setVisibility(View.GONE);
                    holder.myButtonreject.setVisibility(View.GONE);

                }else if(statusConge.equals("Rejecter")){
                    holder.myTextViewstatusConge.setText("Rejeté");

                    holder.myTextViewstatusConge.setVisibility(View.VISIBLE);
                    holder.myButtonaccept.setVisibility(View.GONE);
                    holder.myButtonreject.setVisibility(View.GONE);
                }
            }
        }catch (Exception e){
            holder.myTextViewstatusConge.setText("");
        }





        show_leaves="";


        show_leaves=getLeaves(dateDeDébut,dateDeFin);


        //usersList=getListUsers(holder.myTextViewshow_name , congeList.get(position).getId_Utilisateur());



        String nomUser=getNomUser(utilisateurList,id_Utilisateur);
        String prenomUser=getPrenomUser(utilisateurList,id_Utilisateur);


        String show_name_user = nomUser + " " + prenomUser;
        if (!show_name_user.equals(" ")) {
            try {

                //Modifier le textview Nom et Prénom dans la liste des conges:
                holder.myTextViewshow_name.setText(show_name_user);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        System.out.println("AdapterListConge0:nom:"+utilisateurList.get(position).getNom());
        System.out.println("AdapterListConge0:prenom:"+utilisateurList.get(position).getPrenom());
        System.out.println("AdapterListConge0:show_reason:"+congeList.get(position).getCause());





        //Afficher le nombre de jours du congé dans la ligne de la liste des congé
        try{
            if (show_leaves!=null){
                if (!show_leaves.equals("")){
                    holder.myTextViewshow_leaves.setText(show_leaves);
                }else{
                    holder.myTextViewshow_leaves.setText("");
                }
            }
        }catch (Exception e){
            holder.myTextViewshow_leaves.setText("");
        }

        try{
            if (show_reason!=null){
                if (!show_reason.equals("")){
                    holder.myTextViewshow_reason.setText(show_reason);
                }else{
                    holder.myTextViewshow_reason.setText("");
                }
            }
        }catch (Exception e){
            holder.myTextViewshow_reason.setText("");
        }


        /*try{
            if (show_name!=null){
                if (!show_name.equals("")){
                    holder.myTextViewshow_name.setText(show_name);
                }else{
                    holder.myTextViewshow_name.setText("");
                }
            }
        }catch (Exception e){
            holder.myTextViewshow_name.setText("");
        }*/



        //Pour avoir le nom et le prénom, on va chercher dans la table "users"
        // dans Firebase l'id de l'utilisateur

        System.out.println("AdapterListConge000");



        //String finalShow_leaves = show_leaves;
        holder.myLinearLayoutText.setTag(position);
        holder.myLinearLayoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //context.startActivity(new Intent(context, CongeDetailActivity.class));
                Intent intent = new Intent(context, CongeDetailActivity.class);
                //intent.putExtra("KEY_NAME", myConge);

                String finalId_Utilisateur=congeList.get((Integer) v.getTag()).getId_Utilisateur();
                String finalNom=getNomUser(utilisateurList,finalId_Utilisateur);
                String finalPrenom=getPrenomUser(utilisateurList,finalId_Utilisateur);

                intent.putExtra("nom",  finalNom);
                intent.putExtra("prenom", finalPrenom);
                intent.putExtra("id_Utilisateur", finalId_Utilisateur);
                intent.putExtra("id_Conge", congeList.get((Integer) v.getTag()).getId_Conge());
                intent.putExtra("show_reason", congeList.get((Integer) v.getTag()).getCause());
                intent.putExtra("dateDeDébut", congeList.get((Integer) v.getTag()).getDateDeDébut());
                intent.putExtra("dateDeFin", congeList.get((Integer) v.getTag()).getDateDeFin());
                intent.putExtra("statusConge", congeList.get((Integer) v.getTag()).getStatusConge());
                String finalShow_leaves =getLeaves(congeList.get((Integer) v.getTag()).getDateDeDébut()   ,   congeList.get((Integer) v.getTag()).getDateDeFin());
                intent.putExtra("show_leaves", finalShow_leaves);

                System.out.println("AdapterListConge:nom:"+finalNom);
                System.out.println("AdapterListConge:prenom:"+finalPrenom);
                System.out.println("AdapterListConge:show_reason:"+congeList.get((Integer) v.getTag()).getCause());


                /*intent.putExtra("nom", nom);
                intent.putExtra("prenom", prenom);
                intent.putExtra("id_Utilisateur", id_Utilisateur);
                intent.putExtra("id_Conge", id_Conge);
                intent.putExtra("show_reason", show_reason);
                intent.putExtra("dateDeDébut", dateDeDébut);
                intent.putExtra("dateDeFin", dateDeFin);
                intent.putExtra("show_leaves", finalShow_leaves);*/

                context.startActivity(intent);
            }
        });






        DatabaseReference reference_conge = FirebaseDatabase.getInstance().getReference("conge");

        holder.myButtonaccept.setTag(position);
        holder.myButtonaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Modifier la valeur de "statusConge" de "Conge" en "Approuver"


                //custom dialog
                //AlertDialog.Builder builder = new AlertDialog.Builder(ProfileUtilisateurActivity.this);
                //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                //AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Approuver Congé");

                //String message=utilisateurList.get((Integer) v.getTag()).getNom()  +" "+  utilisateurList.get((Integer) v.getTag()).getPrenom();
                //String message=getPrenomUser(utilisateurList,congeList.get((Integer) v.getTag()).getId_Utilisateur())  +" "+  getNomUser(utilisateurList,congeList.get((Integer) v.getTag()).getId_Utilisateur());
                String message=getNomUser(utilisateurList,congeList.get((Integer) v.getTag()).getId_Utilisateur())  +" "+  getPrenomUser(utilisateurList,congeList.get((Integer) v.getTag()).getId_Utilisateur());
                builder.setMessage("Voulez vous approuver le congé de "+message);


                //set  layout of dialog
                //LinearLayout linearLayout = new LinearLayout(this);
                LinearLayout linearLayout = new LinearLayout(context);
                //LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setPadding(10,10,10,10);

                /*
                //add edit text
                //final EditText editText = new EditText(this);
                final EditText editText = new EditText(context);
                //final EditText editText = new EditText(getActivity());
                //editText.setHint("Enter "+key);
                editText.setHint("Entrer "+key);
                linearLayout.addView(editText);
                final TextView textView = new TextView(context);
                textView.setText("Voulez vous approuver le congé de "+show_name);
                linearLayout.addView(textView);
                */



                builder.setView(linearLayout);

                //add button in dialog to update
                //builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
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


        holder.myButtonreject.setTag(position);
        holder.myButtonreject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Modifier la valeur de "statusConge" de "Conge" en "Rejecter"


                //custom dialog
                //AlertDialog.Builder builder = new AlertDialog.Builder(ProfileUtilisateurActivity.this);
                //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                //AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Rejecter Congé");

                //String message=usersList.get((Integer) v.getTag()).getNom()  +" "+  usersList.get((Integer) v.getTag()).getPrenom();
                //String message=getPrenomUser(utilisateurList,congeList.get((Integer) v.getTag()).getId_Utilisateur())  +" "+  getNomUser(utilisateurList,congeList.get((Integer) v.getTag()).getId_Utilisateur());
                String message=getNomUser(utilisateurList,congeList.get((Integer) v.getTag()).getId_Utilisateur())  +" "+  getPrenomUser(utilisateurList,congeList.get((Integer) v.getTag()).getId_Utilisateur());
                builder.setMessage("Voulez vous rejeter le congé de "+message);

                //set  layout of dialog
                //LinearLayout linearLayout = new LinearLayout(this);
                LinearLayout linearLayout = new LinearLayout(context);
                //LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setPadding(10,10,10,10);

                /*
                //add edit text
                //final EditText editText = new EditText(this);
                final EditText editText = new EditText(context);
                //final EditText editText = new EditText(getActivity());
                //editText.setHint("Enter "+key);//hint e.g Edit name Or Edit phone
                editText.setHint("Entrer "+key);//hint e.g Edit name Or Edit phone
                linearLayout.addView(editText);
                final TextView textView = new TextView(context);
                textView.setText("Voulez vous rejecter le congé de "+show_name);
                linearLayout.addView(textView);
                */



                builder.setView(linearLayout);

                //add button in dialog to update
                //builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                builder.setPositiveButton("Rejeter", new DialogInterface.OnClickListener() {
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
                                //Toast.makeText(ProfileUtilisateurActivity.this,"Error"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getActivity(),"Error"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                Toast.makeText(context,"Error"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
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



    public String getLeaves(String dateDeDébut,String dateDeFin){
        //Calculer le nombre de jours entre la date du début du congé et la date de fin du congé:
        DateTimeFormatter dtf = null;
        LocalDate date1 = null;
        LocalDate date2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            /*dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            date1 = LocalDate.parse(dateDeDébut, dtf);
            date2 = LocalDate.parse(dateDeFin, dtf);
            long daysBetween = Duration.between(date1.atStartOfDay(), date2.atStartOfDay()).toDays();
            System.out.println ("Days: " + daysBetween);
            show_leaves=Long.toString(daysBetween);*/

            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            date1 = LocalDate.parse(dateDeDébut, dtf);
            date2 = LocalDate.parse(dateDeFin, dtf);

            Period p = Period.between(date1, date2);
            long p2 = ChronoUnit.DAYS.between(date1, date2);
            //p2=p2+1;
            show_leaves=Long.toString(p2);

        }
        return show_leaves;
    }




    public String getNomUser(List<Utilisateur> usersList,String id_user){
        String nomUser="";
        for (int i =0;i<usersList.size();i++){
            if(usersList.get(i).getId().equals(id_user)) {
                nomUser = usersList.get(i).getNom();


            }
        }
        return nomUser;
    }


    public String getPrenomUser(List<Utilisateur> usersList,String id_user){
        String prenomUser="";
        for (int i =0;i<usersList.size();i++){
            if(usersList.get(i).getId().equals(id_user)) {
                prenomUser = usersList.get(i).getPrenom();
            }
        }
        return prenomUser;
    }



    /*public List<Utilisateur> getListUsers(TextView myTextViewNomEtPrenom, String id_User){
        usersList=new ArrayList<>();


        DatabaseReference reference_users = FirebaseDatabase.getInstance().getReference("users");

        //get all data ref =reference
        reference_users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Utilisateur modelUser=ds.getValue(Utilisateur.class);

                    usersList.add(modelUser);


                    System.out.println("AdapterListConge001");


                    if(modelUser.getId().equals(id_User)) {
                        String nomUser = modelUser.getNom();
                        String prenomUser = modelUser.getPrenom();
                        String show_name_user = nomUser + " " + prenomUser;
                        if (!show_name_user.equals(" ")) {
                            try {

                                //Modifier le textview Nom et Prénom dans la liste des conges:
                                myTextViewNomEtPrenom.setText(show_name_user);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }



                    //Si l'id trouvé est égal à id_Utilisateur dans l'élément de la liste des congé
                    if(modelUser.getId().equals(id_Utilisateur)){
                        Utilisateur information = new Utilisateur();
                        information.setNom(modelUser.getNom());
                        information.setPrenom(modelUser.getPrenom());
                        information.setEmail(modelUser.getEmail());
                        information.setTelephone(modelUser.getTelephone());
                        information.setCin(modelUser.getCin());
                        information.setImage(modelUser.getImage());
                        information.setCover(modelUser.getCover());
                        information.setDate_inscription(modelUser.getDate_inscription());
                        information.setDate_naissance(modelUser.getDate_naissance());
                        information.setId(modelUser.getId());

                        System.out.println("AdapterListConge002");

                        utilisateurChoisi=information;


                        if(!information.getNom().equals("")){
                            try{
                                nom=information.getNom();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if(!information.getPrenom().equals("")){
                            try{
                                prenom=information.getPrenom();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }




                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return usersList;
    }*/




    /*public List<Conge> orderListByDate(List<Conge> myList){


        for(int i=0;i<myList.size();i++){

            for(int j=i+1;j<myList.size();j++){

                Conge tempUserI=myList.get(i);
                Conge tempUserJ=myList.get(j);

                System.out.println("tempUserI.getCause():"+tempUserI.getCause());
                System.out.println("tempUserJ.getCause():"+tempUserJ.getCause());

                String dateUserI=tempUserI.getDateDeDemande();
                String dateUserJ=tempUserJ.getDateDeDemande();

                *//*if(tempI>tempJ){
                    list[i]=tempJ;
                    list[j]= tempI;

                }*//*

                if(dateJ_Is_Before_DateI(dateUserI,dateUserJ)){
                    System.out.println("myList.get(i).getCause()1:"+myList.get(i).getCause());
                    System.out.println("myList.get(j).getCause()1:"+myList.get(j).getCause());

                    myList.add(j, tempUserI);
                    myList.add(i, tempUserJ);

                    System.out.println("myList.get(i).getCause()1:"+myList.get(i).getCause());
                    System.out.println("myList.get(j).getCause()1:"+myList.get(j).getCause());
                }

            }
        }


        return myList;
    }

    public boolean dateJ_Is_Before_DateI(String dateUserI,String dateUserJ){
        Date dateI=null;
        Date dateJ=null;
        Date date1=null;
        Date date2=null;
        Boolean compare=null;
        try {
            dateI=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dateUserI);
            dateJ=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dateUserJ);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            date1 = sdf.parse(dateUserI);
            date2 = sdf.parse(dateUserJ);


            System.out.println("dateI.toString()1:"+dateI.toString());
            System.out.println("dateJ.toString()1:"+dateJ.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("dateI.toString()2:"+dateI.toString());
        System.out.println("dateJ.toString()2:"+dateJ.toString());

        compare=dateJ.after(dateI)|| dateJ.equals(dateI);
        //compare=date1.before(date2)|| date1.equals(date2);
        System.out.println("compare:"+compare);
        return compare;
    }*/



    /*public String getNomEtPrenom(String id_Utitlisateur) {

        //Pour avoir le nom et le prénom, on va chercher dans la table "users"
        // dans Firebase l'id de l'utilisateur

        System.out.println("AdapterListConge000");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        //get all data ref =reference
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Utilisateur modelUser=ds.getValue(Utilisateur.class);

                    System.out.println("AdapterListConge001");

                    //Si l'id trouvé est égal à id_Utilisateur dans l'élément de la liste des congé
                    if(modelUser.getId().equals(id_Utitlisateur)){
                        Utilisateur information = new Utilisateur();
                        information.setNom(modelUser.getNom());
                        information.setPrenom(modelUser.getPrenom());
                        information.setEmail(modelUser.getEmail());
                        information.setTelephone(modelUser.getTelephone());
                        information.setCin(modelUser.getCin());
                        information.setImage(modelUser.getImage());
                        information.setCover(modelUser.getCover());
                        information.setDate_inscription(modelUser.getDate_inscription());
                        information.setDate_naissance(modelUser.getDate_naissance());
                        information.setId(modelUser.getId());

                        System.out.println("AdapterListConge002");

                        utilisateurChoisi=information;

                        if(!information.getNom().equals("")){
                            try{
                                nom=information.getNom();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if(!information.getPrenom().equals("")){
                            try{
                                prenom=information.getPrenom();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        holder.myTextViewshow_name.setText(nom+" "+prenom);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return nom+" "+prenom;
    }
*/

    //List inverse
    //@Override
    public Conge getItem(int position) {
        //return congeList.get(getCount()-position-1);
        return congeList.get(position);
    }
    //@Override
    public int getCount() {
        return congeList.size();
    }




    // total number of cells
    @Override
    public int getItemCount() {
        //return mData.length;
        return congeList.size();
    }



    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextViewshow_name;
        TextView myTextViewshow_reason;
        TextView myTextViewshow_leaves;
        TextView myTextViewstatusConge;
        Button myButtonaccept;
        Button myButtonreject;
        LinearLayout myLinearLayoutText;
        //Button myButtondetail;

        ViewHolder(View itemView) {
            super(itemView);
            myTextViewshow_name = itemView.findViewById(R.id.show_name);
            myTextViewshow_reason = itemView.findViewById(R.id.show_reason);
            myTextViewshow_leaves = itemView.findViewById(R.id.show_leaves);
            myTextViewstatusConge = itemView.findViewById(R.id.statusConge);
            myButtonaccept = itemView.findViewById(R.id.accept);
            myButtonreject = itemView.findViewById(R.id.reject);
            myLinearLayoutText = itemView.findViewById(R.id.show_text_LinearLayout);
            //myButtondetail = itemView.findViewById(R.id.detail);
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
