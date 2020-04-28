package com.example.homepatika.ui.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homepatika.R;
import com.example.homepatika.data.Gyogyszer;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<Gyogyszer> arrList = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapter(ArrayList<Gyogyszer> arrList, Context context) {
        this.arrList = arrList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d("", "onBindViewHolder fv.");

        holder.sor_nev.setText(arrList.get(position).getMegnevezes());
        holder.sor_leiras.setText(arrList.get(position).getLeiras());
//        holder.sor_mennyiseg.setText(arrList.get(position).getMennyiseg());
        holder.sor_mennyiseg.setText(String.valueOf(arrList.get(position).getMennyiseg()));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + arrList.get(position).getMegnevezes());
                Toast.makeText(context, arrList.get(position).getMegnevezes(), Toast.LENGTH_SHORT).show();
                /* TODO
                *   itt kell majd egy új ablak, ahol tudja módosítani az adatokat, vagy be tudja jelölni, ha elfogyott a gyógyszer.
                * */

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView sor_nev;
        TextView sor_leiras;
        TextView sor_mennyiseg;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             sor_nev = itemView.findViewById(R.id.sor_nev);
             sor_leiras = itemView.findViewById(R.id.sor_leiras);
             sor_mennyiseg = itemView.findViewById(R.id.sor_mennyiseg);
             parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
