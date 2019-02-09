package com.example.renso.mydictionary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    View view;
    int state = 0;
    Button mbtnPlus;
    HashMap<Integer,String> misCategorias;
    Datos datos;
    int tipo; //0 es par, 1 es impar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        datos = new Datos(this);
        misCategorias = new HashMap<>();

        cargaBotones();
        llamaBottonesIniciales();

        mbtnPlus = findViewById(R.id.btnPlus);
        mbtnPlus.setOnClickListener(this);
    }

    private void cargaBotones(){
        //     Datos datos = new Datos(this);
        SQLiteDatabase sqLiteDatabase = datos.getReadableDatabase();
        Cursor cursor= null;
        cursor = sqLiteDatabase.rawQuery("select * from  MATERIAS",null);

        misCategorias.clear();

        if(cursor!=null) {
            if (cursor.moveToFirst()) {
                do {
                    int cod = cursor.getInt(0);
                    String categoria = cursor.getString(1);

                    misCategorias.put(cod,categoria);

                } while (cursor.moveToNext());
            }
        }
    }

    private void llamaBottonesIniciales() {

        tipo = misCategorias.size()%2;
        TableLayout table = findViewById(R.id.tableForButtons);
        Iterator itr = misCategorias.keySet().iterator();

        for(int fila=0; fila<5 && (itr.hasNext());fila++) {
            TableRow tableRow = new TableRow(this);
            table.addView(tableRow);

            for (int columna = 0; columna<2 && (itr.hasNext()); columna++){

                Integer clave = (Integer) itr.next();
                String categoria = misCategorias.get(clave);

                Button btn = new Button(this);

                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
                //btn.setLayoutParams(params);

                btn.setId(clave);
                btn.setText(categoria.toUpperCase());
                btn.setBackgroundResource(R.color.colorPrimary);


               params.setMargins(7,7,7,7);
                btn.setLayoutParams(params);

                //Color.parseColor("#c65569")

                //tableRow.setPadding(10,10,10,10);
                tableRow.addView(btn);
                registerForContextMenu(btn);
                btn.setOnClickListener(btnclick);
            }
        }
    }


    @Override
    public void onClick(View v) {
        // Build an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alertdialog_custom_view,null);

        // Specify alert dialog is not cancelable/not ignorable
        builder.setCancelable(false);

        // Set the custom layout as alert dialog view
        builder.setView(dialogView);

        // Get the custom alert dialog view widgets reference
        Button btn_positive = (Button) dialogView.findViewById(R.id.dialog_positive_btn);
        Button btn_negative = (Button) dialogView.findViewById(R.id.dialog_negative_btn);
        final EditText et_name = dialogView.findViewById(R.id.et_name);

        final AlertDialog dialog = builder.create();

        // Set positive/yes button click listener
        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the alert dialog
                dialog.cancel();

                String name = et_name.getText().toString();

                Toast.makeText(getApplication(),
                        "Curso creado", Toast.LENGTH_SHORT).show();

                creaBoton(name);
            }
        });

        // Set negative/no button click listener
        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss/cancel the alert dialog
                //dialog.cancel();
                dialog.dismiss();
                Toast.makeText(getApplication(),
                        "Curso anulada", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    private void creaBoton(String name) {

        //Datos datos = new Datos(this);
        SQLiteDatabase sqLiteDatabase = datos.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from  MATERIAS where ID = (select max(ID) from MATERIAS)",null);
        cursor.moveToFirst();
        int preCod = cursor.getInt(0);
        String preCategoria = cursor.getString(1);
        int newCod = preCod+1;

        datos.agregarCurso(datos,name.toUpperCase(),newCod);
        cargaBotones();

        TableLayout table = findViewById(R.id.tableForButtons);
        TableRow tableRow = new TableRow(this);
        table.addView(tableRow);

        if(tipo==0){
            Button btn = new Button(this);

            btn.setId(newCod);
            btn.setText(name.toUpperCase());

            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
            btn.setBackgroundResource(R.color.colorPrimary);
            params.setMargins(7,7,7,7);
            btn.setLayoutParams(params);

            tableRow.addView(btn);
            btn.setOnClickListener(btnclick);
        }else {
            //Remove current button
            Button btn = findViewById(preCod);
            ViewGroup layout = (ViewGroup) btn.getParent();
            layout.removeView(btn);

            //Add button that has removed
            Button btn1 = new Button(this);
            btn1.setId(preCod);
            btn1.setText(preCategoria.toUpperCase());

            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
            btn1.setBackgroundResource(R.color.colorPrimary);
            params.setMargins(7,7,7,7);
            btn1.setLayoutParams(params);

            tableRow.addView(btn1);
            registerForContextMenu(btn1);
            btn1.setOnClickListener(btnclick);

            //Add new button
            Button btn2 = new Button(this);

            btn2.setId(newCod);
            btn2.setText(name.toUpperCase());

            btn2.setBackgroundResource(R.color.colorPrimary);
            params.setMargins(7,7,7,7);
            btn2.setLayoutParams(params);


            tableRow.addView(btn2);
            registerForContextMenu(btn2);
            btn2.setOnClickListener(btnclick);

        }
        misCategorias.put(newCod,name);
        tipo = misCategorias.size()%2;

        /*
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        */
    }



    View.OnClickListener btnclick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            mostrarApuntes(misCategorias.get(view.getId()));
        }
    };



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        view=v;
        menu.setHeaderTitle("Choose your option");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mi_menu_context,menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_1:
                datos.eliminarCategoria(datos,view.getId(),misCategorias.get(view.getId()).toUpperCase());

                Toast.makeText(getApplication(),
                        "ELIMINADO", Toast.LENGTH_SHORT).show();

                TableLayout ll = findViewById(R.id.tableForButtons);
                ll.removeAllViews();

                cargaBotones();
                llamaBottonesIniciales();
                return true;
            case R.id.option_2:
                Toast.makeText(this, "Option 2 selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }





    private void mostrarApuntes(String categoria) {

        Intent intent = new Intent(this,ListaActivity.class);
        intent.putExtra("cat",categoria.toUpperCase());

        //Asi se pasan datos a un Activity
        startActivity(intent);
    }
}















/*
    private void creaBotonincial(String name, int cod) {

        LinearLayout dynamicview = (LinearLayout)findViewById(R.id.llayout);
        LinearLayout.LayoutParams lprams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button btn = new Button(this);
        btn.setId(cod);
        btn.setText(name);
        btn.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);

        btn.setLayoutParams(lprams);

        int resID = getResources().getIdentifier(name.toLowerCase() , "mipmap", getPackageName());
        btn.setBackgroundResource(resID);

        btn.setLayoutParams(new LinearLayout.LayoutParams(1200, 800));

        btn.setOnClickListener(btnclick);
        dynamicview.addView(btn);
    }
*/