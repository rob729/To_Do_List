package com.example.robin.minimaltodo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static RelativeLayout rl1,rl2;
    RecyclerView rv;
    ArrayList<Task> al = new ArrayList<>();

    public static final String b = "420";
    static int x=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rl1 = findViewById(R.id.NoToDo);
        rl2 = findViewById(R.id.parent);
        rv = findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        FloatingActionButton mfab = findViewById(R.id.fab);
        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(),add_to_do.class);
                startActivity(i);

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()){
            case R.id.about:
                Intent nxt = new Intent(this,About.class);
                startActivity(nxt);

        }
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        if(appSharedPrefs.contains("MyTask")) {
            String json = appSharedPrefs.getString("MyTask", null);
            Type type = new TypeToken<ArrayList<Task>>() {
            }.getType();
            al = gson.fromJson(json, type);
        }
        if(appSharedPrefs.contains("List")){
            int a = appSharedPrefs.getInt("List",1);
            if(a==0){
                rl1.setVisibility(View.VISIBLE);
            }
            else
                rl1.setVisibility(View.GONE);
        }
        if(al!=null){
            if(al.size()>0) {
                rl1.setVisibility(View.GONE);

                final TaskAdapter taskAdapter = new TaskAdapter(this, al);
                rv.setAdapter(taskAdapter);


            }
            else{
                rl1.setVisibility(View.VISIBLE);
            }
        }
    }

    public static void  updatePage(final ArrayList<Task> al){
        if(al.size()>0) {
            rl1.setVisibility(View.GONE);
        }
        else{
            rl1.setVisibility(View.VISIBLE);
        }
    }

    public static void undo(final ArrayList<Task> al, final Task t){
        Snackbar snackbar = Snackbar.make(rl2, "Deleted Todo", Snackbar.LENGTH_SHORT);
        snackbar.show();
        View view = snackbar.getView();
        TextView txtv = view.findViewById(android.support.design.R.id.snackbar_text);
        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
        snackbar.setAction("undo", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                x=1;
                Log.e("TAG","x: "+x);
            }
        });
    }


}
