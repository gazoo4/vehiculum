package sk.berops.android.vehiculum.io;

import android.app.Activity;

import java.io.FileNotFoundException;

import sk.berops.android.vehiculum.dataModel.Garage;
import sk.berops.android.vehiculum.io.xml.GaragePersistException;

public abstract class DataHandler {

	public abstract Garage loadGarage(Activity activity) throws FileNotFoundException;

	public abstract void persistGarage(Activity activity) throws GaragePersistException;
	
	public abstract void persistGarage(Activity activity, Garage garage) throws GaragePersistException;
}