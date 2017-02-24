package sk.berops.android.vehiculum.io.xml;

import android.app.Activity;

import java.io.IOException;

import sk.berops.android.vehiculum.dataModel.Garage;

/**
 * This is a class defining a minimum functionality for a DataHandler when dealing with the
 * garage. At the moment that means the ability to load the garage and to store the garage.
 *
 * @author Bernard Halas
 * @date 2/22/17
 */

public abstract class DataHandler {
	public static String LOG_TAG = "DataHandler";
	protected Activity activity;

	/**
	 * Constructor
	 * @param activity
	 */
	public DataHandler(Activity activity) {
		this.activity = activity;
	}

	/**
	 * Method used to load garage from the default location (whatever the default location is for the
	 * class that will implenent this method.
	 * @return loaded Garage object
	 */
	public abstract Garage loadGarage() throws IOException;

	/**
	 * Method used to store garage to the default location (whatever the default location is for the
	 * class that will implement this method.
	 * @param garage object to be saved
	 */
	public abstract void saveGarage(Garage garage) throws IOException;
}
