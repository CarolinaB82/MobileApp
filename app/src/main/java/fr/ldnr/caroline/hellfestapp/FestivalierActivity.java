package fr.ldnr.caroline.hellfestapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONObject;

public class FestivalierActivity extends Activity implements View.OnClickListener {

    //declaration du handler pour l'uitliser dans n'importe qu'elle fonction
    private Handler mainHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // afficher le layout
        setContentView(R.layout.festivalier);
        // trouver le bouton
        Button btEnvoyer = findViewById(R.id.btEnvoyer);
        // ajouter un listener
        btEnvoyer.setOnClickListener(this);

        Button btChercher = findViewById(R.id.btChercher);
        btChercher.setOnClickListener(this);
        // initialiser le handler : on fera un handler quand on lui enverra des evenements
        //passe forcement par le 1er thread
        mainHandler = new Handler (Looper.getMainLooper());
    }


    @Override
    public void onClick(View v) {

        EditText etNom=findViewById(R.id.etnom);
        EditText etAge=findViewById(R.id.etage);
        EditText etOrigine=findViewById(R.id.etorigine);

        if(v.getId()==R.id.btEnvoyer) {
            // validation des champs
            if (etNom.getText().toString().isEmpty() || etAge.getText().toString().isEmpty() || etOrigine.getText().toString().isEmpty()) {
                // afficher un message Toast si un champ est vide
                Toast.makeText(this, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
            } else {


                // récupérer les valeurs
                String nom = etNom.getText().toString();
                // transformer une chaine en nombre
                int age = Integer.parseInt(etAge.getText().toString());
                // récupérer la valeur
                String origine = etOrigine.getText().toString();

                // afficher les valeurs
                Log.i("FestivalierActivity", "Nouveau festivalier :" + nom + " " + age + " " + origine);

                FestivalHelper festivalHelper = new FestivalHelper(this);


                // inserer festivalier
                int nb = festivalHelper.insererFestivalier(nom, age, origine);
                // afficher le nombre de festivalier
                Log.i("FestivalierActivity", "C'est le nombre de festivalier de ce pays :" + nb);
                List<String> festivalier = festivalHelper.getFestivalier();
                for (String festi : festivalier)
                    Log.i("FestivalierActivity", festi);
                //pour vider les champs apres validation des données mettre des setText
                etNom.setText("");
                etAge.setText("");
                etOrigine.setText("");
            }
        }
        if(v.getId()==R.id.btChercher) {

            // mise en place de 2 threads pour faire la recherche sur le serveur
            // et sur mes donnees par defaut
            ExecutorService executorService = Executors.newFixedThreadPool(2);
            executorService.execute(this::chercher);

        }else{
            requestPermissions(new String[]{Manifest.permission.INTERNET},0);

            }}
@Override
public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions, @NonNull int[] grantResults)
            {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ExecutorService executorService = Executors.newFixedThreadPool(0);
                    executorService.execute(this::chercher);
                } else {
                    Log.i("FestivalierActivity", "Permission refusée par l'utilisateur");
                }
            }


public void chercher (){
    try {
        afficher("...");
        EditText etOrigine = findViewById(R.id.etorigine);
        String origine = etOrigine.getText().toString();
        String url = "https://fr.wikipedia.org/w/api.php?action=query&prop=extracts&e" +
                "xsentences=3&format=json&titles=" + URLEncoder.encode(origine, "UTF-8");

        // faire une recherche sur un serveur avec l' url
        Log.i("FestivalierActivity", "Recherche de :" + url);


        //  String origine = etOrigine.getText().toString();
        //                String url = "https://fr.wikipedia.org/w/api.php?action=query&prop=extracts&e" +
        //                        "xsentences=3&format=json&titles=Australie";
        // faire une recherche sur mes donnees par defaut avec le origine
        //Log.i("FestivalierActivity", "Recherche de :" + origine);

        // ouverture de la connexion
        URLConnection conn = new URL(url).openConnection();

        // envoie vers le serveur
        InputStream is = conn.getInputStream();

        String contenuJson ="";
        String ligne;
        // Utilisation de BufferedReader pour lire le contenu JSON
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        while ((ligne = br.readLine()) != null)
            contenuJson += ligne;
        br.close();
        is.close();  // Fermer l'InputStream

        // afficher le contenu JSON
        JSONObject racine = new JSONObject(contenuJson);
        // récupérer la première page
        JSONObject query = racine.getJSONObject("query");
        // récupérer les pages
        JSONObject pages = query.getJSONObject("pages");
        // récupérer le numero de la page
        String numeroPage = pages.keys().next();
        // récupérer la page
        JSONObject page = pages.getJSONObject(numeroPage);
        //récupérer l' extrait
        String contenuHtml = page.getString("extract");
        //supprimer les balises html
        contenuHtml = contenuHtml.replaceAll("<.*?>", "");
        //supprimer les espaces
        contenuHtml = contenuHtml.replaceAll("<&#160>", "");

            //afficher le contenu html
        afficher(Html.fromHtml(contenuHtml));


        // TODO lire le flux, ecrire dans la TextView



    }catch (Exception e){
        Log.e("FestivalierActivity", "Echec recherche" , e);

    }}
    private void afficher(CharSequence message){
        // afficher le message sur le thread principal
    mainHandler.post(()-> {
        TextView tvResultat = findViewById(R.id.tvResultat);
        tvResultat.setText(message);
    });
    }
}
