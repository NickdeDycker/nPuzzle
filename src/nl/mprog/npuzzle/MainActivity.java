package nl.mprog.npuzzle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;


public class MainActivity extends Activity {

    GridView grid;
    
    //SharedPreferences.Editor editor = getSharedPreferences("mode", MODE_PRIVATE).edit();
    static int N = 3;
    
    
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //editor.putInt("difficulty", 3);
        
        GridView grid = (GridView) findViewById(R.id.gridView);
        
        ImageAdapter adapter = new ImageAdapter(this);
        
        grid.setAdapter(adapter);
        
        grid.setOnItemClickListener(new OnItemClickListener() {
        	
            public void onItemClick(AdapterView parent, View v, int position, long id) {
               Intent intent = new Intent(MainActivity.this, Image.class);
               intent.putExtra("id", position);
               startActivity(intent);	
                
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.easy) {
            N = 3;
        	//editor.putInt("difficulty", N);
        }
        if (id == R.id.medium) {
        	N = 4;
            //editor.putInt("difficulty", N);
        }
        if (id == R.id.hard) {
        	N = 5;
        	//editor.putInt("difficulty", N);
        }
        if (id == R.id.ok) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
