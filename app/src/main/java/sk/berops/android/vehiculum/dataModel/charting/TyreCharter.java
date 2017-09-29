package sk.berops.android.vehiculum.dataModel.charting;

import android.graphics.Color;

import com.github.mikephil.charting.data.PieEntry;

import java.util.function.Consumer;
import java.util.stream.Stream;

import sk.berops.android.vehiculum.dataModel.maintenance.Tyre;
import sk.berops.android.vehiculum.engine.calculation.NewGenTyreConsumption;

/**
 * @author Bernard Halas
 * @date 9/21/17
 */

public class TyreCharter extends EntryCharter {

	private NewGenTyreConsumption tConsumption;

	public TyreCharter(NewGenTyreConsumption c) {
		tConsumption = c;
	}

	@Override
	public void refreshData() {
		super.refreshData();

		if (! tConsumption.getLaborCost().isZero()) {
			colors.add(Color.LTGRAY);
			vals.add(new PieEntry(tConsumption.getLaborCost().getPreferredValue().floatValue()));
			relays.add(null);
		}

		if (! tConsumption.getMaterialCost().isZero()) {
			colors.add(Color.GRAY);
			vals.add(new PieEntry(tConsumption.getMaterialCost().getPreferredValue().floatValue()));
			relays.add(null);
		}
	}

	protected Stream<Tyre.Season> getStream() {
		return tConsumption.getTyreCost().keySet().stream();
	}

	protected Consumer<Tyre.Season> addColor() {
		return type -> colors.add(type.getColor());
	}

	protected Consumer<Tyre.Season> addVals() {
		return type -> vals.add(new PieEntry(tConsumption.getTyreCost().get(type).getPreferredValue().floatValue()));
	}

	protected Consumer addRelay() {
		// There are no child relays to the FuellingCharter
		return type -> {};
	}
}
