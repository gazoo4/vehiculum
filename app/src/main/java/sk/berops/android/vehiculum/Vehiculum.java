package sk.berops.android.vehiculum;

import android.app.Application;
import android.content.Context;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class Vehiculum extends Application {

	public static Context context;

	public void onCreate() {
		super.onCreate();
		Fabric.with(this, new Crashlytics());
		Vehiculum.context = getApplicationContext();
	}

}