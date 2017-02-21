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

		Crashlytics crashlyticsKit = new Crashlytics.Builder()
				.core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
				.build();
		// Initialize Fabric with the debug-disabled crashlytics.
		Fabric.with(this, crashlyticsKit);

		Vehiculum.context = getApplicationContext();
	}

}