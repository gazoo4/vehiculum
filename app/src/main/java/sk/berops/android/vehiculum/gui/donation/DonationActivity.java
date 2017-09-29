package sk.berops.android.vehiculum.gui.donation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import sk.berops.android.vehiculum.BuildConfig;
import sk.berops.android.vehiculum.R;
import sk.berops.android.vehiculum.engine.donation.IabBroadcastReceiver;
import sk.berops.android.vehiculum.engine.donation.IabHelper;
import sk.berops.android.vehiculum.engine.donation.IabResult;
import sk.berops.android.vehiculum.engine.donation.Inventory;
import sk.berops.android.vehiculum.engine.donation.Purchase;
import sk.berops.android.vehiculum.gui.DefaultActivity;


/**
 * @author Bernard Halas
 * @date 3/1/17
 */

public class DonationActivity extends DefaultActivity implements IabBroadcastReceiver.IabBroadcastListener, DialogInterface.OnClickListener {
	// (arbitrary) request code for the purchase flow
	static final int PURCHASE_REQUEST = 10001;

	// Does the user have an active donation period?
	boolean mDonationActive = false;

	// Will the subscription auto-renew?
	boolean mAutoRenewEnabled = false;

	// Tracks the currently owned SKU and the options in the manage dialog
	String mDonationSku = "";
	String mFirstChoiceSku = "";
	String mSecondChoiceSku = "";

	// Used to select between subscription types (if you have such)
	String mSelectedDonationPeriod = "";

	// SKU for our donation
	static final String SKU_DONATION_MONTHLY = "donation_monthly";
	static final String SKU_DONATION_YEARLY = "donation_yearly";

	// The helper object
	IabHelper mHelper;

	// Provides purchase notification while this app is running
	IabBroadcastReceiver mBroadcastReceiver;

	@Override
	protected void loadLayout() {
		setContentView(R.layout.activity_donate);
	}

	@Override
	protected void attachGuiObjects() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String key = BuildConfig.APP_API;
		String result = obfuscate(key, BuildConfig.OBFUSCATE_KEY);
		Log.d("OBFUSCATE", result);


		/* base64EncodedPublicKey should be YOUR APPLICATION'S PUBLIC KEY
         * (that you got from the Google Play developer console). This is not your
         * developer public key, it's the *app-specific* public key.
         */
		String base64EncodedPublicKey = obfuscate(BuildConfig.APP_API, BuildConfig.OBFUSCATE_KEY);
		Log.d("DEBUG", "Creating IAB helper.");
		mHelper = new IabHelper(this, base64EncodedPublicKey);
		// enable debug logging (for a production application, you should set this to false).
		mHelper.enableDebugLogging(true);

