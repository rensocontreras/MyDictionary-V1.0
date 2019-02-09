package com.example.renso.mydictionary;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Datos extends SQLiteOpenHelper {


    public Datos(Context context) {
        super(context, "apuntes", null , 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table APUNTES(CATEGORIA text, TITULO text, TEXTO text, ESTADO text)");
        db.execSQL("create table MATERIAS(ID integer, CATEGORIA text);");


        db.execSQL("insert into MATERIAS(ID,CATEGORIA) values(0,'ENGLISH');");
        db.execSQL("insert into MATERIAS(ID,CATEGORIA) values(1,'MATEMATICA');");
    }

    public void agregarCliente(Datos db, String categoria, String titulo, String texto, String estado){
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        ContentValues contentValues = new ContentValues();  //El contenValues es un arreglo que conterra los valores de la tabla
        contentValues.put("CATEGORIA",categoria);
        contentValues.put("TITULO",titulo);
        contentValues.put("TEXTO",texto);
        contentValues.put("ESTADO",estado);

        sqLiteDatabase.insert("APUNTES",null, contentValues);
    }


    public void agregarCurso(Datos db, String categoria, int id){
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("CATEGORIA",categoria);
        contentValues.put("ID",id);

        sqLiteDatabase.insert("MATERIAS",null, contentValues);
    }

    public void eliminarCategoria(Datos db, int id, String categoria) {
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from MATERIAS where ID ='" + id + "'");
        sqLiteDatabase.execSQL("delete from APUNTES where CATEGORIA ='" + categoria + "'");

    }


    public void actualizarCliente(Datos db, String categoria, String titulo, String texto, String estado){
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TEXTO",texto);
        contentValues.put("ESTADO",estado);

        String consulta = "CATEGORIA like ? AND TITULO like ?";

        String arg[] = {categoria,titulo};

        sqLiteDatabase.update("APUNTES",contentValues,consulta,arg);
    }


    public void eliminarCliente(Datos db,String titulo, String categoria) {
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from APUNTES where TITULO ='" + titulo + "'" + " AND " + " CATEGORIA ='" + categoria + "'");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
