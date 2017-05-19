package sk.berops.android.vehiculum.engine.currency;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sk.berops.android.vehiculum.dataModel.Currency;
import sk.berops.android.vehiculum.dataModel.expense.Expense;
import sk.berops.android.vehiculum.engine.currency.fixerIo.DataPackage;

/**
 * @author Bernard Halas
 * @date 5/17/17
 */

public class ConvertingService extends IntentService implements Callback<DataPackage> {

	public class ConversionRequest {
		private double fromValue;
		private Currency.Unit fromSymbol;
		private Currency.Unit toSymbol;
		private Date date;

		public double getFromValue() {
			return fromValue;
		}

		public void setFromValue(double fromValue) {
			this.fromValue = fromValue;
		}

		public Currency.Unit getFromSymbol() {
			return fromSymbol;
		}

		public void setFromSymbol(Currency.Unit fromSymbol) {
			this.fromSymbol = fromSymbol;
		}

		public Currency.Unit getToSymbol() {
			return toSymbol;
		}

		public void setToSymbol(Currency.Unit toSymbol) {
			this.toSymbol = toSymbol;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}
	}

	private String LOG_TAG = "ConvertingService";

	private ConversionRequest nextRequest;
	private Poller poller;

	/**
	 * Default constructor
	 */
	public ConvertingService() {
		super("ConvertingService");
		poller = new Poller(this);
	}

	/**
	 * The IntentService calls this method from the default worker thread with
	 * the intent that started the service. When this method returns, IntentService
	 * stops the service, as appropriate.
	 */
	@Override
	protected void onHandleIntent(@Nullable Intent intent) {
		if (nextRequest == null) {
			Log.w(LOG_TAG, "Conversion request not specified");
			return;
		}
		poller.execute(nextRequest);
		nextRequest = null;
	}

	@Override
	public void onResponse(Call<DataPackage> call, Response<DataPackage> response) {

	}

	@Override
	public void onFailure(Call<DataPackage> call, Throwable t) {

	}
}
