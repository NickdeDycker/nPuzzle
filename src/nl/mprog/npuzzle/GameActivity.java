package nl.mprog.npuzzle;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableRow.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

/*
onCreate: 	- Add he picture to a ImageView and remove the ImageView after 3 seconds.
			- Retrieves the size from shared preferences
			- Calculate the size of the screen and that height/width of the scaled image.

restoreGame: - Creates N TableRows with N ImageViews in it, so N^2 tiles.
			- Set a onClickListener to each ImageView. 
			- Set a tag in the solved order to check if the state of the game is solved.

onClick: 	- Retrieve position of the tile clicked on and the blank tile
			- Either 1x or 1y difference, not both, to move.
			- Switch the ImageView resources, switch the ImageViews tags.
			- Change the blank tile variable.

check:		- Retrieve all tags of ImageViews.
			- If the order is 0 to 25 respectively, they are in the order as before shuffling.
			- Start activity EndgameActivity if the tiles are all placed right
*/

public class GameActivity extends Activity {
	
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	
	// Create variables for Board size N, number of moves and position of the blank tile.
	private static int N;
	private static int moves = 0;
	private static int[] blank_tile = new int[]{N - 1, N - 1};
	
	private int padding_pixels = 3;
	
	private final LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	private TableLayout game_board;
	
	private ImageView blanktile_view = (ImageView) null;
	
	// Mapping from ImageView to position, to determine the position when tapping.
	private HashMap<ImageView, int[]> img_to_int = new HashMap<ImageView, int[]>();
	
	// Array with all the ImageView, see check().
	private ArrayList<ImageView> imgs = new ArrayList<ImageView>();

	// Resource ID of the image being used
	private int image_id;
	
    // Final scaled picture width and height
	private int width_scaled;
	private int height_scaled;
	
