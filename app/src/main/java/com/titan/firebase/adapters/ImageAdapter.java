package com.titan.firebase.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.titan.firebase.R;
import com.titan.firebase.models.Upload;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Upload> mUploads;


    public ImageAdapter(Context mContext, List<Upload> mUploads) {
        this.mContext = mContext;
        this.mUploads = mUploads;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Upload uploadCurrent = mUploads.get(position);
        ((ImageHolder) holder).textViewName.setText(uploadCurrent.getName());
        Picasso.get() /*.with(mContext)*/
                .load(uploadCurrent.getImageUrl())
                .fit()
                .centerCrop()
                .into(((ImageHolder) holder).imageView);
    }


    @Override
    public int getItemCount() {
        return mUploads.size();
    }

}
