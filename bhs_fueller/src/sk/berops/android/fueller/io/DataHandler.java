package sk.berops.android.fueller.io;

import java.io.FileNotFoundException;

import sk.berops.android.fueller.dataModel.Garage;

public abstract class DataHandler {

	public abstract Garage loadGarage() throws FileNotFoundException;

	public abstract void persistGarage();
	
	public abstract void persistGarage(Garage garage);
}