package sk.berops.android.vehiculum.engine.calculation;

import android.util.Log;

import sk.berops.android.vehiculum.dataModel.expense.Entry;
import sk.berops.android.vehiculum.dataModel.expense.OtherEntry;

/**
 * @author Bernard Halas
 * @date 8/16/17
 */

public class NewGenOtherCalculator extends NewGenTypeCalculator {
	@Override
	public void calculateNext(Entry entry) {
		super.calculateNext(entry);

		if (entry == null) {
			return;
		}

		if (!(entry instanceof OtherEntry)) {
			Log.w(this.getClass().toString(), "Asked to calculate fuelling consumption on non-fuelling entry");
			return;
		}

		OtherEntry bEntry = (OtherEntry) entry;

		NewGenOtherConsumption prevC = (previous == null) ? null : (NewGenOtherConsumption) previous.getConsumption();
		NewGenOtherConsumption nextC = bEntry.getOtherConsumption();
	}
}
