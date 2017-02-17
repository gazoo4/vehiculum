package sk.berops.android.caramel.gui.tyres;

import android.view.MotionEvent;
import android.view.View;
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
