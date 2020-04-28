package com.example.homepatika.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homepatika.R;
import com.example.homepatika.data.DBHandlerClass;
import com.example.homepatika.data.Gyogyszer;

import java.util.ArrayList;

public class AktualisFragment extends Fragment {
    private static final String TAG = "AktualisFragment";
    private ArrayList<Gyogyszer> arrList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.aktualis_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View content = view.findViewById(R.id.content);

        Log.d(TAG, "onViewCreated: AktualisFragment");
        tesztAdatokkalFeltolt(); // csak a teszteléshez, később eltávolítható
        initList();
    }

    public void tesztAdatokkalFeltolt() {
        DBHandlerClass dbHandler = new DBHandlerClass( getActivity(), null, null, 1);
        Gyogyszer gyogyszer1 = new Gyogyszer(1, "Első neve", "Első leírás", "2022.01.01", 1, 0);
        Gyogyszer gyogyszer2 = new Gyogyszer(1, "Második neve", "Második leírás", "2022.01.02", 2, 0);
        Gyogyszer gyogyszer3 = new Gyogyszer(1, "Harmadik neve", "Harmadik leírás", "2022.01.03", 3, 0);
        Gyogyszer gyogyszer4 = new Gyogyszer(1, "Negyedik neve", "Negyedik leírás", "2022.01.04", 4, 0);
        Gyogyszer gyogyszer5 = new Gyogyszer(1, "Ötödik neve", "Ötödik leírás", "2022.01.05", 5, 0);
        Gyogyszer gyogyszer6 = new Gyogyszer(1, "Hatodik neve", "Hatodik leírás", "2022.01.06", 6, 0);
        Gyogyszer gyogyszer7 = new Gyogyszer(1, "Hetedik neve", "Hetedik leírás", "2022.01.07", 7, 0);
        Gyogyszer gyogyszer8 = new Gyogyszer(1, "Nyolcadik neve", "Nyolcadik leírás", "2022.01.08", 8, 0);

        dbHandler.addHandler(gyogyszer1);
        dbHandler.addHandler(gyogyszer2);
        dbHandler.addHandler(gyogyszer3);
        dbHandler.addHandler(gyogyszer4);
        dbHandler.addHandler(gyogyszer5);
        dbHandler.addHandler(gyogyszer6);
        dbHandler.addHandler(gyogyszer7);
        dbHandler.addHandler(gyogyszer8);
    }

    private void initList() {
        DBHandlerClass dbHandlerClass = new DBHandlerClass(getActivity(), null, null, 1);
        //  Az adatbázis minden rekordjának minden elemét listába szedi. ArrayList<Gyogyszer> értékkel tér vissza.
        //  Ez a függvény alkalmazható egy teljes és részletes lista megjelenítéséhez, mely minden adatot tartalmaz.
        arrList = dbHandlerClass.loadAllListHandler();

        initRecyclerView(getView());
    }

    private void initRecyclerView(@NonNull View view){
        Log.d(TAG, "initRecyclerView: inicializálás");
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(arrList, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

}
