package com.example.homepatika.ui.main;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homepatika.R;
import com.example.homepatika.data.Gyogyszer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder");

        holder.sor_nev.setText(arrList.get(position).getMegnevezes());
        holder.sor_leiras.setText(arrList.get(position).getLeiras());
        holder.sor_mennyiseg.setText(String.valueOf(arrList.get(position).getMennyiseg()));

        // ha lejárt a szavatossága a gyógyszernek, figyelmeztetünk
        String szavatossag = arrList.get(position).getSzavatossag();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        try {
            Date sringDatum = simpleDateFormat.parse(szavatossag);
            if (new Date().after(sringDatum)) {
                Log.d(TAG, "onBindViewHolder: lejárt szavatossági idő a következőnél: " + arrList.get(position).getMegnevezes());
                holder.sor_szavatossagiIdo.setText("Lejárt szavatosságú! (" + arrList.get(position).getSzavatossag() + ".)");
            }
            else {
                holder.sor_szavatossagiIdo.setText("");
            }

        } catch (ParseException e) {
            Log.e(TAG, "onBindViewHolder: A szavatossági idő vizsgálata közben catch ágra futott.", null);
            e.printStackTrace();
        }

        // mennyiségtől függő színekkel jelenik meg a szám és a 'db' felirat
        if ( arrList.get(position).getMennyiseg() == 0 ) {
            holder.sor_mennyiseg.setTextColor(context.getResources().getColor(R.color.nincsKeszleten));
            holder.sor_mennyiseg_db.setTextColor(context.getResources().getColor(R.color.nincsKeszleten));
        }
        else {
            holder.sor_mennyiseg.setTextColor(context.getResources().getColor(R.color.vanKeszleten));
            holder.sor_mennyiseg_db.setTextColor(context.getResources().getColor(R.color.vanKeszleten));
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + arrList.get(position).getMegnevezes());

                Intent intent = new Intent(context, ReszletekFragment.class);
                intent.putExtra("id", arrList.get(position).getId());
                context.startActivity(intent);
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
        TextView sor_mennyiseg_db;
        TextView sor_szavatossagiIdo;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             sor_nev = itemView.findViewById(R.id.sor_nev);
             sor_leiras = itemView.findViewById(R.id.sor_leiras);
             sor_mennyiseg = itemView.findViewById(R.id.sor_mennyiseg);
             sor_mennyiseg_db = itemView.findViewById(R.id.sor_mennyiseg_db);
             parentLayout = itemView.findViewById(R.id.parent_layout);
             sor_szavatossagiIdo = itemView.findViewById(R.id.sor_szavatossagiIdo);
        }
    }
}
