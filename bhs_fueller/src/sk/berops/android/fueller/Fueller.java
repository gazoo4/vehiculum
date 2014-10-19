package sk.berops.android.fueller;

import android.app.Application;
import android.content.Context;

public class Fueller extends Application {

	private static Context context;

	public void onCreate() {
		super.onCreate();
		Fueller.context = getApplicationContext();
	}

	public static Context getAppContext() {
		return Fueller.context;
	}
	

}