package sk.berops.android.fueller.io;

import android.app.Activity;

import java.io.FileNotFoundException;

import sk.berops.android.fueller.dataModel.Garage;

public abstract class DataHandler {

	public abstract Garage loadGarage(Activity activity) throws FileNotFoundException;

	public abstract void persistGarage(Activity activity);
	
	public abstract void persistGarage(Activity activity, Garage garage);
}