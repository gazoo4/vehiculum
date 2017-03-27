package sk.berops.android.vehiculum.engine.currency;

import android.os.Process;

/**
 * @author Bernard Halas
 * @date 3/20/17
 */

public class ConversionRunnable implements Runnable {

	/**
	 * Define main loop for getting the currency exchange rates and reflecting that accordingly in the app.
	 */
	@Override
	public void run() {
		// Moves the current thread into the background
		android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
	}
}
