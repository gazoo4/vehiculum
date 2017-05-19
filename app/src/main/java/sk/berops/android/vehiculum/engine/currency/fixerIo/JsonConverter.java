package sk.berops.android.vehiculum.engine.currency.fixerIo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Bernard Halas
 * @date 5/18/17
 */

public class JsonConverter {
	public static GsonConverterFactory buildConverter() {
		GsonBuilder gsonBuilder = new GsonBuilder();

		// Adding custom deserializers
		gsonBuilder.registerTypeAdapter(DataPackage.class, new ResponseDeserializer());
		Gson myGson = gsonBuilder.create();

		return GsonConverterFactory.create(myGson);
	}
}
