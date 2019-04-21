package com.example.asynctask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

public class MainActivity extends AppCompatActivity {

    private ListView list;
    private ProgressBar progress;
    private ArrayList items;
    private MyTask myTask ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     // requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);
        findId();
        createObject();
        makinglist();
        addItems();
        settingAdapter();
       myTask.execute();
       // myTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private void findId() {

        progress=findViewById(R.id.progress);
        list=findViewById(R.id.list);
    }
    private void createObject() {
        //making object of extended class of asynctask
        myTask=new MyTask();
    }

    private void makinglist()
    {
        //creating list
        items=new ArrayList();
    }
    private void addItems(){
        //adding items
        for(int i=0;i<100;i++)
        {
            items.add("hello");
        }
    }
    private void settingAdapter()
    {
        //setting adapter list
        list.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,new ArrayList<>()));
    }


    class MyTask extends AsyncTask
    {
        int count=0;

        ArrayAdapter adapter;
        @Override
        protected void onPreExecute() {

            //getting a list adapter and show visibility of progress bar
            adapter = (ArrayAdapter) list.getAdapter();
          //  progress.setIndeterminate(false);
            progress.setVisibility(View.VISIBLE);

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            //iterating object with running in background and initiating progress for updation of data
            for(Object data:items){

                publishProgress(data);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i("tag",String.valueOf(Thread.currentThread().getId()));
                if(isCancelled()){
                    break;
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            //updating and addding data to adapter

            adapter.add(values[0]);
            count++;
           // progress.setProgress((int)((double)count/items.size())*10000);
            progress.setProgress(count);


        }

        @Override
        protected void onPostExecute(Object o) {

            //executing in last and display it when progress is loaded
            Toast.makeText(MainActivity.this, "Loaded successfully", Toast.LENGTH_SHORT).show();
            progress.setVisibility(View.INVISIBLE);

        }


    }

    @Override
    protected void onDestroy() {
        // cancel the thread task on backward
        super.onDestroy();
        Log.i("tag", "onDestroy: ");
      myTask.cancel(true);
    }
}
