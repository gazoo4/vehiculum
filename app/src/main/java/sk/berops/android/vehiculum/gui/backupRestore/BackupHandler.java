package sk.berops.android.vehiculum.gui.backupRestore;

import android.app.Activity;
import android.text.format.DateFormat;

import java.io.File;
import java.util.Calendar;

import sk.berops.android.vehiculum.dataModel.Garage;
import sk.berops.android.vehiculum.io.xml.XMLHandler;
import sk.berops.android.vehiculum.io.xml.XMLWriteException;

/**
 * @author Bernard Halas
 * @date 2/7/17
 */

public abstract class BackupHandler {
	private static final String FILE_PREFIX = "crml";
	protected static final String LOG_TAG = "Backup";

	protected Activity activity;

	public BackupHandler(Activity activity) {
		this.activity = activity;
	}

	public abstract void backup(Garage garage);

	public abstract Garage restore();

	/**
	 * Method to generate the filename for backing up the garage object. Consists of 2 parts:
	 * 1) FILE_PREFIX
	 * 2) date+time suffix
	 * @return filename
	 */
	protected static String generateFileName() {
		String name = FILE_PREFIX;
		DateFormat dateFormat = new DateFormat();
		name += "-" + dateFormat.format("yyyyMMdd.HHmmss", Calendar.getInstance().getTime());
		return name;
	}

	/**
	 * Method used to persist the garage object in a xml file
	 * @param garage to be persisted
	 * @param folder to store the file in; if null, we're storing the file to internal directory
	 * @param filename of the target file (without the ".xml" suffix)
	 * @throws XMLWriteException
	 */
	protected void persistXML(Garage garage, File folder, String filename) throws XMLWriteException {
		XMLHandler xmlHandler = new XMLHandler();
		if (folder == null) {
			XMLHandler.verifyPermissions(activity);
			xmlHandler.persistGarageToInternal(activity, garage, filename);
		} else {
			xmlHandler.persistGarageToExternal(activity, garage, folder, filename);
		}
	}
}
