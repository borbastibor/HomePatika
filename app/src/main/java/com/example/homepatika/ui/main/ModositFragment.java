package com.example.homepatika.ui.main;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.homepatika.R;
import com.example.homepatika.data.DBHandlerClass;
import com.example.homepatika.data.Gyogyszer;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ModositFragment extends Fragment {
    public ModositFragment() {
    }

    private static final String TAG = "ModositFragment";

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        return inflater.inflate(R.layout.modosit_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View content = view.findViewById(R.id.content);

        final EditText textEditGyogyszerneve = getView().findViewById(R.id.gyogyszerNeve);
        final EditText textEditGyogyszerleirasa = getView().findViewById(R.id.gyogyszerLeirasa);
        final EditText textEditGyogyszerszavatossaga = getView().findViewById(R.id.gyogyszerSzavatossaga);
        final EditText textEditGyogyszermennyisege = getView().findViewById(R.id.gyogyszerMennyisege);
        final Spinner spinnerGyogyszerreceptes = getView().findViewById(R.id.gyogyszerReceptesSpinner);
        Button buttonGyogyszerFelvitele = getView().findViewById(R.id.gyogyszerFelviteleGomb);

        Log.d(TAG, "onViewCreated");

        final DBHandlerClass dbHandlerClass = new DBHandlerClass(getActivity(), null, null, 1);

        // a gyógyszer szavatosságát nem kézzel írjuk be, hanem a datepickert hívjuk segítségül
        textEditGyogyszerszavatossaga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date,
                        szavatosagCalendar.get(Calendar.YEAR),
                        szavatosagCalendar.get(Calendar.MONTH),
                        szavatosagCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        buttonGyogyszerFelvitele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Új gyógyszer felvitele gomb megnyomva.");

                // egyesével megvizsgáljuk, hogy a leírást leszámítva minden mező ki van-e töltve, és ha nincs, arra figyelmeztetjük a usert.
                boolean boxesFilled = true;

                if(textEditGyogyszerneve.getText().toString().length() == 0) {
                    textEditGyogyszerneve.setError("Add meg a gyógyszer nevét!");
                    boxesFilled = false;
                }

                if(textEditGyogyszermennyisege.getText().toString().length() == 0) {
                    textEditGyogyszermennyisege.setError("Add meg a mennyiséget!");
                    boxesFilled = false;
                }

                Log.d(TAG, "onClick: - boxesFilled értéke: " + boxesFilled);

                if(boxesFilled == true) {
                    // minden adat ki van töltve
                    String ujNev = textEditGyogyszerneve.getText().toString();
                    String ujLeiras = textEditGyogyszerleirasa.getText().toString();
                    String ujSzavatossag = textEditGyogyszerszavatossaga.getText().toString();
                    int ujMennyiseg = Integer.parseInt(textEditGyogyszermennyisege.getText().toString());
                    int ujReceptes = (int) spinnerGyogyszerreceptes.getSelectedItemId();

                    Log.d(TAG, "onClick: - ujNev=" + ujNev + ", ujLeiras=" + ujLeiras + ", ujSzavatossag=" + ujSzavatossag + ", ujMennyiseg=" + Integer.toString(ujMennyiseg) + ", ujReceptes=" + Integer.toString(ujReceptes));

                    // létrehozzuk az új gyógyszert
                    Gyogyszer ujGyogyszer = new Gyogyszer(1, ujNev, ujLeiras, ujSzavatossag, ujMennyiseg, ujReceptes);

                    // beírjuk az adatbázisba
                    dbHandlerClass.addHandler(ujGyogyszer);
                    Toast.makeText(getContext(),"Sikeres rögzítés",Toast.LENGTH_SHORT).show();

                    textEditGyogyszerneve.setText("");
                    textEditGyogyszerszavatossaga.setText("");
                    textEditGyogyszermennyisege.setText("");
                    textEditGyogyszerleirasa.setText("");
                    spinnerGyogyszerreceptes.setSelection(0);

                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getFragmentManager().popBackStack();
                }
            }

        });
    }

    // eltüntetjük a billentyűzetet, ha nincs elveszíti a fókuszt az elem
    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    // Ha a date pickeren változtatva van valami, bezárása után frissíteni kell a texteditet is
    private void szavatossagTextEditFrissitese(){
        final EditText textEditGyogyszerszavatossaga = getView().findViewById(R.id.gyogyszerSzavatossaga);

        String datumFormatum = "yyyy.MM.dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datumFormatum);
        Log.d(TAG, "szavatossagTextEditFrissitese: a dátum frissült");
        textEditGyogyszerszavatossaga.setText(simpleDateFormat.format(szavatosagCalendar.getTime()));
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }
}