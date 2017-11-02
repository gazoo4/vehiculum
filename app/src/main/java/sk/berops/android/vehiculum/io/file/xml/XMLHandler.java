package sk.berops.android.vehiculum.io.file.xml;

import android.app.Activity;
import android.util.Log;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import sk.berops.android.vehiculum.dataModel.Garage;
import sk.berops.android.vehiculum.io.file.FileDataHandler;
import sk.berops.android.vehiculum.io.file.VehiculumCustomMatcher;

/**
 * This class is used to define the behavior of the FileDataHandler in the XML world
 *
 * @author Bernard Halas
 * @date 2/22/17
 */

public class XMLHandler extends FileDataHandler {
	protected final static String DEFAULT_SUFFIX =  ".xml";

	/**
	 * Constructor
	 * @param activity
	 */
	public XMLHandler(Activity activity) {
		super(activity);
	}

	@Override
	public void exportTo(Garage garage, String pathFileName) throws IOException {
		Serializer serializer = new Persister(new VehiculumCustomMatcher());
		File outFile = new File(pathFileName);

		try {
			verifyPermissions(activity);
			serializer.write(garage, outFile);
		} catch (Exception ex) {
			IOException ioEx = new IOException("Failed serializing the garage");
			ioEx.setStackTrace(ex.getStackTrace());
			Log.e("ERROR", ioEx.getMessage());
			throw ioEx;
		}
	}

	@Override
	public Garage restoreFrom(String pathFileName) throws IOException {
		Serializer serializer = new Persister(new VehiculumCustomMatcher());
		File inFile = new File(pathFileName);

		Garage garage;
		try {
			verifyPermissions(activity);

			long startTime = System.nanoTime();
			garage = serializer.read(Garage.class, inFile);
			long endTime = System.nanoTime();

			Log.d("DEBUG", "Garage loaded in "+ (endTime - startTime)/1000000 +" ms");
			return garage;
		} catch (FileNotFoundException ex) {
			throw ex;
		} catch (Exception ex) {
			IOException ioEx = new IOException("Problem during deserialization of the garage from "+ inFile.getPath()
					+ " " + ex.getMessage());
			ioEx.setStackTrace(ex.getStackTrace());
			Log.e("ERROR", ex.getMessage());
			Log.e("ERROR", ex.getStackTrace().toString());
			throw ioEx;
		}
	}

	@Override
	protected boolean isUpToDate(String pathname) {

		return false;
	}

	@Override
	protected void updateVersion(String pathname, String backupPathname) {

	}

	@Override
	protected String getDefaultSuffix() {
		return DEFAULT_SUFFIX;
	}
}
