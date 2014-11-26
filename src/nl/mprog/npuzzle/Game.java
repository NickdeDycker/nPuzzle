package nl.mprog.npuzzle;

import java.util.ArrayList;
import java.util.HashMap;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class Game extends Activity{
	
	//SharedPreferences stored = getSharedPreferences("mode", MODE_PRIVATE);
	
	//int dif = stored.getInt("difficulty", 3);
	static int N = 3;
	static int moves = 0;
	static int[] blanktile = new int[]{N-1, N-1};
	
	ImageView blanktileObj = (ImageView) null;
	
	HashMap<ImageView, int[]> imgToInt = new HashMap<ImageView, int[]>();
	
	ArrayList<ImageView> imgs = new ArrayList<ImageView>();
	
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);

        Intent startgame = getIntent();
        
        final int position = startgame.getExtras().getInt("id");
        ImageAdapter imageAdapter = new ImageAdapter(this);
        

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageAdapter.imageArray[position]);
        bitmap = Bitmap.createScaledBitmap(bitmap,1000, 1000, true);
        
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        
        TableLayout tl = (TableLayout) findViewById(R.id.table);
        
        for(int Y = N-1; Y > -1; Y = Y - 1){
        	
        	TableRow tr = new TableRow(this);
        	tr.setLayoutParams(lp);
        	
        	for(int X = N-1; X > -1; X = X - 1){
        		
        		Bitmap targetBitmap = Bitmap.createBitmap(bitmap, 
        				(int) (X*(1.0/N)*bitmap.getWidth()), 
        				(int) (Y*(1.0/N)*bitmap.getHeight()), 
        				(int) ((1.0/N) * bitmap.getWidth()), 
        				(int) ((1.0/N) * bitmap.getHeight()));
        		
        		ImageView imagev = new ImageView(this);
        		imagev.setLayoutParams(lp);
        		if(X==N-1 && Y==N-1){
        			imagev.setImageBitmap(null);
        			blanktileObj = imagev;
        		}
        		else{
        			imagev.setImageBitmap(targetBitmap);
        		}
        		
        		imagev.setTag(X + Y*N);
        		imgs.add(imagev);
        		imgToInt.put(imagev, new int[]{X, Y});
        		tr.addView(imagev);
        		tr.setClickable(true);

                imagev.setOnClickListener(tablerowOnClickListener);
        	};
        	tl.addView(tr, new TableLayout.LayoutParams(lp));
        	
        	
        };

    }
    View.OnClickListener tablerowOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
        	
        	// Hash map to get position tile
        	int[] pos = imgToInt.get(v);
        	int posx = pos[0];
        	int posy = pos[1];
        	
        	// Position blank tile
        	int posx2 = blanktile[0];
        	int posy2 = blanktile[1];
        	
        	// 1 x or 1 y value difference
        	if (((posx2 - posx == 1 || posx2 - posx == -1) && posy - posy2 == 0) || 
        	((posy2 - posy == 1 || posy2 - posy == -1) && posx - posx2 == 0)) {
        		
        		moves = moves + 1;
        		
                ImageView my_image1 = (ImageView) v;
                
                // Get contents ImageView
                Drawable img1 = my_image1.getDrawable();
                Drawable img2 = null;
                
                // Get tags from views
                int tag1 = (Integer) blanktileObj.getTag();
                int tag2 = (Integer) v.getTag();
                
                // Change the tags between the views
                blanktileObj.setTag(tag2);
                v.setTag(tag1);
                
                // Set new pos for blank tile
                blanktile = pos;
                
                // Chance contents for both views
                my_image1.setImageDrawable(img2);
                blanktileObj.setImageDrawable(img1);
                
                // Change which ImageView is the blank tile
                blanktileObj = my_image1;
                
                check(0);
            	}	
    };
    };
    
    public void check(int x){
    	int count = 0;
    	for(int i = 0; i < N*N; i = i + 1){
    		ImageView img = imgs.get(i);
    		if ((Integer) img.getTag() == i){
    			count = count + 1;
    		}
    		}
    	
    	if (count == N*N) {
    		Intent intent_win = new Intent(Game.this, Endgame.class);
    		intent_win.putExtra("turns", moves);
            startActivity(intent_win);	
    	}
    		
    	}
    	
    	
    };
