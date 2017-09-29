package sk.berops.android.vehiculum.engine.currency;

import java.util.Date;

import sk.berops.android.vehiculum.dataModel.Currency;

/**
 * @author Bernard Halas
 * @date 5/17/17
 */

public class ConvertingService implements Runnable {

	public static class Request {
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
	private ConversionCallback callback;
	private Request request;

	public ConvertingService(ConversionCallback callback, Request request) {
		super();
		this.callback = callback;
		this.request = request;
	}

	@Override
	public void run() {

		Poller poller = Poller.getInstance();
		double result = poller.convert(request);
		callback.rateDelivery(result);
	}
}
