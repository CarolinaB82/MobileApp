package fr.ldnr.caroline.hellfestapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class AlerteActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alerte);

        // recuperation d'un tableau de chaine
        String[] lieux = getResources().getStringArray(R.array.alerte_lieux);
        AutoCompleteTextView etLieu = findViewById(R.id.etlieu);

        //class generique adaptateur de tableau de chaine
        // fait l'association entre le composant graphique et le tableau de chaine
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, lieux);
        etLieu.setAdapter(aa);
    }

    public void envoyerClick(View view) {
        // afficher un message de confirmation
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //titre
        builder.setTitle(R.string.alerte_soustitres);
        //message de confirmation
        builder.setMessage(R.string.alerte_confirmer);
        //icon
        builder.setIcon(R.mipmap.icon);
        //action
        builder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {

            @Override
                  public void   onClick(DialogInterface dialogInterface, int i) {
                        envoyer();
                    }
                });
        //action
        builder.setNegativeButton(android.R.string.no, null);
        //action
        builder.show();
    }
    public void envoyer() {
        EditText etIntitule = findViewById(R.id.etintitulé);
        String intitule = etIntitule.getText().toString();
        CheckBox urgentCheckBox = findViewById(R.id.cbUrgent);
        if (urgentCheckBox.isChecked()) {
            // Afficher un toast avec le message "Alerte envoyée en urgence"
            Toast.makeText(this, getString(R.string.alerte_envoi_urgent, intitule), Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(this, getString(R.string.alerte_envoi, intitule), Toast.LENGTH_SHORT).show();
        }
    }}