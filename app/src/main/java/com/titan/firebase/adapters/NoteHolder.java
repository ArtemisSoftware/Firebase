package com.titan.firebase.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.titan.firebase.R;

public class NoteHolder extends RecyclerView.ViewHolder {
    TextView textViewTitle;
    TextView textViewDescription;
    TextView textViewPriority;

    public NoteHolder(View itemView) {
        super(itemView);
        textViewTitle = itemView.findViewById(R.id.text_view_title);
        textViewDescription = itemView.findViewById(R.id.text_view_description);
        textViewPriority = itemView.findViewById(R.id.text_view_priority);
    }
}