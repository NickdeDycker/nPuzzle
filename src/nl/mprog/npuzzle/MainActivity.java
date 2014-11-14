package nl.mprog.npuzzle;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;


public class MainActivity extends ActionBarActivity {

    GridView grid;

    static final String[] images = new String[] {
            "Picture 1", "Picture 2", "Picture 3"};
    
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        grid = (GridView) findViewById(R.id.gridView);
        
        ImageAdapter adapter = new ImageAdapter(this);
        
        grid.setAdapter(adapter);
        
        grid.setOnItemClickListener(new OnItemClickListener() {
        	
            public void onItemClick(AdapterView parent, View v, int position, long id) {
               Intent intent = new Intent(MainActivity.this, Image.class);
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
