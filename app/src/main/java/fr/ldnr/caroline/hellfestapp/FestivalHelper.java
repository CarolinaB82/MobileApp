package fr.ldnr.caroline.hellfestapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FestivalHelper extends SQLiteOpenHelper {

    public FestivalHelper(@Nullable Context context ) {
        super(context, "hellfest.sqlite", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //
        db.execSQL("CREATE TABLE origine (id INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT NOT NULL)");
        // creation d'origine
        String insertionOrigine = "INSERT INTO origine (nom) VALUES (?)";
        // insertion des origine dans la table origine avec des tableaux d'objets
        db.execSQL(insertionOrigine, new Object[]{"France"});
        db.execSQL(insertionOrigine, new Object[]{"Suede"});
        db.execSQL(insertionOrigine, new Object[]{"Espagne"});
        db.execSQL(insertionOrigine, new Object[]{"Australie"});
        db.execSQL(insertionOrigine, new Object[]{"Lion"});
        db.execSQL("CREATE TABLE festivalier (id INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT NOT NULL, age INTEGER NOT NULL, id_origine INTEGER NOT NULL, FOREIGN KEY (id_origine) REFERENCES origine (id))");


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int insererFestivalier(String nom, int age, String origine){
        // insertion des origine
        int id_origine = getIdOrigine(origine);
        // insertion des festivalier
        SQLiteDatabase db = getWritableDatabase();

        // insertion des festivalier
        db.execSQL("INSERT INTO festivalier (nom, age, id_origine) VALUES (?, ?, ?)",
                new Object[] {nom,age, id_origine});

        // permet de lire les donnees issue d'une base de donnees
       Cursor c = db.rawQuery("SELECT COUNT(*) FROM festivalier WHERE id_origine = ?",new String[]{String.valueOf(id_origine)});
       // je veux recuperer l'id
       c.moveToNext();
       // requete qui renvoie un element : le numero de l'espece en fonction de son nom
       int resultat = c.getInt(0);
       // je ferme la connexion
       c.close();
       // je ferme la connexion
        db.close();
        // je retourne le resultat
        return resultat;
    }

    private int getIdOrigine(String origine) {
        // je veux lire dans ma base de donnee
        SQLiteDatabase db = getReadableDatabase();
        // je veux selectionner l'id
        Cursor c = db.rawQuery("SELECT id FROM origine WHERE nom LIKE ?", new String[]{String.valueOf(origine)});
        // je veux recuperer l'id
        c.moveToNext();
        // requete qui renvoie un element : le numero de l'espece en fonction de son nom
        int id = c.getInt(0);
        // je ferme la connexion
        c.close();
        // je ferme la connexion
        db.close();
        // je retourne le resultat
        return id;
    }

    public List<String>getFestivalier(){
        // je veux lire dans ma base de donnee
        List<String> festivalier = new ArrayList<>();

        String sql = "SELECT * FROM festivalier JOIN origine ON id_origine = origine.id ORDER BY age";
        // je veux selectionner l'id
        SQLiteDatabase db = getReadableDatabase();
        // je veux recuperer l'id
        Cursor c = db.rawQuery(sql, null);
        // je fais une boucle sur les donnees selectionner
        while (c.moveToNext()) {

            festivalier.add(c.getString(1) + "|" + c.getInt(2) + "|" + c.getString(5));

        }
        c.close();
        db.close();
        return festivalier;
    }

}
