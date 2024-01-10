package com.pointage1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pointage1.Model.Employee;
import com.pointage1.Model.Utilisateur;
import com.pointage1.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    RadioButton b1,b2;
    private Context mContext;
    private Activity mActivity;
    private ArrayList<Utilisateur> mContentList;
    FirebaseDatabase database;
    DatabaseReference reference;
    int i=0;


    public ImageAdapter(Context mContext, Activity mActivity, ArrayList<Utilisateur> mContentList) {

        this.mContext = mContext;
        this.mActivity = mActivity;
        this.mContentList = mContentList;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view, viewType);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView imgPost;
        private TextView tvTitle;




        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            // Find all views ids
            cardView = (CardView) itemView.findViewById(R.id.card_view_top);
            imgPost = (ImageView) itemView.findViewById(R.id.post_img);
            tvTitle = (TextView) itemView.findViewById(R.id.title_text);


        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder mainHolder, int position) {
        ViewHolder holder = (ViewHolder) mainHolder;


        final Utilisateur model = mContentList.get(position);
        // setting data over views
        String imgUrl = model.getImage();
        if (imgUrl != null && !imgUrl.isEmpty()) {
            Glide.with(mContext)
                    .load(imgUrl)
                    .into(holder.imgPost);
        }

        holder.tvTitle.setText(model.getNom());

    }

    @Override
    public int getItemCount() {
        return mContentList.size();
    }
}