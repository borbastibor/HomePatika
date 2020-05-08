package com.example.homepatika.ui.main;
//Zsoltiteszt

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && isResumed()) {
            Log.d(TAG, "az AktualisFragment nem látható");
        }
        else if (isVisibleToUser && isResumed()){
            Log.d(TAG, "az AktualisFragment látható");
            initList();
        }
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");

        initList(); //újratöltjük a listát
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        return inflater.inflate(R.layout.aktualis_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View content = view.findViewById(R.id.content);

        Log.d(TAG, "onViewCreated");
        initList();
    }

    private void initList() {
        DBHandlerClass dbHandlerClass = new DBHandlerClass(getActivity(), null, null, 1);
        //  Az adatbázis minden rekordjának minden elemét listába szedi. ArrayList<Gyogyszer> értékkel tér vissza.
        //  Ez a függvény alkalmazható egy teljes és részletes lista megjelenítéséhez, mely minden adatot tartalmaz.
        arrList = dbHandlerClass.loadAllListHandler();

        if (arrList.size() == 0) {
            // üres az adatbázis, feltöltjük teszt adatokkal
            Log.d(TAG, "initList: Üres adatbázis; figyelmeztetés.");
            alertDialogUres();
        }

        initRecyclerView(getView());
    }

    private void initRecyclerView(@NonNull View view){
        Log.d(TAG, "initRecyclerView: inicializálás");
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(arrList, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    //Figyelmeztetés üres adatbázis esetén
    private void alertDialogUres() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Üres az adatbázis!");
        builder.setMessage("Nincs egyetlen egy gyógyszer sem az adatbázisban. Az \"Új gyógyszer felvétele\" fülön van lehetőség hozzáadni.");
        builder.setCancelable(true);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}
