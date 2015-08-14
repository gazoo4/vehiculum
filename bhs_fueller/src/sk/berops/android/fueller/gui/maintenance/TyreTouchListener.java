package sk.berops.android.fueller.gui.maintenance;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class TyreTouchListener implements OnTouchListener {

	TouchCallbackInterface caller;
	
	public TyreTouchListener(TouchCallbackInterface caller) {
		this.caller = caller;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		v.performClick();
		if (caller == null) {
			return false;
		}
		
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			caller.touchCallback(event.getX(), event.getY());
			return true;
		}
		
		return false;
	}
}
