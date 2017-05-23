package sk.berops.android.vehiculum;

import android.app.Application;
import android.content.Context;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import io.fabric.sdk.android.Fabric;

public class Vehiculum extends Application {

	public static Context context;

	public void onCreate() {
		super.onCreate();

		// Initializing Fabric with the debug-disabled crashlytics.
		Crashlytics crashlyticsKit = new Crashlytics.Builder()
				.core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
				.build();
		Fabric.with(this, crashlyticsKit, new Crashlytics());

		Vehiculum.context = getApplicationContext();
	}

}