package fr.ldnr.caroline.hellfestapp;

import android.annotation.SuppressLint;
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

public class ProgActivity extends Activity {
    public static String message = "activité créée";

    // constante en java
    public final static String CLE_RETOUR ="messageAffiche";

    // pour mesurer le temps
    private long debut, fin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ProgActivity.MainStageView(this));
        Log.i("MainStageActivity", "activité créée");
        Toast.makeText(this, "MainStage", Toast.LENGTH_LONG).show();
        debut = System.currentTimeMillis();
        // pour mesurer le temps
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            fin = System.currentTimeMillis();
            long duree = fin - debut;
            // pour mesurer le temps

            Intent intent = new Intent(this, PopCornActivity.class);
            intent.putExtra("temps", duree);
            // pour passer les données
            //startActivity(intent);

            // 1ere etape du renvoie des données
            startActivityForResult(intent, 0);
            // requestCode : code de la requete
        }
        return true;
    }
// 3eme etape du renvoie des données

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // appel de la constante en java
if(data!=null && data.getBooleanExtra(CLE_RETOUR, false))
    Log.i("ProgActivity", "Message popcorn affiché");
else
    Log.i("ProgActivity", "Message popcorn Pas affiché");
}

public class MainStageView extends View {
        public MainStageView(Context context) {
            super(context);
        }
        //cas particulier d'heritage


        @Override
    protected void onDraw(@NonNull Canvas canvas) {
        //super.onDraw(canvas);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.prog4);
            // Obtenir la largeur et la hauteur de la vue
            int viewWidth = getWidth();
            int viewHeight = getHeight();

            // Redimensionner le bitmap pour qu'il corresponde à la taille de la vue
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp, viewWidth, viewHeight, true);

            // Dessiner le bitmap redimensionné
            canvas.drawBitmap(scaledBitmap, 0, 0, null);
    }
}}