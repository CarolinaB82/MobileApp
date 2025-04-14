package fr.ldnr.caroline.hellfestapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PopCornActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new PopCornActivity.PopCornView(this));
        Log.i("PopCornActivity", "activité créée");
        Toast.makeText(this, "PopCorn", Toast.LENGTH_LONG).show();
        long duree =getIntent().getLongExtra("temps", 0);
        // reception des données dans l'intent
        // getLongExtra est un type de données long
        if (duree > 500){
            String secondes = getResources().getQuantityString(R.plurals.popcorn_secondes, (int)duree/1000);
            String texte = getString(R.string.popcorn_avertissement,(int)duree/1000, secondes);
            Toast.makeText(this, texte , Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, "Pas de popcorn ("+duree/1000+"secondes)" , Toast.LENGTH_SHORT).show();

            // 2eme etape du renvoie des données
            Intent resultat = new Intent();
            resultat.putExtra(ProgActivity.CLE_RETOUR, true);
            setResult(0, resultat);
            // code de la requete de resultat



        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            Intent intent = new Intent(this, CarteActivity.class);
            // FLAG : pas d'animation quand l'activité est lancée, n'ouvre pas l'image
            // en glissant
            //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            //intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            // reorganise la pile plutot que de lancer une nouvelle version
            // l'animation est inversé
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // ferme l'appli a la fin de la pile d'activité
            // systeme de retour a l'accueil
            // si retour arriere sur l'accueil cela ferme l'appli
            // si on avance dans l'appli les activités montent
            // si on recule dans l'appli les activités descendent
            startActivity(intent);
            // requestCode : code de la requete
        }
        return true;
    }

    public class PopCornView extends View {
        public PopCornView(Context context) {
            super(context);
        }
        //cas particulier d'heritage


        @Override
        protected void onDraw(@NonNull Canvas canvas) {
            //super.onDraw(canvas);
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.popcorn1);
            canvas.drawBitmap(bmp, 0, 0, null);
        }
    }}

