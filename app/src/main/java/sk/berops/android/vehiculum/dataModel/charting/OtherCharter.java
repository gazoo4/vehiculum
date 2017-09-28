package sk.berops.android.vehiculum.dataModel.charting;

import java.util.function.Consumer;
import java.util.stream.Stream;

import sk.berops.android.vehiculum.dataModel.expense.OtherEntry;
import sk.berops.android.vehiculum.engine.calculation.NewGenOtherConsumption;

/**
 * @author Bernard Halas
 * @date 9/21/17
 */

public class OtherCharter extends EntryCharter {

	private NewGenOtherConsumption oConsumption;

	public OtherCharter(NewGenOtherConsumption o) {
		oConsumption = o;
	}

	protected Stream getStream() {
		return null;
	}

	protected Consumer addColor() {
		return null;
	}

	protected Consumer addVals() {
		return null;
	}

	protected Consumer addRelay() {
		return null;
	}
}
