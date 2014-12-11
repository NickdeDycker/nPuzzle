package nl.mprog.npuzzle;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter{

	private Context c;
	
	protected ArrayList<Integer> image_id_array = new ArrayList<Integer>();

    /*
     * Retrieve all the drawables with a name starting with puzzle_ .
     */
    private void addAllDrawables() {
    	
    	for (int i = 0; i < 10; i++) {
    		int res_id = c.getResources().getIdentifier("puzzle_"+i, "drawable",  c.getPackageName());
    		
    		if (res_id != 0) {
    			image_id_array.add(res_id);
    		}
    	}
    }
    
	protected ImageAdapter(Context context) {
		c = context;
		addAllDrawables();
	}
	
	@Override
	public int getCount() {
		return image_id_array.size();
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
	public View getView(int position, View v, ViewGroup parent) {
		View grid;
		LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// Retrieve the ImageView from the .xml file for the layout.
        if (v == null) {
		    grid = new View(c);
		    grid = inflater.inflate(R.layout.single_grid, parent, false);
		    ImageView imageView = (ImageView)grid.findViewById(R.id.grid_single);
		    imageView.setImageResource(image_id_array.get(position));  	    
		} 
        else {
		    grid = (View) v;
		}
        
        return grid;
	}
}
