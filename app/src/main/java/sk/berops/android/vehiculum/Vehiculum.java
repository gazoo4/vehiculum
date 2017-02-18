package sk.berops.android.vehiculum;

import android.app.Application;
import android.content.Context;

public class Vehiculum extends Application {

	public static Context context;

	public void onCreate() {
		super.onCreate();
		Vehiculum.context = getApplicationContext();
	}

}