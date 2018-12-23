package com.example.sanay.complaint;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sanay.complaint.Models.ModelComplaintDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusViewHolder> {

    public List<ModelComplaintDetail> complaintList;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public StatusAdapter(List<ModelComplaintDetail> complaintList) {
        this.complaintList = complaintList;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.status_single_element,parent,false);
        context = parent.getContext();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {

        String category = complaintList.get(position).getCategory();
        String subCategory = complaintList.get(position).getSubcategory();
        String url = complaintList.get(position).getImagedownloadurl();

        Glide.with(context).load(url).into(holder.ivImage);
        holder.tvCategory.setText(category);
        holder.tvSubcategory.setText(subCategory);

    }

    @Override
    public int getItemCount() {
        return complaintList.size();
    }
}


class StatusViewHolder extends RecyclerView.ViewHolder{
    public TextView tvCategory,tvSubcategory;
    public View mView;
    public ImageView ivImage;

    public StatusViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        tvCategory = mView.findViewById(R.id.status_category);
        tvSubcategory = mView.findViewById(R.id.status_sub_category);
        ivImage = mView.findViewById(R.id.status_item_image);
    }



}
