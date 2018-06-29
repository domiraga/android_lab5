package com.example.dr.android_lab5;

import android.app.DownloadManager;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private ArrayList<String> target;
    private SimpleCursorAdapter adapter;
    private MySQLite db;
    //public int kasuj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new MySQLite(this);
        setContentView(R.layout.activity_main);
        String[] values = new String[] {"Pies","Kot","Koń","Ptak","Ryba"};
        this.target = new ArrayList<String>();
        this.target.addAll(Arrays.asList(values));
        this.adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, db.lista(), new String[] {"_id", "gatunek"}, new int[] {android.R.id.text1, android.R.id.text2});
        ListView listview = (ListView) findViewById(R.id.listView);
        listview.setAdapter(this.adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
                TextView name = (TextView)view.findViewById(android.R.id.text1);
                Animal zwierz = db.pobierz((Integer.parseInt(name.getText().toString())));
                Intent intencja = new Intent(getApplicationContext(), DodajWpis.class);
                intencja.putExtra("element", zwierz);
                startActivityForResult(intencja, 2);
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int pos, long id) {
                TextView name = (TextView)view.findViewById(android.R.id.text1);
                Log.d("name1", name.getText().toString());
                db.usun(name.getText().toString());
                Intent intencja =  new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intencja, 3);
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public void nowyWpis (MenuItem mi)
    {
        Intent intencja = new Intent(this, DodajWpis.class);
        startActivityForResult(intencja,1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==1 && resultCode==RESULT_OK)
        {

            Bundle extras = data.getExtras();
            Animal nowy = (Animal)extras.getSerializable("nowy");
            this.db.dodaj(nowy);
            //this.db.usun();
            adapter.changeCursor(db.lista());
            adapter.notifyDataSetChanged();
        }
        if(requestCode==2 && resultCode==RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Animal nowy = (Animal)extras.getSerializable("nowy");
            this.db.aktualizuj(nowy);
            // this.db.usun();
            adapter.changeCursor(db.lista());
            adapter.notifyDataSetChanged();
        }
        if(requestCode==3 && resultCode==RESULT_OK)
        {
            adapter.changeCursor(db.lista());
            adapter.notifyDataSetChanged();
            Log.d("delete", "usuwanie");
        }
    }
}
