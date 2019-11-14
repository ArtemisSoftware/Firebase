package com.titan.firebase.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.titan.firebase.R;

public class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    TextView textViewTitle;
    TextView textViewDescription;
    TextView textViewPriority;
    OnFirebaseListener listener;

    public NoteHolder(View itemView, OnFirebaseListener listener) {
        super(itemView);
        textViewTitle = itemView.findViewById(R.id.text_view_title);
        textViewDescription = itemView.findViewById(R.id.text_view_description);
        textViewPriority = itemView.findViewById(R.id.text_view_priority);

        this.listener = listener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.onItemClick(getAdapterPosition());
    }
}