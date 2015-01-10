package sk.berops.android.fueller.io.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.content.Context;
import android.util.Log;
import android.view.View;
import sk.berops.android.fueller.Fueller;
import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.Garage;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.io.DataHandler;

public class XMLHandler extends DataHandler {
	private static final int fileHistory = 5;
	static String defaultFileName = "garage.xml";
	
	public static String getFullFileName(String fileName) {
		return ""+ Fueller.getAppContext().getFilesDir().getParentFile().getPath() +"/"+ fileName;
	}
	
	public Garage loadGarage() throws FileNotFoundException {
		return loadFromFile(defaultFileName);
	}
	
	public static Garage loadFromFile(String fileName) throws FileNotFoundException {
		Serializer serializer = new Persister();
		File file = new File(getFullFileName(fileName));
		Garage garage;
		try {
			long startTime = System.nanoTime();
			garage = serializer.read(Garage.class, file);
			long endTime = System.nanoTime();
			
			Log.d("DEBUG", "Garage loaded in "+ (endTime - startTime)/1000000 +"ms");
			
			startTime = System.nanoTime();
			garage.initAfterLoad();
			endTime = System.nanoTime();
			
			Log.d("DEBUG", "Garage initalized in "+ (endTime - startTime)/1000000 +"ms");
			return garage;
		} catch (FileNotFoundException ex) {
			throw ex;
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Could not load Garage!");
		return null;
	}
	
	public void persistGarage() {
		persistGarage(MainActivity.garage);
	}
	
	public void persistGarage(Garage garage) {
		persistGarage(garage, "" + defaultFileName);
	}
	
	public void persistGarage(Garage garage, String fileName) {
		Serializer serializer = new Persister();
		
		fileName = getFullFileName(fileName);
		
		File file = new File(fileName);
		try {
			dateOutFiles(fileName);
			serializer.write(garage, file);
		} catch (Exception e) {
			System.out.println("An exception during an automated serialization and persistence of an object");
			e.printStackTrace();
		}
	}
	
	private static void dateOutFiles(String fileName) {
		if (fileHistory == 0) return;
		File files[] = new File[fileHistory + 1];
		files[0] = new File(fileName);
		for (int i = 1; i <= fileHistory; i++) {
			files[i] = new File(buildFileHistoryName(fileName, i));
		}
		
		for (int i = fileHistory; i > 0; i--) {
			if (files[i-1].exists()) {
				files[i] = files[i-1];
				files[i].renameTo(new File(buildFileHistoryName(fileName, i)));
			}
		}
	}
	
	private static String buildFileHistoryName(String fileName, int historyLevel) {
		String newName = "";
		if (historyLevel == 0) {
			return fileName;
		}
		newName += fileName;
		newName += '.';
		newName += ((Integer) historyLevel).toString();
		return newName;
	}
}
