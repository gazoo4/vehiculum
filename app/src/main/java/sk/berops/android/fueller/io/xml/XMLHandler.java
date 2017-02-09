package sk.berops.android.fueller.io.xml;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.FileNotFoundException;

import sk.berops.android.fueller.Fueller;
import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.Garage;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.io.DataHandler;

public class XMLHandler extends DataHandler {
	private Activity activity;
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

	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
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
			return garage;
		} catch (FileNotFoundException ex) {
			throw ex;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d("ERROR", "Failed loading garage");
		return null;
	}

	public void persistGarage(Activity activity) throws XMLWriteException {
		persistGarage(activity, MainActivity.garage);
	}

	public void persistGarage(Activity activity, Garage garage) throws XMLWriteException {
		persistGarageToInternal(activity, garage, "" + defaultFileName);
	}

	public void persistGarageToInternal(Activity activity, Garage garage, String fileName) throws XMLWriteException {
		
		fileName = getFullFileName(fileName);
		
		File file = new File(fileName);
		File temp = new File(fileName +".temp");

		persistGarage(activity, garage, temp);
		// If the serialization to the temp file is successful, move the content here:
		dateOutFiles(fileName);
		boolean success = temp.renameTo(file);
		if (!success) {
			XMLWriteException e = new XMLWriteException("Failed renaming "+ temp.getName() +" to "+ file.getName());
			Log.e("ERROR", "File saving problem occurred");
			throw e;
		}
	}

	public void persistGarageToExternal(Activity activity, Garage garage, File folder, String filename)  throws XMLWriteException {
		if (isExternalStorageWritable()) {
			File file = new File(folder, filename + ".xml");
			persistGarage(activity, garage, file);
		} else {
			// TODO
		}
	}

	public void persistGarage(Activity activity, Garage garage, File file) throws XMLWriteException {
		Serializer serializer = new Persister(new FuellerCustomMatcher());

		try {
			verifyPermissions(activity);
			serializer.write(garage, file);
		} catch (Exception ex) {
			Log.e("ERROR", "File serialization problem occurred");
			XMLWriteException xmlEx = new XMLWriteException("Failed serializing the garage");
			xmlEx.setStackTrace(ex.getStackTrace());
			throw xmlEx;
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