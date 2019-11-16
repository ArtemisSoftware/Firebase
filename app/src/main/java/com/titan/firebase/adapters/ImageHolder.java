package com.titan.firebase.adapters;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.titan.firebase.R;

public class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{

    public TextView textViewName;
    public ImageView imageView;
    public OnImageListener listener;

    public ImageHolder(View itemView, OnImageListener listener) {
        super(itemView);

        textViewName = itemView.findViewById(R.id.text_view_name);
        imageView = itemView.findViewById(R.id.image_view_upload);

        this.listener = listener;

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position);
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.setHeaderTitle("Select Action");
        MenuItem doWhatever = menu.add(Menu.NONE, 1, 1, "Do whatever");
        MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");

        doWhatever.setOnMenuItemClickListener(this);
        delete.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        if (listener != null) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {

                switch (item.getItemId()) {
                    case 1:
                        listener.onWhatEverClick(position);
                        return true;
                    case 2:
                        listener.onDeleteClick(position);
                        return true;
                }
            }
        }
        return false;
    }


}
