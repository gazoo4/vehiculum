package sk.berops.android.fueller.io.xml;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.FileNotFoundException;

import sk.berops.android.fueller.Fueller;
import sk.berops.android.fueller.dataModel.Garage;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.io.DataHandler;

public class XMLHandler extends DataHandler {
	private static final int fileHistory = 100;
	static String defaultFileName = "garage.xml";

	// Storage Permissions
	private static final int REQUEST_EXTERNAL_STORAGE = 1;
	private static String[] PERMISSIONS_STORAGE = {
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE
	};

	/**
	 * Checks if the app has permission to write to device storage
	 *
	 * If the app does not has permission then the user will be prompted to grant permissions
	 */
	public static void verifyPermissions(Activity activity) {
		// Check if we have write permission
		int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

		if (permission != PackageManager.PERMISSION_GRANTED) {
			// We don't have permission so prompt the user
			ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
		}
	}
	
	public static String getFullFileName(String fileName) {
		return ""+ Fueller.context.getFilesDir().getParentFile().getPath() +"/"+ fileName;
	}
	
	public Garage loadGarage(Activity activity) throws FileNotFoundException {
		return loadFromFile(activity, defaultFileName);
	}
	
	public static Garage loadFromFile(Activity activity, String fileName) throws FileNotFoundException {
		Serializer serializer = new Persister(new FuellerCustomMatcher());
		File file = new File(getFullFileName(fileName));
		Garage garage;
		try {
			verifyPermissions(activity);

			long startTime = System.nanoTime();
			garage = serializer.read(Garage.class, file);
			long endTime = System.nanoTime();
			
			Log.d("DEBUG", "Garage loaded in "+ (endTime - startTime)/1000000 +" ms");
			
			startTime = System.nanoTime();
			garage.initAfterLoad();
			endTime = System.nanoTime();
			
			Log.d("DEBUG", "Garage initalized in "+ (endTime - startTime)/1000000 +" ms");
			return garage;
		} catch (FileNotFoundException ex) {
			throw ex;
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Could not load Garage!");
		return null;
	}
	
	public void persistGarage(Activity activity) {
		persistGarage(activity, MainActivity.garage);
	}
	
	public void persistGarage(Activity activity, Garage garage) {
		persistGarage(activity, garage, "" + defaultFileName);
	}
	
	public void persistGarage(Activity activity, Garage garage, String fileName) {
		Serializer serializer = new Persister(new FuellerCustomMatcher());
		
		fileName = getFullFileName(fileName);
		
		File file = new File(fileName);
		try {
			verifyPermissions(activity);
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