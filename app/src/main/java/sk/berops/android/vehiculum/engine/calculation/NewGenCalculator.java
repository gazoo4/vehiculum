package sk.berops.android.vehiculum.engine.calculation;

import sk.berops.android.vehiculum.dataModel.expense.Entry;

/**
 * @author Bernard Halas
 * @date 7/25/17
 */

public abstract class NewGenCalculator {
	protected Entry initial;
	protected Entry previous;

	public abstract void calculateNext(Entry entry);
}
