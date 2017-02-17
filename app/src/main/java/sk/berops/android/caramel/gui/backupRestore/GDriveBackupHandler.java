package sk.berops.android.caramel.gui.backupRestore;

import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import sk.berops.android.caramel.dataModel.Garage;
import sk.berops.android.caramel.gui.MainActivity;
import sk.berops.android.caramel.io.xml.XMLWriteException;

/**
 * @author Bernard Halas
 * @date 2/3/17
 */

public class GDriveBackupHandler extends BackupHandler {
	protected MainActivity activity;

	private GoogleApiClient mGoogleApiClient;

	private boolean backupRequested;
	private boolean restoreRequested;

	public GDriveBackupHandler(MainActivity activity) {
		super(activity);
		mGoogleApiClient = new GoogleApiClient.Builder(activity)
				.addApi(Drive.API)
				.addScope(Drive.SCOPE_FILE)
				.addConnectionCallbacks(activity)
				.addOnConnectionFailedListener(activity)
				.build();
		setBackupRequested(false);
		setRestoreRequested(false);
	}

	@Override
	public void backup(final Garage garage) {
		Log.i(LOG_TAG, "Creating backup file");
		Drive.DriveApi.newDriveContents(mGoogleApiClient)
				.setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
					@Override
					public void onResult(@NonNull DriveApi.DriveContentsResult result) {
						// If the operation was not successful, we cannot do anything and must fail.
						if (!result.getStatus().isSuccess()) {
							Log.i(LOG_TAG, "Failed to create backup file");
							return;
						}

						final String tempFilename = "gdrive.tmp";
						try {
							persistXML(garage, null, tempFilename);
						} catch (XMLWriteException ex) {
							Log.e(LOG_TAG, "Backup failed. Unable create a temp XML file", ex);
							return;
						}
						File tempBackup = new File(tempFilename);
						FileInputStream ins;
						try {
							ins = new FileInputStream(tempBackup);
						} catch (IOException ex) {
							Log.e(LOG_TAG, "Unable to open backup file", ex);
							return;
						}

						OutputStream os = result.getDriveContents().getOutputStream();
						byte[] buffer = new byte[4096];
						int len;
						try {
							while ((len = ins.read(buffer)) > 0) {
								os.write(buffer, 0, len);
							}
						} catch (IOException ex) {
							Log.e(LOG_TAG, "Unable to write file contents", ex);
						}

						MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
								.setMimeType("application/caramel")
								.setTitle(BackupHandler.generateFileName())
								.build();

						IntentSender intentSender = Drive.DriveApi
								.newCreateFileActivityBuilder()
								.setInitialMetadata(metadataChangeSet)
								.setInitialDriveContents(result.getDriveContents())
								.build(mGoogleApiClient);

						try {
							activity.startIntentSenderForResult(intentSender, MainActivity.REQUEST_CODE_CREATOR, null, 0, 0, 0);
						} catch (IntentSender.SendIntentException ex) {
							Log.e(LOG_TAG, "Failed to run file chooser.", ex);
						}
					}
				});
	}

	@Override
	public Garage restore() {
		return null;
	}

	public void onResume() {
	//	mGoogleApiClient.connect();
	//	start following here: https://developers.google.com/identity/sign-in/android/start-integrating
	}

	public void onPause() {
	//	mGoogleApiClient.disconnect();
	//  start following here: https://developers.google.com/identity/sign-in/android/start-integrating
	}

	public boolean isBackupRequested() {
		return backupRequested;
	}

	public void setBackupRequested(boolean backupRequested) {
		this.backupRequested = backupRequested;
	}

	public boolean isRestoreRequested() {
		return restoreRequested;
	}

	public void setRestoreRequested(boolean restoreRequested) {
		this.restoreRequested = restoreRequested;
	}
}
