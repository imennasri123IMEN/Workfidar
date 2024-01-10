package com.pointage1.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.pointage1.Model.Conge;
import com.pointage1.Model.Utilisateur;
import com.pointage1.R;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class AdapterListCongeEmploye extends RecyclerView.Adapter<AdapterListCongeEmploye.ViewHolder> {

    //private String[] mData;
    List<Conge> congeList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    private String nom="";
    private String prenom="";
    private String show_leaves="";
    private Utilisateur utilisateurChoisi=null;
    List<Utilisateur> usersList;
    private Context context;
    private String show_name;

    //private final View.OnClickListener mOnClickListener = new MyOnClickListener();

    // data is passed into the constructor
    public AdapterListCongeEmploye(Context context, List<Conge> objects) {
        this.mInflater = LayoutInflater.from(context);
        //this.mData = data;
        congeList = objects;
        this.context = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = mInflater.inflate(R.layout.activity_product_details_only_layout_2, parent, false);
        View view = mInflater.inflate(R.layout.list_conge_employe_item, parent, false);
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
        //show_leaves="";


        //show_leaves=getLeaves(dateDeDébut,dateDeFin);


        //usersList=getListUsers(holder.myTextViewshow_name , congeList.get(position).getId_Utilisateur());




        //Afficher le nombre de jours du congé dans la ligne de la liste des congé
        /*try{
            if (show_leaves!=null){
                if (!show_leaves.equals("")){
                    holder.myTextViewshow_leaves.setText(show_leaves);
                }else{
                    holder.myTextViewshow_leaves.setText("");
                }
            }
        }catch (Exception e){
            holder.myTextViewshow_leaves.setText("");
        }*/

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


        try{
            if (dateDeDébut!=null){
                if (!dateDeDébut.equals("")){
                    holder.myTextViewdateDeDébut.setText(dateDeDébut);
                }else{
                    holder.myTextViewdateDeDébut.setText("");
                }
            }
        }catch (Exception e){
            holder.myTextViewdateDeDébut.setText("");
        }


        try{
            if (dateDeFin!=null){
                if (!dateDeFin.equals("")){
                    holder.myTextViewdateDeFin.setText(dateDeFin);
                }else{
                    holder.myTextViewdateDeFin.setText("");
                }
            }
        }catch (Exception e){
            holder.myTextViewdateDeFin.setText("");
        }


        try{
            if (statusConge!=null){
                if (!statusConge.equals("")){


                    if (statusConge.equals("En demande")){
                        holder.myTextViewstatusConge.setText("En demande");

                    }else if(statusConge.equals("Approuver")){
                        holder.myTextViewstatusConge.setText("Approuvé");

                    }else if(statusConge.equals("Rejecter")){
                        holder.myTextViewstatusConge.setText("Rejecté");
                    }


                }else{
                    holder.myTextViewstatusConge.setText("");
                }
            }
        }catch (Exception e){
            holder.myTextViewstatusConge.setText("");
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

        //System.out.println("AdapterListConge000");



        //String finalShow_leaves = show_leaves;
        /*holder.myLinearLayoutText.setTag(position);
        holder.myLinearLayoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //context.startActivity(new Intent(context, CongeDetailActivity.class));
                Intent intent = new Intent(context, CongeDetailActivity.class);
                //intent.putExtra("KEY_NAME", myConge);
                //intent.putExtra("nom", usersList.get((Integer) v.getTag()).getNom());
                //intent.putExtra("prenom", usersList.get((Integer) v.getTag()).getPrenom());
                intent.putExtra("id_Utilisateur", congeList.get((Integer) v.getTag()).getId_Utilisateur());
                intent.putExtra("id_Conge", congeList.get((Integer) v.getTag()).getId_Conge());
                intent.putExtra("show_reason", congeList.get((Integer) v.getTag()).getCause());
                intent.putExtra("dateDeDébut", congeList.get((Integer) v.getTag()).getDateDeDébut());
                intent.putExtra("dateDeFin", congeList.get((Integer) v.getTag()).getDateDeFin());
                intent.putExtra("statusConge", congeList.get((Integer) v.getTag()).getStatusConge());
                //String finalShow_leaves =getLeaves(congeList.get((Integer) v.getTag()).getDateDeDébut()   ,   congeList.get((Integer) v.getTag()).getDateDeFin());
                //intent.putExtra("show_leaves", finalShow_leaves);


                context.startActivity(intent);
            }
        });*/






        //DatabaseReference reference_conge = FirebaseDatabase.getInstance().getReference("conge");



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
        TextView myTextViewshow_reason;
        TextView myTextViewdateDeDébut;
        TextView myTextViewdateDeFin;
        TextView myTextViewstatusConge;
        LinearLayout myLinearLayoutText;
        //Button myButtondetail;

        ViewHolder(View itemView) {
            super(itemView);
            myTextViewshow_reason = itemView.findViewById(R.id.show_reason);
            myTextViewdateDeDébut = itemView.findViewById(R.id.dateDeDébut);
            myTextViewdateDeFin = itemView.findViewById(R.id.dateDeFin);
            myTextViewstatusConge = itemView.findViewById(R.id.statusConge);
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
