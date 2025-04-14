package fr.ldnr.caroline.hellfestapp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CarteActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new CarteView(this));
        Log.i("CarteActivity", "activité créée");
        //voila comment aller chercher le text manifest de bienvenue
        Toast.makeText(this, getString(R.string.carte_bienvenue), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            int largeur = findViewById(android.R.id.content).getWidth();
            if((int)event.getX() < largeur / 2){
            Log.i("CarteActivity", "onTouchEvent");
            // aller d'une activité a une autre
            Intent intent = new Intent(this, ProgActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://hellfest.fr/")
            );
            try {
                startActivity(intent);
            }catch (ActivityNotFoundException ex){
                Log.e("CarteActivity", "Pas de navigateur ?", ex);
                // si pb de navigateur on affiche un message
            }

            }}
        return true;
    }

    public class CarteView extends View {
        public CarteView(Context context) {
            super(context);
        }
        //cas particulier d'heritage


        @Override
        protected void onDraw(@NonNull Canvas canvas) {
            //super.onDraw(canvas);
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.hellfest);
            canvas.drawBitmap(bmp, 0, 0, null);
        }
    }
}