package sk.berops.android.vehiculum.io.xml;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import sk.berops.android.vehiculum.Vehiculum;
import sk.berops.android.vehiculum.dataModel.Garage;

/**
 * Class which is responsible for managing the garage load and store activities on the files. It's an
 * abstract class, which means you need to expand it to a class dealing with csv/xml/sqlite... files
 * of your choice.
 *
 * @author Bernard Halas
 * @date 2/22/17
 */

public abstract class FileDataHandler extends DataHandler {
	protected final static String DEFAULT_FILENAME = "garage";
	protected final static String TEMP_SUFFIX =  ".temp";
	protected final static String ARCHIVE_PREFIX = "v-";

	protected final static int historyCount = 100;

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

	/**
	 * Method used to build a new fileName with the suffix (.1, .2, .3,...) that goes to the past
	 * depending on how much in the historyLevel we go to
	 * @param fileName normal name of the file
	 * @param historyLevel level, based on which the suffix is created
	 * @return
	 */
	private static String buildFileHistoryName(String fileName, int historyLevel) {
		if (historyLevel == 0) {
			return fileName;
		}

		String newName = "";
		newName += fileName;
		newName += '.';
		newName += ((Integer) historyLevel).toString();
		return newName;
	}

	protected String defaultPath;
	protected String archivePath;
	protected String defaultFileName;
	protected String tempFileName;

	/**
	 * Constructor
	 * @param activity
	 */
	public FileDataHandler(Activity activity) {
		super(activity);
	}

	/**
	 * Each file type will have a different suffix (zip, xml, sql,...)
	 * @return filename suffix
	 */
	protected abstract String getDefaultSuffix();

	@Override
	public Garage loadGarage() throws IOException {
		return restoreFrom(getDefaultPath() +"/"+ getDefaultFileName());
	}

	@Override
	public void saveGarage(Garage garage) throws IOException {
		String defaultPath = getDefaultPath();
		String defaultFileName = getDefaultFileName();
		String tempFileName = getTempFileName();

		File defaultFile = new File(defaultPath +"/"+ defaultFileName);
		File tempFile = new File(defaultPath +"/"+ tempFileName);

		exportTo(garage, defaultPath +"/"+ tempFileName);
		dateOutFiles(defaultPath +"/"+ defaultFileName);
		boolean success = tempFile.renameTo(defaultFile);
		if (!success) {
			IOException ex = new IOException("Failed renaming "+ tempFile.getName() +" to "+ defaultFile.getName());
			Log.e("ERROR", ex.getMessage());
			throw ex;
		}
	}

	/**
	 * Method called when needing to persist the garage to the external storage location
	 * @param garage to be persisted
	 * @throws IOException in case we issues are faced during file handling
	 */
	public void saveToExternal (Garage garage) throws IOException {
		if (isExternalStorageWritable()) {
			String destination = getArchivePath() +"/"+ generateArchiveFileName();
			exportTo(garage, destination);
			Toast.makeText(activity, "Successfully saved to: "+ destination, Toast.LENGTH_LONG).show();
		} else {
			IOException ex = new IOException("Unable to get access to external storage");
			Log.e("ERROR", ex.getMessage());
			throw ex;
		}
	}

	/**
	 * Method used to save persist the garage object to the specified location
	 * @param garage object to be persisted
	 * @param pathFileName target location
	 * @throws IOException in case issues are faced during file handling
	 */
	public abstract void exportTo(Garage garage, String pathFileName) throws IOException;

	/**
	 * Method used to restore the garage object from the specified location
	 * @param pathFileName specified location
	 * @return garage object loaded from the specified file
	 * @throws IOException in case issues are faced during file handling
	 */
	public abstract Garage restoreFrom(String pathFileName) throws IOException;

	/**
	 * Method used to store the garage object to the specified location
	 * @param garage object to be stored
	 * @param uri location specification
	 * @throws IOException in case issues are faced during file handling
	 */
	public void exportTo(Garage garage, Uri uri) throws IOException {
		exportTo(garage, uri.getPath());
	}

	/**
	 * Method used to load/restore the garage object from a file from the specified location
	 * @param uri location specified
	 * @return garage object
	 * @throws IOException in case issues are faced during file handling
	 */
	public Garage restoreFrom(Uri uri) throws IOException {
		return restoreFrom(uri.getPath());
	}

	/**
	 * Method used to archive files on the drive in the way that (for example):
	 * garage.xml --> garage.xml.1
	 * garage.xml.1 --> garage.xml.2
	 * garage.xml.2 --> garage.xml.3
	 * ...
	 * garage.xml.(historyCount - 1) --> garage.xml.(historyCount)
	 * @param pathFileName
	 */
	private static void dateOutFiles(String pathFileName) {
		if (historyCount == 0) return;
		File files[] = new File[historyCount + 1];
		files[0] = new File(pathFileName);
		for (int i = 1; i <= historyCount; i++) {
			files[i] = new File(buildFileHistoryName(pathFileName, i));
		}

		for (int i = historyCount; i > 0; i--) {
			if (files[i-1].exists()) {
				files[i] = files[i-1];
				files[i].renameTo(new File(buildFileHistoryName(pathFileName, i)));
			}
		}
	}

	/**
	 * Method to generate the filename for backing up the garage object. Consists of 2 parts:
	 * 1) FILE_PREFIX
	 * 2) date+time suffix
	 * @return filename
	 */
	protected String generateArchiveFileName() {
		DateFormat dateFormat = new DateFormat();

		return ARCHIVE_PREFIX
				+ DEFAULT_FILENAME
				+"-"
				+ dateFormat.format("yyMMdd.HHmm", Calendar.getInstance().getTime())
				+ getDefaultSuffix();
	}

	/**
	 * Method used to return a default filename in which the garage object is/will be persisted
	 * @return default filename
	 */
	protected String getDefaultFileName() {
		return DEFAULT_FILENAME	+ getDefaultSuffix();
	}

	/**
	 * Method used to provide a temporary filename in which the garage object is/can be persisted
	 * @return
	 */
	protected String getTempFileName() {
		return DEFAULT_FILENAME	+ TEMP_SUFFIX;
	}

	/**
	 * Method to provide the default path for persisting the garage object
	 * @return path
	 */
	protected String getDefaultPath() {
		return Vehiculum.context.getFilesDir().getParentFile().getPath();
	}

	/**
	 * Method to provide the default path for archiving the garage object
	 * @return path
	 */
	protected String getArchivePath() {
		return android.os.Environment.getExternalStorageDirectory().getPath();
	}

	/**
	 * Method used to check if we have the right permissions to the external storage and whether
	 * the state of the storage allows to write into it
	 * @return true if the storage is writable
	 */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}
}