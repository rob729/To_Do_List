package com.example.robin.minimaltodo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.github.abdularis.civ.AvatarImageView;
import com.google.gson.Gson;

import java.util.ArrayList;

public class TaskAdapter  extends RecyclerView.Adapter<TaskAdapter.ViewHolder>  {

    private Context ctx;
    private ArrayList<Task> taskArrayList;


    public TaskAdapter(Context ctx, ArrayList<Task> taskArrayList) {
        this.ctx = ctx;
        this.taskArrayList = taskArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(ctx);
        View inflatedView = li.inflate(R.layout.item_row,parent,false);
        ViewHolder vh= new ViewHolder(inflatedView);
        return vh;


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Task t = taskArrayList.get(position);
        holder.task.setText(t.getTask());
        holder.time.setText(t.getTime());
        String x = t.getTask();
        holder.a.setText(x.charAt(0)+"");


    }

    @Override
    public int getItemCount() {
        return taskArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView task,time;
        AvatarImageView a;

        public ViewHolder(View inflatedView) {
            super(inflatedView);
            task = inflatedView.findViewById(R.id.task);
            time = inflatedView.findViewById(R.id.time);
            a = inflatedView.findViewById(R.id.TxtImg);

            inflatedView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int j = getAdapterPosition();
                    final Task t = taskArrayList.get(j);
                    taskArrayList.remove(j);
                    notifyDataSetChanged();
                    final SharedPreferences appSharedPrefs = PreferenceManager
                            .getDefaultSharedPreferences(ctx);
                    final Gson gson = new Gson();
                    MainActivity.updatePage(taskArrayList);
                    MainActivity.undo(taskArrayList,t);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(MainActivity.x==1){
                                taskArrayList.add(t);
                                notifyDataSetChanged();
                                MainActivity.updatePage(taskArrayList);
                            }
                            // Do something after 5s = 5000ms
                            Log.e("TAG","Z: "+MainActivity.x);
                            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                            String data = gson.toJson(taskArrayList);
                            prefsEditor.putString("MyTask", data);
                            prefsEditor.apply();
                            MainActivity.x=0;

                        }
                    }, 2000);
                    return true;
                }
            });
        }
    }
}
