package com.titan.firebase.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.titan.firebase.R;

public class ImageHolder extends RecyclerView.ViewHolder {

    public TextView textViewName;
    public ImageView imageView;

    public ImageHolder(View itemView) {
        super(itemView);

        textViewName = itemView.findViewById(R.id.text_view_name);
        imageView = itemView.findViewById(R.id.image_view_upload);
    }
}
