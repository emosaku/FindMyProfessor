package com.cis400.findmyprofessor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {

    Context context;
    ArrayList<Professor> list;

    public CardAdapter(Context context, ArrayList<Professor> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Professor professor = list.get(position);
        holder.name.setText(professor.getName());
        holder.email.setText(professor.getEmail());
        holder.office.setText(professor.getOffice());
        holder.title.setText(professor.getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, office, title;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textname);
            email = itemView.findViewById(R.id.textemail);
            office = itemView.findViewById(R.id.textoffice);
            title = itemView.findViewById(R.id.texttitle);
        }
    }
}
