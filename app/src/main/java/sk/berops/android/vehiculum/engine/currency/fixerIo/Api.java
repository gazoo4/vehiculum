package sk.berops.android.vehiculum.engine.currency.fixerIo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Bernard Halas
 * @date 5/18/17
 */

public interface Api {

	/**
	 * Method defined for getting the exchange rates from the server, always against EUR base
	 * @param dateIso date in format YYYY-MM-DD
	 * @param currencyIso currency in ISO4217
	 * @return
	 */
	@GET("{date}")
	Call<DataPackage> getEurExchangeRate(@Path("date") String dateIso, @Query("symbols") String currencyIso);
}
