package sk.berops.android.vehiculum.dataModel.charting;

import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

/**
 * @author Bernard Halas
 * @date 8/24/17
 *
 * This abstract class ensures that we're able to extract the data values and the legend information from the entry for a use in charts
 */

public abstract class Charter implements PieChartable {

	private Charter relay;
	protected ArrayList<PieEntry> vals;
	protected ArrayList<Integer> colors;
	protected String label;

	/**
	 * Method called when data objects {@link Charter#vals} and {@link Charter#colors} are empty and need to be populated with data for extraction
	 */
	public void refreshData() {
		vals = new ArrayList<>();
		colors = new ArrayList<>();
	}

	/**
	 * Method to get the values for the consumption pie chart (either to calculate them or to relay the calculation to another Charter)
	 * @return DataSet of the values
	 */
	public ArrayList<PieEntry> getPieChartVals() {
		if (relay != null) {
			return relay.getPieChartVals();
		}

		if (vals == null || vals.size() == 0) {
			refreshData();
		}
		return vals;
	}

	/**
	 * Method to get the colors for the consumption pie chart (either to extract them or to relay the extraction to another Charter)
	 * @return list of the colors
	 */
	public ArrayList<Integer> getPieChartColors() {
		if (relay != null) {
			return relay.getPieChartColors();
		}

		if (colors == null || colors.size() == 0) {
			refreshData();
		}
		return colors;
	}

	/**
	 * Method to get the label for the consumption pie chart (either to extract it or to relay the extraction to another Charter)
	 * @return label
	 */
	public String getPieChartLabel() {
		if (relay != null) {
			return relay.getPieChartLabel();
		}

		if (label == null) {
			refreshData();
		}
		return label;
	}

	/**
	 * Method used to tunnel information extraction from a relay instead of this object
	 * @param target
	 */
	public void changeFocus(Charter target) {
		this.relay = target;
	}
}
