package com.example.homepatika.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.homepatika.R;

public class AktualisFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.aktualis_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View content = view.findViewById(R.id.content);

        TextView tv = view.findViewById(R.id.tvEgy);
        tv.setText("Aktuális gyógyszer készlet fog itt megjelenni egy listVienn vagy egy recyclerView-n. Ha a listában rátapizunk egy sorra, akkor tudjuk módosítani az adatait, vagy jelezni, hogy azt a gyógyszert felhasználtuk (elfogyott).");
    }


}
