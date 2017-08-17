package sk.berops.android.vehiculum.engine.calculation;

import android.util.Log;

import sk.berops.android.vehiculum.dataModel.expense.BureaucraticEntry;
import sk.berops.android.vehiculum.dataModel.expense.Entry;

/**
 * @author Bernard Halas
 * @date 8/15/17
 */

public class NewGenBureaucraticCalculator extends NewGenTypeCalculator {
	@Override
	public void calculateNext(Entry entry) {
		super.calculateNext(entry);

		if (entry == null) {
			return;
		}

		if (!(entry instanceof BureaucraticEntry)) {
			Log.w(this.getClass().toString(), "Asked to calculate fuelling consumption on non-fuelling entry");
			return;
		}

		BureaucraticEntry bEntry = (BureaucraticEntry) entry;

		NewGenBureaucraticConsumption prevC = (previous == null) ? null : (NewGenBureaucraticConsumption) previous.getConsumption();
		NewGenBureaucraticConsumption nextC = bEntry.getBureaucraticConsumption();
	}
}
