package sk.berops.android.vehiculum.engine.currency.fixerIo;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * @author Bernard Halas
 * @date 5/18/17
 */

public class ResponseDeserializer implements JsonDeserializer<DataPackage> {
	@Override
	public DataPackage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		Gson gson = new Gson();

		return gson.fromJson(json, DataPackage.class);
	}
}
