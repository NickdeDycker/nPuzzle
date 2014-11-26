package nl.mprog.npuzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Image extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_screen);
        
        // Retrieve the image from the intent
        Intent intent = getIntent(); 
        final int position = intent.getExtras().getInt("id");
        
        ImageAdapter imageAdapter = new ImageAdapter(this);
        
        ImageView picture_3sec = (ImageView) findViewById(R.id.picture);
        picture_3sec.setImageResource(imageAdapter.imageArray[position]);
        
        Thread timer= new Thread()
        {
            public void run()
            {
                try
                {
                    //Display for 3 seconds
                    sleep(3000);
                }
                catch (InterruptedException e) 
                {
                    //handle exception
                    e.printStackTrace();
                }
                finally
                {   
                    // Go to activity Game and give the picture with intent.
                    Intent startgame = new Intent(Image.this, Game.class);
                    startgame.putExtra("id", position);
                    startActivity(startgame);
                }
            }
        };
        
        timer.start();
    }
}
