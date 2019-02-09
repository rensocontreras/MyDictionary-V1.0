package com.example.renso.mydictionary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ListaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    ArrayList<HashMap<String,String>> arrayList;
    Button mbtnAgregar;
    static String tipoCategoria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        Intent i=getIntent();  //Con getIntent obtenemos el Intent que ha provocato l'attivazione dell'activity Lista Activity
        String temp = i.getStringExtra("cat");

        if(temp != null)
            tipoCategoria =temp;

        Datos datos = new Datos(this);

        //Se recupero los datos en modo lectura
        SQLiteDatabase sqLiteDatabase = datos.getReadableDatabase();

        //Se define la consulta
        Cursor cursor= null;
        cursor = sqLiteDatabase.rawQuery("select * from  APUNTES where CATEGORIA =?",new String[] {tipoCategoria});


        /*
             //Se define la consulta
        String myQuery = "select * from  APUNTES where CATEGORIA = " + tipoCategoria;
        Cursor cursor = sqLiteDatabase.rawQuery(myQuery, null);
         */


        ListView mlvUsuarios = findViewById(R.id.lvUsuarios);
        arrayList = new ArrayList<>();

        if(cursor!=null){
            if(cursor.moveToFirst()){
                do{
                    //Se obtienen los datos de cada columna
                    String categoria = cursor.getString(0);
                    String titulo = cursor.getString(1);
                    String texto = cursor.getString(2);
                    String estado = cursor.getString(3);

                    HashMap<String,String> map = new HashMap<>();
                    map.put("cat",categoria);
                    map.put("tit",titulo);
                    map.put("text",texto);
                    map.put("est",estado);

                    arrayList.add(map);

                    //Repite mientras pueda avanzar de registro
                }while(cursor.moveToNext());
            }
        }

        ListAdapter listAdapter = new SimpleAdapter(
                this,
                arrayList,
                R.layout.item_usuarios,
                new String[]{"cat","tit","text","est"},
                new int[]{ R.id.tvCategoria, R.id.tvTitulo, R.id.tvTexto, R.id.tvEstado}
        ){  @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position,convertView,parent);

                    // Initialize a TextView for ListView each Item
                    TextView tv = (TextView) view.findViewById(R.id.tvEstado);

                    if (tv.getText().equals("INCOMPLETO")) {
                        // Set the text color of TextView (ListView Item)
                        //tv.setTextColor(Color.RED);

                        view.setBackgroundColor(Color.parseColor("#FF0000"));
                    } else view.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    return view;

        }
        };

        mlvUsuarios.setAdapter(listAdapter);
        mlvUsuarios.setOnItemClickListener(this);

        mbtnAgregar = findViewById(R.id.btnAgregar);

        //Se implementa el evento onclick para cada objeto
        mbtnAgregar.setOnClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        HashMap<String,String> map = arrayList.get(position);
        String categoria = map.get("cat").toString();
        String titulo = map.get("tit").toString();
        String texto = map.get("text").toString();
        String estado = map.get("est").toString();

        Bundle bundle = new Bundle();
        bundle.putString("vcategoria",categoria);
        bundle.putString("vtitulo",titulo);
        bundle.putString("vtexto",texto);
        bundle.putString("vestado",estado);

        Intent intent = new Intent(this,AccederNotaActivity.class);
        intent.putExtras(bundle);

        //Asi se pasan datos a un Activity
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(this,FormularioActivity.class);
        intent.putExtra("cat",tipoCategoria);

        //Asi se pasan datos a un Activity
        startActivity(intent);
        finish();
    }
}
