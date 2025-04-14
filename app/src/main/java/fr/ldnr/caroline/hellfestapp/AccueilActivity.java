package fr.ldnr.caroline.hellfestapp;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

import java.util.List;
import java.util.Locale;
import android.os.LocaleList;
/*
 * Classe d'accueil
 */
public class AccueilActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Initialiser
        super.onCreate(savedInstanceState);
        // Charger la vue
        setContentView(R.layout.accueil);
        // Charger les donnees
        lireNouvelles();
        // Charger les evenements
        Button btCarte = findViewById(R.id.btCarte);
        // setOnClickListener
        btCarte.setOnClickListener(this);

        // Charger les evenements
        Button btProg = findViewById(R.id.btAlerte);
        // setOnClickListener 2eme facon de faire , class anonyme
        btProg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// demarrer l'activité
                Intent intent = new Intent(AccueilActivity.this, AlerteActivity.class);
                // passer des donnees
                startActivity(intent);
            }
        });

        /* Dans la deuxième méthode, le contexte this est utilisé pour créer l'objet Intent. Cela signifie que l'activité CarteActivity sera démarrée par rapport à l'activité actuelle, qui n'est pas spécifiée dans le code fourni.
         */


        Button btAnnuaire = findViewById(R.id.btAnnuaire);
        // setOnClickListener
        btAnnuaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// demarrer l'activité
                Intent intent = new Intent(AccueilActivity.this, AnnuaireActivity.class);
                // passer des donnees
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        // demarrer l'activité
       Intent intent = new Intent(this, CarteActivity.class);
       // passer des donnees
       startActivity(intent);


        /*La différence entre les deux méthodes onClick() est le contexte utilisé pour créer l'objet Intent.
Dans la première méthode, le contexte AccueilActivity.this est utilisé pour créer l'objet Intent. Cela signifie que l'activité ProgActivity sera démarrée par rapport à l'activité AccueilActivity.*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Charger le menu inflater = compresser
        getMenuInflater().inflate(R.menu.accueil, menu);
        // afficher le menu
        SharedPreferences sp = getSharedPreferences("hell", MODE_PRIVATE);
        // on verifie si le menu envoyer est coché
        menu.findItem(R.id.menu_envoyer).setChecked(
                // on verifie si le menu envoyer est coché
                sp.getBoolean("envoyer", true));
        // afficher le menu
        return true;
    }

    /**
     * Méthode appelée lorsque l'utilisateur sélectionne un item du menu.
     * @param featureId The panel that the menu is in.
     * @param item The menu item that was selected.
     *
     * @return
     */
    @Override
    public boolean onMenuItemSelected(int featureId, @NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_carte) {
            // demarrer l'activité
            Intent intent = new Intent(this, CarteActivity.class);
            // passer des donnees
            startActivity(intent);
        }
        if(item.getItemId()==R.id.menu_festivalier) {
            // demarrer l'activité
            Intent intent = new Intent(this, FestivalierActivity.class);
            // passer des donnees
            startActivity(intent);

        }
        // afficher le menu
        if(item.getItemId()==R.id.menu_envoyer){

            // si on coche ou non le menu envoyer
            item.setChecked(!item.isChecked());
            // on verifie si le menu envoyer est coché
            SharedPreferences sp = getSharedPreferences("hell", MODE_PRIVATE);
            // on crée un éditeur de préférences pour enregistrer les modifications du menu envoyer
            SharedPreferences.Editor ed = sp.edit();
            // on enregistre le menu envoyer dans le fichier hell avec la clé envoyer et la valeur du menu envoyer
            ed.putBoolean("envoyer", item.isChecked());
            // on sauvegarde les modifications
            ed.apply();
        }
        if (item.getItemId() == R.id.menu_language) {
            // Gérer le changement de langue
            changeLanguage();
        }

        // afficher le menu
        return true;
    }



    private void changeLanguage() {
        // Récupère la langue actuelle
        Locale currentLocale = getResources().getConfiguration().locale;

        // Définir la nouvelle langue (alternance entre français et anglais)
        Locale newLocale = currentLocale.getLanguage().equals("fr") ? Locale.ENGLISH : Locale.FRENCH;

        // Applique la nouvelle langue
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // Pour Android 7.0 (API 24) et plus, utiliser LocaleList
            LocaleList localeList = new LocaleList(newLocale);
            LocaleList.setDefault(localeList);

            Configuration config = new Configuration();
            config.setLocales(localeList);
            // Crée un contexte avec la nouvelle configuration
            Context context = createConfigurationContext(config);
            // Applique le contexte créé
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        } else {
            // Pour les versions inférieures à Android 7.0
            Locale.setDefault(newLocale);
            Configuration config = new Configuration();
            config.locale = newLocale;
            // Met à jour la configuration de manière classique
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }

        // Redémarre l'activité pour appliquer les changements de langue
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }



    /*
 * Methode pour lire les nouvelles
 */
    private void lireNouvelles() {
            try {
                //lecture du fichier news.txt
                InputStream is = getAssets().open("news.txt");
                // transformer un flux binaire en flux de caractere
                BufferedReader br = new BufferedReader(
                        // transformer un flux binaire en flux de caractere
                        new InputStreamReader(is, StandardCharsets.UTF_8));
                // lire ligne par ligne
                String ligne, contenu = "";
                // stockage des donnees
                // boucle
                while ((ligne = br.readLine()) != null) {
                    // ajout des donnees
                    contenu += ligne + "\n";
                }
                TextView tvNouvelles = findViewById(R.id.tvNouvelles);
                // sous java proprietes sont des get ou set
                tvNouvelles.setText(contenu);
            } catch (Exception ex) {
                Log.e("AccueilActivity", "Erreur lecture news");
            }
        }


}




