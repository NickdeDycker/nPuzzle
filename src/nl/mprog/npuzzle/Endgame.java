package nl.mprog.npuzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class Endgame extends Activity{
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_screen);
        
        Intent intent_win = getIntent();
        
        final int moves = intent_win.getExtras().getInt("turns");
        
        LinearLayout ll = (LinearLayout) findViewById(R.id.endgame);
        
        TextView tv = new TextView(this);
        
        tv.setText("Moves:");
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        
        ll.addView(tv);
}
};
