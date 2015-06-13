package sk.berops.android.fueller;

import android.app.Application;
import android.content.Context;

public class Fueller extends Application {

	public static Context context;

	public void onCreate() {
		super.onCreate();
		Fueller.context = getApplicationContext();
	}

}