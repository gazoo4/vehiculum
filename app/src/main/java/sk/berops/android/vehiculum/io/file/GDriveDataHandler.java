package sk.berops.android.vehiculum.io.file;

import android.app.Activity;

import java.io.IOException;

import sk.berops.android.vehiculum.dataModel.Garage;

public class GDriveDataHandler extends DataHandler {
	private FileDataHandler fileDataHandler;

	/**
	 * Constructor
	 * @param activity
	 */
	public GDriveDataHandler(Activity activity) {
		super(activity);
		fileDataHandler = new ArchiveDataHandler(activity);
	}

	public void setFileDataHandler(FileDataHandler fileDataHandler) {
		this.fileDataHandler = fileDataHandler;
	}

	@Override
	public Garage loadGarage() throws IOException {
		// 1) choose file
		// 2) download the file
		// 3) read the file
		fileDataHandler.restoreFrom("xxxx");
		// 4) get the garage object
		// 5) erase the file
		return null;
	}

	@Override
	public void saveGarage(Garage garage) throws IOException {
		// 1) write the file locally
		fileDataHandler.exportTo(garage, "xxxx");
		// 2) set the destination
		// 3) upload the file
		// 4) remove the local file
	}
}