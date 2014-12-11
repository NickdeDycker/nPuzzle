package nl.mprog.npuzzle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class EndgameActivity extends Activity {
	
	private Bitmap bitmap_raw;
	private Bitmap bitmap_scaled;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_screen);
        
        // Retrieve the intent with its values.
        Intent intent_win = getIntent();
        
        final int moves = intent_win.getExtras().getInt("turns");
        final int image_id = intent_win.getExtras().getInt("image_id");
        final int width = intent_win.getExtras().getInt("width");
        final int height = intent_win.getExtras().getInt("height");
        
        // Set the TextView to show the number of moves made
        TextView moves_text = (TextView) findViewById(R.id.moves);
        moves_text.setText(getString(R.string.move) + moves);

        // Scale the image to the size of screen, so smaller pictures will be full screen as well.
        bitmap_raw = BitmapFactory.decodeResource(getResources(), image_id);
        bitmap_scaled = Bitmap.createScaledBitmap(bitmap_raw, width, height, true);
        
        ImageView completed_image = (ImageView) findViewById(R.id.completed_image);
        completed_image.setImageBitmap(bitmap_scaled);
        
        // A button to return to the MainActivity.
        Button menu_button = (Button) findViewById(R.id.menu_button);
        menu_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent to_menu = new Intent(EndgameActivity.this, MainActivity.class);
				bitmap_scaled.recycle();
				bitmap_raw.recycle();
				startActivity(to_menu);
				finish();
			}
        });        
        
        // Remove the entry that determined if a game was started.
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    	SharedPreferences.Editor editor = preferences.edit();
    	
    	editor.remove("game_started");
    	editor.commit();    
	}
	
	@Override
    public void onBackPressed() {
    	// Return to main menu.       
		Intent intent_back = new Intent(EndgameActivity.this, MainActivity.class);
		bitmap_scaled.recycle();
		bitmap_raw.recycle();
        startActivity(intent_back);
        this.finish();
    }
};
