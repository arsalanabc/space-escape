package com.a.spaceescape2;



import org.anddev.andengine.ui.activity.BaseActivity;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Menu extends BaseActivity{
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        
        Button startgame = (Button)findViewById(R.id.StartGame);
        startgame.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        	
        		Intent StartGameIntent = new Intent(Menu.this,newgame.class);
        		startActivity(StartGameIntent);
        	}
        });
        
        
        
        Button help = (Button)findViewById(R.id.Help);
        help.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		Intent StartGameIntent = new Intent(Menu.this,TouchDragExample.class);
        		startActivity(StartGameIntent);
        	}
        });
        
        Button options = (Button)findViewById(R.id.Options);
        options.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		Intent StartGameIntent = new Intent(Menu.this, webpage.class);
        		startActivity(StartGameIntent);
        	}
        });
        
        Button credits = (Button)findViewById(R.id.Credits);
        credits.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		Intent StartGameIntent = new Intent(Menu.this, sceneMenu.class);
        		startActivity(StartGameIntent);
        	}
        });
        
    }

 /*   public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
*/
}
