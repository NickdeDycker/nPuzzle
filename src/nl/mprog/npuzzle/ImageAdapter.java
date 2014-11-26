package nl.mprog.npuzzle;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter{

	// private context? Better name?
	Context c;
	
	public Integer[] imageArray = {
			R.drawable.puzzle_0,
			R.drawable.puzzle_4,
			R.drawable.puzzle_2,
			R.drawable.puzzle_3
	};
	
	public ImageAdapter(Context context) {
		c = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imageArray.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView imageGrid = new ImageView(c);
		
		WindowManager wm = (WindowManager)c.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		
		int height = (int) (metrics.heightPixels / 2.5);
		int width = (int) (metrics.widthPixels / 2.5);
		
		if (height > width){
			height = width;
		}
		else{
			width = height;
		}
		
	    imageGrid.setLayoutParams(new GridView.LayoutParams(width, height));
	    imageGrid.setAdjustViewBounds(true);
	    imageGrid.setScaleType(ImageView.ScaleType.FIT_CENTER);
		imageGrid.setPadding(0, 100, 0 ,0);
		
		imageGrid.setImageResource(imageArray[position]);
		
		return imageGrid;
	}

}
