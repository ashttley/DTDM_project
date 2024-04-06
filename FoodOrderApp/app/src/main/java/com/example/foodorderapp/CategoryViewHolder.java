package com.example.foodorderapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    TextView textView;
    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
     textView = itemView.findViewById(R.id.textViewMessage);
    }
}