		// Start setup. This is asynchronous and the specified listener
		// will be called once setup completes.
		Log.d("DEBUG", "Starting setup.");
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				Log.d("DEBUG", "Setup finished.");

				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					Log.d("ERROR", "Problem setting up in-app billing: " + result);
					return;
				}

				// Have we been disposed of in the meantime? If so, quit.
				if (mHelper == null) return;

				// Important: Dynamically register for broadcast messages about updated purchases.
				// We register the receiver here instead of as a <receiver> in the Manifest
				// because we always call getPurchases() at startup, so therefore we can ignore
				// any broadcasts sent while the app isn't running.
				// Note: registering this listener in an Activity is a bad idea, but is done here
				// because this is a SAMPLE. Regardless, the receiver must be registered after
				// IabHelper is setup, but before first call to getPurchases().
				mBroadcastReceiver = new IabBroadcastReceiver(DonationActivity.this);
				IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
				registerReceiver(mBroadcastReceiver, broadcastFilter);

				// IAB is fully set up. Now, let's get an inventory of stuff we own.
				Log.d("DEBUG", "Setup successful. Querying inventory.");
				try {
					mHelper.queryInventoryAsync(mGotInventoryListener);
				} catch (IabHelper.IabAsyncInProgressException e) {
					Log.d("DEBUG", "Error querying inventory. Another async operation in progress.");
				}
			}
		});
	}

	/**
	 * Method used to obfuscating a string using a key (XOR operation is used)
	 * @param s
	 * @param key
	 * @return
	 */
	private String obfuscate(String s, String key) {
		byte[] data = Base64.decode(s, Base64.NO_WRAP);
		byte[] byteKey = Base64.decode(key, Base64.NO_WRAP);
		return Base64.encodeToString(xorWithKey(data, byteKey), Base64.NO_WRAP);
	}

	/**
	 * Method used for XOR operation across two strings
	 * @param a
	 * @param key
	 * @return
	 */
	private byte[] xorWithKey(byte[] a, byte[] key) {
		byte[] result = new byte[a.length];
		for (int i = 0; i < a.length; i++) {
			result[i] = (byte) (a[i] ^ key[i % key.length]);
		}

		return result;
	}

	// Listener that's called when we finish querying the items and subscriptions we own
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
			Log.d("DEBUG", "Query inventory finished.");

			// Have we been disposed of in the meantime? If so, quit.
			if (mHelper == null) return;

			// Is it a failure?
			if (result.isFailure()) {
				Log.d("DEBUG", "Failed to query inventory: " + result);
				return;
			}

			Log.d("DEBUG", "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

			// First find out which subscription is auto renewing
			Purchase donateMonthly = inventory.getPurchase(SKU_DONATION_MONTHLY);
			Purchase donateYearly = inventory.getPurchase(SKU_DONATION_YEARLY);
			if (donateMonthly != null && donateMonthly.isAutoRenewing()) {
				mDonationSku = SKU_DONATION_MONTHLY;
				mAutoRenewEnabled = true;
			} else if (donateYearly != null && donateYearly.isAutoRenewing()) {
				mDonationSku = SKU_DONATION_YEARLY;
				mAutoRenewEnabled = true;
			} else {
				mDonationSku = "";
				mAutoRenewEnabled = false;
			}

			// The user is subscribed if either subscription exists, even if neither is auto
			// renewing
			mDonationActive = (donateMonthly != null && verifyDeveloperPayload(donateMonthly))
					|| (donateYearly != null && verifyDeveloperPayload(donateYearly));
			Log.d("DEBUG", "User " + (mDonationActive ? "HAS" : "DOES NOT HAVE")
					+ " active donation.");
			// TODO: Here you can appreciate the active donation

			//updateUi();
			//setWaitScreen(false);
			Log.d("DEBUG", "Initial inventory query finished; enabling main UI.");
		}
	};

	@Override
	public void receivedBroadcast() {
		// Received a broadcast notification that the inventory of items has changed
		Log.d("DEBUG", "Received broadcast notification. Querying inventory.");
		try {
			mHelper.queryInventoryAsync(mGotInventoryListener);
		} catch (IabHelper.IabAsyncInProgressException e) {
			Log.e("ERROR","Error querying inventory. Another async operation in progress.");
		}
	}

	// "Donate" button clicked. Explain to user, then start purchase
	// flow for donation.
	public void onDonateButtonClicked(View arg0) {
		if (!mHelper.subscriptionsSupported()) {
			Log.e("ERROR", "Subscriptions not supported on your device yet. Sorry!");
			return;
		}

		CharSequence[] options;
		if (!mDonationActive || !mAutoRenewEnabled) {
			// Both subscription options should be available
			options = new CharSequence[2];
			options[0] = getString(R.string.activity_donate_period_monthly);
			options[1] = getString(R.string.activity_donate_period_yearly);
			mFirstChoiceSku = SKU_DONATION_MONTHLY;
			mSecondChoiceSku = SKU_DONATION_YEARLY;
		} else {
			// This is the subscription upgrade/downgrade path, so only one option is valid
			options = new CharSequence[1];
			if (mDonationSku.equals(SKU_DONATION_MONTHLY)) {
				// Give the option to upgrade to yearly
				options[0] = getString(R.string.activity_donate_period_yearly);
				mFirstChoiceSku = SKU_DONATION_YEARLY;
			} else {
				// Give the option to downgrade to monthly
				options[0] = getString(R.string.activity_donate_period_monthly);
				mFirstChoiceSku = SKU_DONATION_MONTHLY;
			}
			mSecondChoiceSku = "";
		}

		int titleResId;
		if (!mDonationActive) {
			titleResId = R.string.activity_donate_period_prompt;
		} else if (!mAutoRenewEnabled) {
			titleResId = R.string.activity_donate_resignup_prompt;
		} else {
			titleResId = R.string.activity_donate_update_prompt;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(titleResId)
				.setSingleChoiceItems(options, 0 /* checkedItem */, this)
				.setPositiveButton(R.string.activity_donate_prompt_continue, this)
				.setNegativeButton(R.string.activity_donate_prompt_cancel, this);
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	@Override
	public void onClick(DialogInterface dialog, int id) {
		if (id == 0 /* First choice item */) {
			mSelectedDonationPeriod = mFirstChoiceSku;
		} else if (id == 1 /* Second choice item */) {
			mSelectedDonationPeriod = mSecondChoiceSku;
		} else if (id == DialogInterface.BUTTON_POSITIVE /* continue button */) {
            /* TODO: for security, generate your payload here for verification. See the comments on
             *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
             *        an empty string, but on a production app you should carefully generate
             *        this. */
			String payload = "";

			if (TextUtils.isEmpty(mSelectedDonationPeriod)) {
				// The user has not changed from the default selection
				mSelectedDonationPeriod = mFirstChoiceSku;
			}

			List<String> oldSkus = null;
			if (!TextUtils.isEmpty(mDonationSku)
					&& !mDonationSku.equals(mSelectedDonationPeriod)) {
				// The user currently has a valid subscription, any purchase action is going to
				// replace that subscription
				oldSkus = new ArrayList<String>();
				oldSkus.add(mDonationSku);
			}

			//setWaitScreen(true);
			Log.d("DEBUG", "Launching purchase flow for donation.");
			try {
				mHelper.launchPurchaseFlow(this, mSelectedDonationPeriod, IabHelper.ITEM_TYPE_SUBS,
						oldSkus, PURCHASE_REQUEST, mPurchaseFinishedListener, payload);
			} catch (IabHelper.IabAsyncInProgressException e) {
				Log.e("ERROR", "Error launching purchase flow. Another async operation in progress.");
				//setWaitScreen(false);
			}
			// Reset the dialog options
			mSelectedDonationPeriod = "";
			mFirstChoiceSku = "";
			mSecondChoiceSku = "";
		} else if (id != DialogInterface.BUTTON_NEGATIVE) {
			// There are only four buttons, this should not happen
			Log.e("ERROR", "Unknown button clicked in donation dialog: " + id);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("DEBUG", "onActivityResult(" + requestCode + "," + resultCode + "," + data);
		if (mHelper == null) return;

		// Pass on the activity result to the helper for handling
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			// not handled, so handle it ourselves (here's where you'd
			// perform any handling of activity results not related to in-app
			// billing...
			super.onActivityResult(requestCode, resultCode, data);
		}
		else {
			Log.d("DEBUG", "onActivityResult handled by IABUtil.");
		}
	}

	/**
	 * Verifies the developer payload of a purchase
	 * @param p purchase
	 * @return true if
	 */
	boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();

		/*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

		return true;
	}

	// Callback for when a donation is finished
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			Log.d("DEBUG", "Purchase finished: " + result + ", purchase: " + purchase);

			// if we were disposed of in the meantime, quit.
			if (mHelper == null) return;

			if (result.isFailure()) {
				Log.e("ERROR", "Error purchasing: " + result);
				//setWaitScreen(false);
				return;
			}
			if (!verifyDeveloperPayload(purchase)) {
				Log.e("Error", "Error purchasing. Authenticity verification failed.");
				//setWaitScreen(false);
				return;
			}

			Log.d("DEBUG", "Purchase successful.");
			if (purchase.getSku().equals(SKU_DONATION_MONTHLY)
					|| purchase.getSku().equals(SKU_DONATION_YEARLY)) {
				// bought the infinite gas subscription
				Log.d("DEBUG", "Infinite gas subscription purchased.");
				// TODO: Throw some thankful alert here
				mDonationActive = true;
				mAutoRenewEnabled = purchase.isAutoRenewing();
				mDonationSku = purchase.getSku();
				//setWaitScreen(false);
			} else {
				Log.e("ERROR", "We purchase something unexpected (a cat in a bag??)");
			}
		}
	};

	// We're being destroyed. It's important to dispose of the helper here!
	@Override
	public void onDestroy() {
		super.onDestroy();

		// very important:
		if (mBroadcastReceiver != null) {
			unregisterReceiver(mBroadcastReceiver);
		}

		// very important:
		Log.d("DEBUG", "Destroying helper.");
		if (mHelper != null) {
			mHelper.disposeWhenFinished();
			mHelper = null;
		}
	}
}
