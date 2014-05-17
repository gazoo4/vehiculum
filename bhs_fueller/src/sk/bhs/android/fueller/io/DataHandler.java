package sk.bhs.android.fueller.io;

import java.io.FileNotFoundException;

import sk.bhs.android.fueller.dataModel.Garage;

public abstract class DataHandler {

	public abstract Garage loadGarage() throws FileNotFoundException;

	public abstract void persistGarage();
	
	public abstract void persistGarage(Garage garage);
}