package com.example.homepatika.ui.main;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homepatika.R;
import com.example.homepatika.data.DBHandlerClass;
import com.example.homepatika.data.Gyogyszer;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReszletekFragment extends AppCompatActivity {
    private static final String TAG = "ReszletekFragment";

    final Calendar szavatosagCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            szavatosagCalendar.set(Calendar.YEAR, year);
            szavatosagCalendar.set(Calendar.MONTH, month);
            szavatosagCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            szavatossagTextEditFrissitese();
        }
    };

    //
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

     // megnézzük, hogy jött-e adat (az id-t adjuk át az AktualisFragmentről)
     private void getIncomingIntent(){
         Log.d(TAG, "getIncomingIntent: Jott id?");
         if(getIntent().hasExtra("id")) {
             Log.d(TAG, "getIncomingIntent: - jött");
             int atadottId = getIntent().getIntExtra("id", 0);

             loadData(atadottId);
         }
     }

     // az áthozott gyógyszer id-je alapján lekérdezzük a gyógyszer adatait
    private void loadData (int atvettId){
        Log.d(TAG, "loadData: betöltjük a megfelelő gyógyszer adatait az adatbázisból");

        final EditText textEditGyogyszerneve = findViewById(R.id.gyogyszerNeve);
        final EditText textEditGyogyszerleirasa = findViewById(R.id.gyogyszerLeirasa);
        final EditText textEditGyogyszerszavatossaga = findViewById(R.id.gyogyszerSzavatossaga);
        final EditText textEditGyogyszermennyisege = findViewById(R.id.gyogyszerMennyisege);
        final Spinner spinnerGyogyszerreceptes = findViewById(R.id.gyogyszerReceptesSpinner);
        Button buttonGyogyszerModositas = findViewById(R.id.gyogyszerModositasGomb);
        Button buttonGyogyszerTorles = findViewById(R.id.gyogyszerTorlesGomb);

        final DBHandlerClass dbHandlerClass = new DBHandlerClass(this, null, null, 1);

        final Gyogyszer kivalasztottGyogyszer = dbHandlerClass.loadOneByIdHandler(atvettId);

        // az alkatrészek felveszik a kiválasztott gyógyszer paramétereit
        textEditGyogyszerneve.setText(kivalasztottGyogyszer.getMegnevezes());
        textEditGyogyszerleirasa.setText(kivalasztottGyogyszer.getLeiras());
        textEditGyogyszerszavatossaga.setText(kivalasztottGyogyszer.getSzavatossag());
        textEditGyogyszermennyisege.setText(String.valueOf(kivalasztottGyogyszer.getMennyiseg()));
        spinnerGyogyszerreceptes.setSelection(Integer.parseInt(String.valueOf(kivalasztottGyogyszer.getReceptes())));

        textEditGyogyszerszavatossaga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ReszletekFragment.this, date,
                        szavatosagCalendar.get(Calendar.YEAR),
                        szavatosagCalendar.get(Calendar.MONTH),
                        szavatosagCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        buttonGyogyszerModositas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Módosítás gomb megnyomva.");

                int elemId = kivalasztottGyogyszer.getId();
                String ujNev = textEditGyogyszerneve.getText().toString();
                String ujLeiras = textEditGyogyszerleirasa.getText().toString();
                String ujSzavatossag = textEditGyogyszerszavatossaga.getText().toString();
                int ujMennyiseg = Integer.parseInt(textEditGyogyszermennyisege.getText().toString());
                int ujReceptes = (int) spinnerGyogyszerreceptes.getSelectedItemId();

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

                // létrehozzuk a módosított gyógyszert
                Gyogyszer modositottGyogyszer = new Gyogyszer(elemId, ujNev, ujLeiras, ujSzavatossag, ujMennyiseg, ujReceptes);

                // update-eljük az adatbázisban
                dbHandlerClass.updateHandler(modositottGyogyszer);
                Toast.makeText(getApplicationContext(),"Sikeres módosítás",Toast.LENGTH_SHORT).show();

                if(boxesFilled == true)
                    finish();

            }
        });

        //Törlés gomb eseménye
        buttonGyogyszerTorles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Módosítás gomb megnyomva.");

                final int elemId = kivalasztottGyogyszer.getId();

                confirmDialogTorles(elemId);
            }
        });
        
    }

    // ha a datepickeren változik valami, akkor frissítjük a szavatosságot tartalmazó texteditet is
    private void szavatossagTextEditFrissitese(){
        Log.d(TAG, "szavatossagTextEditFrissitese");
        final EditText textEditGyogyszerszavatossaga = findViewById(R.id.gyogyszerSzavatossaga);

        String datumFormatum = "yyyy.MM.dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datumFormatum);
        textEditGyogyszerszavatossaga.setText(simpleDateFormat.format(szavatosagCalendar.getTime()));
    }

    //Törlés megerősítés kérése
    private void confirmDialogTorles(final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Törlés megerősítése");
        builder.setMessage("A gyógyszer a törlést követően eltűnik az adatbázisból. Ha újra szükség lesz rá, új gyógyszerként kell felvenni.\nValóban törölve legyen?");
        builder.setCancelable(false);
        builder.setPositiveButton("Igen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBHandlerClass dbHandlerClass = new DBHandlerClass(getApplicationContext(),null,null,1);
                if(dbHandlerClass.deleteHandler(id) == true) {
                    Toast.makeText(getApplicationContext(),"Sikeres törlés",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Valami hiba történt!",Toast.LENGTH_SHORT).show();
                }
                //Visszalépés a kezdő oldalra
                finish();
            }
        });

        builder.setNegativeButton("Nem", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

}
