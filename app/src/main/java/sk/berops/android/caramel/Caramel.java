package sk.berops.android.caramel;

import android.app.Application;
import android.content.Context;

public class Caramel extends Application {

	public static Context context;

	public void onCreate() {
		super.onCreate();
		Caramel.context = getApplicationContext();
	}

}