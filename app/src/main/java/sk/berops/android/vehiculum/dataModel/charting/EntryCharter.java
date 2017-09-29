package sk.berops.android.vehiculum.dataModel.charting;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author Bernard Halas
 * @date 9/27/17
 */

public abstract class EntryCharter extends PieCharter {

	@Override
	public void refreshData() {
		super.refreshData();

		Stream s = getStream();
		if (s != null) {
			s.forEach(addColor()
					.andThen(addVals())
					.andThen(addRelay()));
		}
	}

	protected abstract Stream getStream();

	protected abstract Consumer addColor();

	protected abstract Consumer addVals();

	protected abstract Consumer addRelay();
}