	private Bitmap image_scaled;
	
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);
        
    	game_board = (TableLayout) findViewById(R.id.game_board);
        
        // Retrieve the difficulty and size of the game.
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        
        // Commit call to prevent the warning for preferences.edit().
        editor.commit();

        N = preferences.getInt("difficulty", 4); 
        
        if (preferences.getInt("game_started", 0) == 0){
	        // Reset the position
	        blank_tile[0] = N - 1;
	        blank_tile[1] = N - 1;
	        
	        // Retrieve image from intent
	        Intent start_game = getIntent();
	        image_id = start_game.getExtras().getInt("id");
	        
	        // Load the chosen image once
	        Bitmap image_raw = BitmapFactory.decodeResource(getResources(), image_id);
	        
	        getScreenSizes(image_raw);
	        
	        image_scaled = Bitmap.createScaledBitmap(image_raw, width_scaled, height_scaled, true);
	        
	        croppedImage();

	        image_raw.recycle();
	        image_scaled.recycle();
	        
	        // A 3 second timer
	        final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
            	
                @Override
                public void run() {
                	shuffleGame();
                }
            }, 3000);   
        }
    }
    
    /*
     * Creates a tiled version of the image in the correct order.
     */
    private void croppedImage() {
    	
    	// Create N rows with N tiles in it.
        for (int Y = 0; Y < N ; Y = Y + 1) {
        	
        	final TableRow table_row = new TableRow(this);
        	table_row.setLayoutParams(lp);
        	
        	for (int X = 0; X < N; X++) {
        		
        		// Scaling to previously calculated measurements
        		Bitmap tile_bitmap = Bitmap.createBitmap(image_scaled, 
        				(int) (X * (1.0 / N) * width_scaled), 
        				(int) (Y * (1.0 / N) * height_scaled), 
        				(int) ((1.0 / N) * width_scaled), 
        				(int) ((1.0 / N) * height_scaled));
        		
        		final ImageView tile = new ImageView(this);
        		tile.setLayoutParams(lp);
        		
        		tile.setPadding(padding_pixels, padding_pixels, padding_pixels, padding_pixels);
        		
        		// Set the most lower right tile as the blank tile
        		if (X == N - 1 && Y == N - 1) {
        			tile.setImageBitmap(null);
        			blanktile_view = tile;
        		}
        		else {
        			tile.setImageBitmap(tile_bitmap);
        		}

                table_row.addView(tile);

        		imgs.add(tile);
        	};
        	
            table_row.setGravity(Gravity.CENTER);
            table_row.setClickable(true);
            
        	// Add the TableRow with ImageView to the TableLayout.
            game_board.addView(table_row, new TableLayout.LayoutParams(lp));
        };    
    }
    
    /*
     * Shuffle the game in a reversed order.
     */
    private void shuffleGame() {
    	for (int i = 0; i < N * N ; i++) {
    		
    		final int tag_value = N * N - i - 1;
    		
    		// Calculate the position on the image itself.
    		int crop_x = tag_value;
    		int crop_y = 0;
    		
    		for (int row = 1; crop_x >= N ; row++) {
    			crop_x = tag_value - N * row;
    			crop_y = crop_y + 1;
    		}
    		
    		// Calculate the position on the board
    		int board_x = i;
    		int board_y = 0;
    		
    		for (int var = 1; board_x > N - 1; var++) {
    			board_x = i - N * var;
    			board_y = board_y + 1;
    		}
    		
    		final ImageView tile_1 = imgs.get(i);
    		
    		// Set the blank tile.
    		if (crop_x == N - 1 && crop_y == N - 1) {
    			blank_tile[0] = board_x;
    			blank_tile[1] = board_y;
    			blanktile_view = tile_1;
    		}
    		
    		// Switch tiles, but prevent double switching (1 to 7 and then 7 to 1).
    		if (i < N * N / 2) {
    			final ImageView tile_2 = imgs.get(tag_value);
    			
	            final Drawable img1 = tile_1.getDrawable();
	            final Drawable img2 = tile_2.getDrawable();
	
	            tile_1.setImageDrawable(img2);
	            tile_2.setImageDrawable(img1);
    		}
    		
            tile_1.setOnClickListener(moveTileOnClickListener);  
    		tile_1.setTag(tag_value);
    		img_to_int.put(tile_1, new int[]{board_x, board_y});  
    	}
    }
    
    /*
     * Creates the game from scratch or from a saved state if there is one.
     */
    private void restoreGame() {
    	
    	editor.remove("game_started");
    	editor.commit();
    	
    	TableRow table_row = new TableRow(this);
    	
    	for (int i = 0; i < N * N ; i++) {
    		
    		// Get tag value for the tile and recalculate its position.
    		final int tag_value = preferences.getInt(""+i, N * N - i - 1);
    		
    		editor.remove(""+i);
    		editor.commit();
    		
    		// Calculate the position on the image itself.
    		int crop_x = tag_value;
    		int crop_y = 0;
    		
    		for (int row = 1; crop_x >= N ; row++) {
    			crop_x = tag_value - N * row;
    			crop_y = crop_y + 1;
    		}
    		
    		// Calculate the position on the board
    		int board_x = i;
    		int board_y = 0;
    		
    		for (int var = 1; board_x > N - 1; var++) {
    			board_x = i - N * var;
    			board_y = board_y + 1;
    		}
    		
    		Bitmap bitmap_tile;
    		
    		ImageView tile_view = new ImageView(this);
    		
    		// Lower right crop in the picture is the blank tile.
    		if (crop_x == N - 1 && crop_y == N - 1) {
    			bitmap_tile = null;
    			blank_tile[0] = board_x;
    			blank_tile[1] = board_y;
    			blanktile_view = tile_view;
    		}
    		else { 
	    		bitmap_tile = Bitmap.createBitmap(image_scaled, 
	    				(int) (crop_x * (1.0 / N) * width_scaled), 
	    				(int) (crop_y * (1.0 / N) * height_scaled), 
	    				(int) ((1.0 / N) * width_scaled), 
	    				(int) ((1.0 / N) * height_scaled));
    		}
    		
    		tile_view.setLayoutParams(lp);
    		
    		// Set a border between the tiles.
    		tile_view.setPadding(padding_pixels, padding_pixels, padding_pixels, padding_pixels);
    		
    		tile_view.setTag(tag_value);
    		tile_view.setImageBitmap(bitmap_tile);
    		tile_view.setOnClickListener(moveTileOnClickListener);
    		
    		imgs.add(tile_view);
    		img_to_int.put(tile_view, new int[]{board_x, board_y});

    		table_row.addView(tile_view);
    		
            // Add the TableRow to the TableLayout and create a new Table Row when the row is full.
    		for (int row = 0; row <= N; row++) {

    			if (i > 1 && i - row * N + 1 == 0) {
    	    		table_row.setGravity(Gravity.CENTER);
    	    		table_row.setClickable(true);
    				
    				game_board.addView(table_row, new TableLayout.LayoutParams(lp)); 
    				
    	        	table_row = new TableRow(this);
    	        	table_row.setLayoutParams(lp);
    			}
    		}
    	}
    }
    
    /*
     * Handles the movement of the tiles when being tapped/clicked on.
     */
    View.OnClickListener moveTileOnClickListener = new View.OnClickListener() {
    	@Override
        public void onClick(View tile) {
        	
        	// Hash map to get the position of the tile.
        	int[] pos = img_to_int.get(tile);
        	int pos_x = pos[0];
        	int pos_y = pos[1];
        	
        	// Position blank tile
        	int pos_x2 = blank_tile[0];
        	int pos_y2 = blank_tile[1];
        	
        	// 1 x (exclusive) or 1 y value difference
        	if (((pos_x2 - pos_x == 1 || pos_x2 - pos_x == -1) && pos_y - pos_y2 == 0) || 
        	((pos_y2 - pos_y == 1 || pos_y2 - pos_y == -1) && pos_x - pos_x2 == 0)) {
        		
        		moves++;
        		
                ImageView clicked_tile = (ImageView) tile;
                
                // Get contents ImageView
                Drawable img1 = clicked_tile.getDrawable();
                Drawable img2 = null;
                
                // Get tags from views
                int tag1 = (Integer) blanktile_view.getTag();
                int tag2 = (Integer) clicked_tile.getTag();
                
                // Change the tags between the views
                blanktile_view.setTag(tag2);
                clicked_tile.setTag(tag1);
                
                // Set new position for blank tile
                blank_tile = pos;
                
                // Change contents for both views
                clicked_tile.setImageDrawable(img2);
                blanktile_view.setImageDrawable(img1);

                // Change which ImageView is the blank tile
                blanktile_view = clicked_tile;
                
                // Check if the state is solved
                check(0);
            }	
        };
    };
    
    /*
     * Checks whether the tiles are in the correct order, if so, the game is completed.
     */
    private void check(int x) {
    	int count = 0;
    	
    	// Check if all ImageViews have the corrected number in the correct order.
    	for (int i = 0; i < N * N; i = i + 1) {
    		ImageView img = imgs.get(i);
    		if ((Integer) img.getTag() == i) {
    			count = count + 1;
    		}
    	}
    	
    	// If all tiles are on the correct place, start the EndgameActivity activity.
    	if (count == N * N) {
        	
        	editor.remove("game_started");
        	editor.commit();
        	
    		Intent intent_win = new Intent(GameActivity.this, EndgameActivity.class);
    		intent_win.putExtra("turns", moves);
    		intent_win.putExtra("image_id", image_id);
    		intent_win.putExtra("width", width_scaled);
    		intent_win.putExtra("height", height_scaled);
            startActivity(intent_win);	
            this.finish();
    	}	
    }
    
    /*
     * Retrieves the screen width and height, subtracting the padding on the tiles and the status bar height.
     */
    private void getScreenSizes(Bitmap image_raw) {
    	
    	// Retrieve the height of the status bar
        int statusbar_height = getStatusBarHeight();
        
        // Correction for the padding. 2 pixels above and below each ImageView
        int padding_height = N * 2 * padding_pixels;
        
        // Retrieve screen size
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height_screen = metrics.heightPixels - statusbar_height - padding_height;
        int width_screen = metrics.widthPixels;  
        
        // Retrieve picture size and calculate ratio
        int height_image = image_raw.getHeight();
        int width_image = image_raw.getWidth();
        float ratio = (float) (height_image) / width_image;
        
        // Scaling depending on the lowest ratio width/height between image and picture.
        if (ratio < (float) (height_screen) / width_screen) {
        	width_scaled = width_screen;
            height_scaled = (int) (height_image * (float) (width_screen) / width_image);
        }
        else {
            width_scaled = (int) (width_image * (float) (height_screen) / height_image);
            height_scaled = height_screen;
        }  
    }
    
    /*
     * Retrieves the height of the status bar. Default value is 0 in case nothing is found.
     */
    private int getStatusBarHeight() {
    	int status_bar_height = 0;
  	  	int resource_id = getResources().getIdentifier("status_bar_height", "dimen", "android");
  	  	if (resource_id > 0) {
  	  		status_bar_height = getResources().getDimensionPixelSize(resource_id);
  	  	}
  	  	return status_bar_height;
    }
  
    @Override
    public void onPause() {
    	super.onPause();
    	
    	int state = preferences.getInt("state", 0);
    	
    	// Not intentionally closing, like quitting/closing the application.
    	if (state == 0) {

	    	editor.putInt("move_count", moves);
	    	editor.putInt("image_id", image_id);
	    	editor.putInt("game_started", 1);
	    	editor.commit();
	    	
	    	for (int i = 0 ; i < N * N ; i++) {
	    		ImageView tile_view = imgs.get(i);
	    		int tag_value = (Integer) tile_view.getTag();
	    		editor.putInt("" + i, tag_value);
	    		editor.commit();
	    	}
    	}
    	// Intentional closing, like resetting, finishing the game or going back to MainActivity.
    	else {
            int N = preferences.getInt("difficulty", 4);
            editor.clear();
            editor.putInt("difficulty", N);
            editor.commit();
    	}
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	
    	game_board = (TableLayout) findViewById(R.id.game_board);
    	
    	preferences = PreferenceManager.getDefaultSharedPreferences(this);
    	editor = preferences.edit();
    	
    	int game_started = preferences.getInt("game_started", 0);
    	N = preferences.getInt("difficulty", 4);
    	moves = preferences.getInt("move_count", 0);   	
    	
    	// Rebuild the image and clear the layout, then recreate the game.
    	if (game_started == 1) {
        	// Reset the variables.
        	imgs = new ArrayList<ImageView>();
        	img_to_int = new HashMap<ImageView, int[]>();
        	
        	// Clear the board in case of a onPause/onResume.
        	if (game_board.getChildCount() > 0) {
        		game_board.removeAllViews();
        	}
    		
    		image_id = preferences.getInt("image_id", 0);
        	
        	Bitmap image_raw = BitmapFactory.decodeResource(getResources(), image_id);
        	
        	// Recalculate in case the orientation of the screen changed.
        	getScreenSizes(image_raw);
        	
            image_scaled = Bitmap.createScaledBitmap(image_raw, width_scaled, height_scaled, true);
            image_raw.recycle();
    		restoreGame();
    	}
    	// Start a new game.
    	else {
    		editor.clear();
    		editor.commit();
    		
    		editor.putInt("difficulty", N);
    		editor.commit();
    		
    		moves = 0;
    	}
    }
    
    @Override
    public void onBackPressed() {
    	// Return to main menu without saving the game.
    	editor.putInt("state", 1);
    	editor.commit();
        
		Intent intent_back = new Intent(GameActivity.this, MainActivity.class);
        startActivity(intent_back);
        this.finish();
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

        // Don't save the game.
    	editor.putInt("state", 1);
        
    	Intent reset_game = getIntent();
    	reset_game.putExtra("id", image_id);
    	
        if (id == R.id.easy) {
        	editor.putInt("difficulty", 3);
        	startActivity(reset_game);
        }
        
        if (id == R.id.medium) {
            editor.putInt("difficulty", 4);
        	startActivity(reset_game);
        }
        
        if (id == R.id.hard) {
        	editor.putInt("difficulty", 5);
        	startActivity(reset_game);
        }
        
        if (id == R.id.reset) {
        	startActivity(reset_game);
        }
        
        if (id == R.id.menu) {
        	Intent to_menu = new Intent(GameActivity.this, MainActivity.class);
        	startActivity(to_menu);
        }
        
        editor.commit();
        this.finish();
        return super.onOptionsItemSelected(item);
    }
};
