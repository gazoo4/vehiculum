package sk.berops.android.vehiculum.engine.currency;

import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import sk.berops.android.vehiculum.dataModel.Currency;
import sk.berops.android.vehiculum.engine.currency.fixerIo.Api;
import sk.berops.android.vehiculum.engine.currency.fixerIo.DataPackage;
import sk.berops.android.vehiculum.engine.currency.fixerIo.JsonConverter;

import static sk.berops.android.vehiculum.io.internet.Connectivity.isOnline;

/**
 * @author Bernard Halas
 * @date 5/17/17
 */

/**
 * Thread-safe class for creating the REST client polling object for the currency conversion tasks
 */
public class Poller {

	private static final String LOG_TAG = "CurrencyPoller";
	private static Poller instance = null;
	private static String FIXER_IO_BASE_URL = "https://api.fixer.io/";
	private static Object mutex = new Object();

	/**
	 * 2-level cache HashMap: date & currency:
	 * cache.get(date).get(currency)
	 */
	private static HashMap<String, HashMap<String, Double>> cache;

	public static Poller getInstance() {
		if (instance == null) {
			// The only synchronized part is the instance creation; no need to synchronize the whole getInstance method
			synchronized(mutex) {
				if (instance == null) {
					instance = new Poller();
				}
			}
		}
		return instance;
	}

	private Api fixerService;

	private Poller() {
		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
		OkHttpClient client = new OkHttpClient.Builder()
				.addInterceptor(interceptor)
				.connectTimeout(5, TimeUnit.MINUTES)
				.writeTimeout(5, TimeUnit.MINUTES)
				.readTimeout(5, TimeUnit.MINUTES)
				.build();

		Retrofit restAdapter = new Retrofit.Builder()
				.baseUrl(FIXER_IO_BASE_URL)
				.addConverterFactory(JsonConverter.buildConverter())
				.client(client)
				.build();

		fixerService = restAdapter.create(Api.class);
	}

	/**
	 * Method for Currency conversion.
	 * @param request Containing Date for the conversion and From value/currency pair and To currency
	 * @return
	 */
	public synchronized double convert(ConvertingService.Request request) {

		if (!isOnline()) {
			// TODO
		}

		String dateIso = new SimpleDateFormat("YYYY-MM-DD").format(request.getDate());
		Currency.Unit currencyFrom = request.getFromSymbol();
		Currency.Unit currencyTo = request.getToSymbol();
		double value = request.getFromValue();

		/*
		How conversion works now:
		EUR  -> EUR   value = value
		OTH  -> EUR   value = value/rate
		EUR  -> OTH   value = value*rate
		OTH1 -> OTH2  value = value*rate2/rate1
		 */
		if (currencyFrom != Currency.Unit.EUR) {
			try {
				double rate = callCache(dateIso, currencyFrom.getUnitIsoCode());
				value /= rate;
			} catch (IOException ex) {
				// TODO
			}
		}

		if (currencyTo != Currency.Unit.EUR) {
			try {
				double rate = callCache(dateIso, currencyTo.getUnitIsoCode());
				value *= rate;
			} catch (IOException ex) {
				// TODO
			}
		}

		return value;
	}

	/**
	 * Method to look into the cache first whether the rate has already been cached. If not, query for the rate and cache it.
	 * @param dateIso
	 * @param currencyIso
	 * @return conversion rate (double)
	 * @throws IOException
	 */
	private double callCache(String dateIso, String currencyIso) throws IOException {

		if (cache == null) {
			cache = new HashMap<>();
		}

		if (cache.get(dateIso) == null) {
			cache.put(dateIso, new HashMap<String, Double>());
		}

		if (cache.get(dateIso).get(currencyIso) == null) {
			double rate = executeCall(dateIso, currencyIso);
			cache.get(dateIso).put(currencyIso, rate);
		}

		return (cache.get(dateIso).get(currencyIso));
	}

	private double executeCall(String dateIso, String currencyIso) throws IOException {
		Call<DataPackage> call = fixerService.getEurExchangeRate(dateIso, currencyIso);
		try {
			return call.execute().body().getRates().get(currencyIso);
		} catch (IOException ex) {
			Log.w(LOG_TAG, "Call failed: "+ call.toString());
			throw ex;
		}
	}
}
