package com.joint.cinemapp;

import java.util.ArrayList;

import com.joint.cinemapp.entity.MovieListen;
import com.jumplife.adapter.MovieListenGridAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.AdapterView.OnItemClickListener;

public class MovieListenGridViewActivity extends Activity {
	
	private ArrayList<MovieListen> MovieListenList;
	private GridView movielistenGridView;
    private MovieListenGridAdapter adapter;
	private LoadDataTask loadtask;
	private boolean isPlay = false;
	private ImageButton play;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movielisten);

        initView();
        
        loadtask = new LoadDataTask();
	    if(Build.VERSION.SDK_INT < 11)
	    	loadtask.execute();
        else
        	loadtask.executeOnExecutor(LoadDataTask.THREAD_POOL_EXECUTOR, 0);
    }
	
	private void initView() {
		movielistenGridView = (GridView)findViewById(R.id.gridview_movielisten);
		play = (ImageButton)findViewById(R.id.play);
		play.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				isPlay = !isPlay;
				if(isPlay)
					play.setImageResource(R.drawable.pause);
				else
					play.setImageResource(R.drawable.play);
			}			
		});
	}
	
	private void fetchData() {
		MovieListenList = new ArrayList<MovieListen>(10);
		
		MovieListenList.add(new MovieListen().fakeData1());
		MovieListenList.add(new MovieListen().fakeData2());
		MovieListenList.add(new MovieListen().fakeData3());
	}
	
	private void setView() {		
		movielistenGridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	Intent newAct = new Intent();
            	Bundle bundle = new Bundle();
                bundle.putInt("Id", MovieListenList.get(position).getId());
	            newAct.setClass(MovieListenGridViewActivity.this, ListenViewPagerActivity.class);
	            MovieListenGridViewActivity.this.startActivity(newAct);
            }
        });
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        
        adapter = new MovieListenGridAdapter(MovieListenGridViewActivity.this, MovieListenList,
        		(int)(screenWidth / 2 - 80), (int)(screenWidth / 2 - 80));
		movielistenGridView.setAdapter(adapter);
	}
	
	class LoadDataTask extends AsyncTask<Integer, Integer, String>{  
        
    	@Override  
        protected void onPreExecute() {
    		super.onPreExecute();  
        }  
          
        @Override  
        protected String doInBackground(Integer... params) {
        	Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        	fetchData();
            return "progress end";  
        }  
  
        @Override  
        protected void onProgressUpdate(Integer... progress) {    
            super.onProgressUpdate(progress);  
        }  
  
        @Override  
        protected void onPostExecute(String result) {
        	if(MovieListenList != null && MovieListenList.size() > 0){
        		setView();
        	}
        	super.onPostExecute(result);
        }
    }
}
