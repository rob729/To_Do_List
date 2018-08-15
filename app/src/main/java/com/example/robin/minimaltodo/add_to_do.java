package com.example.robin.minimaltodo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class add_to_do extends AppCompatActivity {

    Switch aSwitch;
    LinearLayout ll1;
    EditText edtTime, edtDate, edtTask;
    TimePickerDialog mTimePicker;
    DatePickerDialog mDatePicker;
    FloatingActionButton floatingActionButton;
    SimpleDateFormat sdf;
    ArrayList<Task> taskArrayList = new ArrayList<>();
    SharedPreferences appSharedPrefs ;
    Gson gson = new Gson();
    int day,month,year,hr,min;
    OneTimeWorkRequest notifyManager;
    boolean checked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);
        aSwitch = findViewById(R.id.HasRemind);
        ll1 = findViewById(R.id.EnterDateTime);
        edtTime = findViewById(R.id.EnterTime);
        edtDate = findViewById(R.id.EnterDate);
        edtTask = findViewById(R.id.userToDoEditText);
        appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        floatingActionButton = findViewById(R.id.makeToDoFloatingActionButton);
        final Drawable cross = getResources().getDrawable(R.drawable.ic_cancel);
        if (cross != null) {
            cross.setColorFilter(getResources().getColor(R.color.icons), PorterDuff.Mode.SRC_ATOP);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(cross);


            if (appSharedPrefs.contains("MyTask")) {
                String json = appSharedPrefs.getString("MyTask", null);
                Type type = new TypeToken<ArrayList<Task>>() {
                }.getType();
                taskArrayList = gson.fromJson(json, type);
            }

            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        ll1.setVisibility(View.VISIBLE);
                        checked=true;
                    } else {
                        ll1.setVisibility(View.INVISIBLE);
                        checked=false;
                    }
                }
            });

            edtTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Calendar mcurrentTime = Calendar.getInstance();
                    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    mTimePicker = new TimePickerDialog(add_to_do.this, new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker timePicker, int i, int i1) {
                            edtTime.setText(i + ":" + i1);
                            hr=i;
                            min=i1;

                        }
                    }, hour, minute, false);
                    mTimePicker.show();
                }
            });

            edtDate.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {
                    final Calendar mcurrentDate = Calendar.getInstance();
                    int mYear = mcurrentDate.get(Calendar.YEAR);
                    int mMonth = mcurrentDate.get(Calendar.MONTH);
                    int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                    String myFormat = "dd MMM, yyyy";
                    sdf = new SimpleDateFormat(myFormat, Locale.US);
                    mDatePicker = new DatePickerDialog(add_to_do.this, new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                            edtDate.setText(selectedday + "/" + (selectedmonth + 1) + "/" + selectedyear);
                            year = selectedyear;
                            month = selectedmonth;
                            day = selectedday;
                        }
                    }, mYear, mMonth, mDay);
                    mDatePicker.show();
                }
            });

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (edtTask.getText().toString().equals(""))
                        finish();
                    else {
                        String tk = edtTask.getText().toString();
                        DateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm", Locale.US);
                        String tm = formatter.format(new Date(System.currentTimeMillis()));
                        taskArrayList.add(new Task(tk, tm, System.currentTimeMillis()));
                        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                        String data = gson.toJson(taskArrayList);
                        prefsEditor.putString("MyTask", data);
                        prefsEditor.putString("Task", tk);
                        prefsEditor.apply();
                        if (checked) {
                            Calendar c = Calendar.getInstance();
                            c.set(year,month,day,hr,min);
                            c.set(Calendar.SECOND, 0);
                            c.set(Calendar.MILLISECOND, 0);
                            Log.e("TAG","Day: "+ day + " hr: "+ hr);
//                        if(c.getTimeInMillis() < System.currentTimeMillis()) {
//                            c.add(Calendar.DAY_OF_YEAR, 7);
//                        }
                            Log.e("TAG", c.getTimeInMillis() + " " + System.currentTimeMillis());
                            notifyManager = new OneTimeWorkRequest
                                    .Builder(notify.class)
                                    .setInitialDelay(c.getTimeInMillis() - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                                    .build();
                            WorkManager.getInstance().enqueue(notifyManager);
                        }
                        finish();
                    }
                }
            });

        }

    }



}
