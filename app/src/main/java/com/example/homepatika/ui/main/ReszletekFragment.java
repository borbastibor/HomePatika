package com.example.homepatika.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homepatika.R;
import com.example.homepatika.data.DBHandlerClass;
import com.example.homepatika.data.Gyogyszer;

import org.w3c.dom.Text;

public class ReszletekFragment extends AppCompatActivity {
    private static final String TAG = "ReszletekFragment";

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reszletek_layout);
        Log.d(TAG, "onCreate");

        getIncomingIntent();
     }

     private void getIncomingIntent(){
         Log.d(TAG, "getIncomingIntent: Jott id?");
         if(getIntent().hasExtra("id")) {
             Log.d(TAG, "getIncomingIntent: - jött");
             int atadottId = getIntent().getIntExtra("id", 0);

             loadData(atadottId);
         }
     }

    private void loadData (int atvettId){
        Log.d(TAG, "loadData: betöltjük a megfelelő gyógyszer adatait az adatbázisból");

        final EditText textEditGyogyszerneve = findViewById(R.id.gyogyszerNeve);
        final EditText textEditGyogyszerleirasa = findViewById(R.id.gyogyszerLeirasa);
        final EditText textEditGyogyszerszavatossaga = findViewById(R.id.gyogyszerSzavatossaga);
        final EditText textEditGyogyszermennyisege = findViewById(R.id.gyogyszerMennyisege);
        final EditText textEditGyogyszerreceptes = findViewById(R.id.gyogyszerReceptes);        // TODO Ide kéne a Spinnert beműteni (gyogyszerReceptesSpinner)
        Button buttonGyogyszerModositas = findViewById(R.id.gyogyszerModositasGomb);

        final DBHandlerClass dbHandlerClass = new DBHandlerClass(this, null, null, 1);

        final Gyogyszer kivalasztottGyogyszer = dbHandlerClass.loadOneByIdHandler(atvettId);

        textEditGyogyszerneve.setText(kivalasztottGyogyszer.getMegnevezes());
        textEditGyogyszerleirasa.setText(kivalasztottGyogyszer.getLeiras());
        textEditGyogyszerszavatossaga.setText(kivalasztottGyogyszer.getSzavatossag());
        textEditGyogyszermennyisege.setText(String.valueOf(kivalasztottGyogyszer.getMennyiseg()));
        textEditGyogyszerreceptes.setText(String.valueOf(kivalasztottGyogyszer.getReceptes()));

        buttonGyogyszerModositas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Módosítás gomb megnyomva.");

                int elemId = kivalasztottGyogyszer.getId();
                String ujNev = textEditGyogyszerneve.getText().toString();
                String ujLeiras = textEditGyogyszerleirasa.getText().toString();
                String ujSzavatossag = textEditGyogyszerszavatossaga.getText().toString();
                int ujMennyiseg = Integer.parseInt(textEditGyogyszermennyisege.getText().toString());
                int ujReceptes = Integer.parseInt(textEditGyogyszerreceptes.getText().toString());          // TODO Ide is be kéne műteni a Spinnert (gyogyszerReceptesSpinner)


                // egyesével megvizsgáljuk, hogy a leírást leszámítva minden mező ki van-e töltve, és ha nincs, arra figyelmeztetjük a usert.
                boolean boxesFilled = true;

                if(textEditGyogyszerneve.getText().toString().length() == 0) {
                    textEditGyogyszerneve.setError("Add meg a gyógyszer nevét!");
                    boxesFilled = false;
                }

                if(textEditGyogyszerszavatossaga.getText().toString().length() == 0) {
                    textEditGyogyszerszavatossaga.setError("Add meg a gyógyszer lejárati idejét!");
                    boxesFilled = false;
                }

                if(textEditGyogyszermennyisege.getText().toString().length() == 0) {
                    textEditGyogyszermennyisege.setError("Add meg a mennyiséget!");
                    boxesFilled = false;
                }

                if(textEditGyogyszerreceptes.getText().toString().length() == 0) {
                    textEditGyogyszerreceptes.setError("Add meg, hogy a gyógyszer receptköteles-e!");
                    boxesFilled = false;
                }


                // létrehozzuk a módosított gyógyszert
                Gyogyszer modositottGyogyszer = new Gyogyszer(elemId, ujNev, ujLeiras, ujSzavatossag, ujMennyiseg, ujReceptes);

                // update-eljük az adatbázisban
                dbHandlerClass.updateHandler(modositottGyogyszer);

                if(boxesFilled == true)
                    finish();

                /* TODO
                    Ha a készlet 0-ra lett módosítva, akkor deletehandler kell update helyett! */


            }
        });
        
    }
}
