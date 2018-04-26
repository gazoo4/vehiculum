package sk.berops.android.vehiculum.io.file;

import android.app.Activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import sk.berops.android.vehiculum.dataModel.Garage;
import sk.berops.android.vehiculum.io.file.xml.XMLHandler;

/**
 * @author Bernard Halas
 * @date 2/23/17
 */

public class ArchiveDataHandler extends FileDataHandler {
	protected final static String DEFAULT_SUFFIX =  ".zip";

	private FileDataHandler fileHandler;

	/**
	 * Constructor
	 * @param activity
	 */
	public ArchiveDataHandler(Activity activity) {
		super(activity);
		fileHandler = new XMLHandler(activity);
	}

	public void setFileHandler(FileDataHandler handler) {
		this.fileHandler = handler;
	}

	// TODO: implement the following two methods, these are here just for testing purposes

	@Override
	protected boolean isUpToDate(String pathname) {return true;}

	@Override
	protected void updateVersion(String pathname, String backup) {
		return;
	}

	@Override
	public void exportTo(Garage garage, String pathFileName) throws IOException{
		fileHandler.exportTo(garage, pathFileName + TEMP_SUFFIX);
		compress(pathFileName + TEMP_SUFFIX, pathFileName);
		new File(pathFileName + TEMP_SUFFIX).delete();
	}

	@Override
	public Garage restoreFrom(String pathFileName) throws IOException {
		Garage result;
		decompress(pathFileName, pathFileName + TEMP_SUFFIX);
		result = fileHandler.restoreFrom(pathFileName + TEMP_SUFFIX);
		new File(pathFileName + TEMP_SUFFIX).delete();
		return result;
	}

	@Override
	protected String getDefaultSuffix() {
		return DEFAULT_SUFFIX;
	}

	/**
	 * This method takes datafile as an input and compresses it to zip with the same name, just different suffix.
	 * @param pathFileNameIn of the input file
	 * @param pathFileNameOut of the output file
	 * @throws IOException if the xml file can't be accessed or read
	 */
	private void compress(String pathFileNameIn, String pathFileNameOut) throws IOException {
		String[] pathElementsIn = pathFileNameIn.split("/");
		String fileNameIn = pathElementsIn[pathElementsIn.length - 1];

		File inFile = new File(pathFileNameIn);
		File outFile = new File(pathFileNameOut);
		FileInputStream ins = new FileInputStream(inFile);
		FileOutputStream fos = new FileOutputStream(outFile);
		ZipOutputStream zos = new ZipOutputStream(fos);
		ZipEntry entry = new ZipEntry(fileNameIn);
		// the fileNameIn is not important at this stage as when restoring this data the filename is not read
		// any name would work here
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
	 * This method takes the
	 * @param pathFileNameIn
	 * @param pathFileNameOut
	 * @throws IOException
	 */
	private void decompress(String pathFileNameIn, String pathFileNameOut) throws IOException {
		File outFile = new File(pathFileNameOut);
		FileOutputStream fos = new FileOutputStream(outFile);

		ZipInputStream zis = new ZipInputStream(new FileInputStream(pathFileNameIn));

		if (zis.getNextEntry() != null) {
			// is there anything in the archive?
			int len;
			byte[] buffer = new byte[4096];
			while ((len = zis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
			fos.close();
		}
		zis.closeEntry();
		zis.close();
	}
}