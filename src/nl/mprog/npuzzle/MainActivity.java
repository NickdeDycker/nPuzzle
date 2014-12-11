package nl.mprog.npuzzle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        final Intent start_game = new Intent(MainActivity.this, GameActivity.class);
        
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);       
        
        // Retrieves whether if there still is a game that was played, but got destroyed.
        int game_started = preferences.getInt("game_started", 0);
        
        // If a game was started, restore this ASAP.
        if (game_started == 1) {
        	startActivity(start_game);
        }
        else {
	        setContentView(R.layout.start_screen); 
	        
	        SharedPreferences.Editor editor = preferences.edit();
	       
	        // Set the state to 1 to say onPause (at Game activity) should save the game.
	        editor.putInt("state", 1);
	        editor.commit();
	        
	        GridView grid = (GridView) findViewById(R.id.gridView);
	        
	        // A custom adapter to show images in a GridView.
	        final ImageAdapter image_adapter = new ImageAdapter(this);
	        
	        grid.setAdapter(image_adapter);
	        
	        // Clicking on a image starts the activity Game and sends the Resource ID of the image.
	        grid.setOnItemClickListener(new OnItemClickListener() {
	            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            	start_game.putExtra("id", image_adapter.image_id_array.get(position));
	            	startActivity(start_game);	
	            }
	        });
        }
    }
    
    @Override
    public void onBackPressed() {	
    	super.onBackPressed();
    	this.finish();
    }
}
