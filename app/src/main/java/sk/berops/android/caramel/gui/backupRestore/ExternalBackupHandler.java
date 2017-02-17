package sk.berops.android.caramel.gui.backupRestore;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import sk.berops.android.caramel.R;
import sk.berops.android.caramel.dataModel.Garage;
import sk.berops.android.caramel.io.xml.XMLWriteException;

/**
 * Class responsible for managing a backup of the garage to external storage destination.
 *
 * @author Bernard Halas
 * @date 2/2/17
 */

public class ExternalBackupHandler extends BackupHandler {

	public ExternalBackupHandler(Activity activity){
		super(activity);
	}

	/**
	 * Method used to store the garage object as a zip-compressed xml file.
	 * Generates file with name FILE_PREFIX-date.time.zip in folder
	 * /storage/emulated/0/Android/data/sk.berops.android.fueller/files/Documents/
	 * @param garage to be persisted
	 */
	@Override
	public void backup(Garage garage) {
		File folder = activity.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
		String filename = generateFileName();
		String toast;
		String path = folder.getPath().toString() + "/" + filename;
		try {
			persistXML(garage, folder, filename);
			compressXML(folder, filename);
			cleanUpXML(folder, filename);
			toast = activity.getResources().getString(R.string.toast_menu_backup_saved_successfully);
			toast += " " + path;
			Log.i(LOG_TAG, "File successfully saved to " + path);
		} catch (XMLWriteException | IOException ex) {
			Log.e(LOG_TAG, "Couldn't write to "+ path);
			Log.e(LOG_TAG, ex.getStackTrace().toString());
			toast = activity.getResources().getString(R.string.toast_menu_backup_not_saved_successfully);
		}
		Toast.makeText(activity.getApplicationContext(), toast, Toast.LENGTH_LONG).show();
	}

	/**
	 * This method takes xml file as an input and compresses it to zip with the same name, just different suffix.
	 * @param folder where the file is located
	 * @param filename of the input file (without ".xml" suffix)
	 * @throws IOException if the xml file can't be accessed or read
	 */
	private void compressXML(File folder, String filename) throws IOException {
		File inFile = new File(folder, filename + ".xml");
		File outFile = new File(folder, filename + ".zip");
		FileInputStream ins = new FileInputStream(inFile);
		FileOutputStream fos = new FileOutputStream(outFile);
		ZipOutputStream zos = new ZipOutputStream(fos);
		ZipEntry entry = new ZipEntry(filename + ".xml");
		zos.putNextEntry(entry);

		int len;
		byte[] buffer = new byte[4096];
		while ((len = ins.read(buffer)) > 0) {
			zos.write(buffer, 0, len);
		}

		ins.close();
		zos.closeEntry();
		zos.close();
	}

	/**
	 * Method to remove the xml file carrying the garage information
	 * @param folder where the file is located
	 * @param filename without the ".xml" suffix
	 */
	private void cleanUpXML(File folder, String filename) {
		File xmlFile = new File(folder, filename + ".xml");
		xmlFile.delete();
	}

	// TODO
	@Override
	public Garage restore() {
		return null;
	}
}
