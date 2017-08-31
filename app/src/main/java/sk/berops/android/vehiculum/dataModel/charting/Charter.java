package sk.berops.android.vehiculum.dataModel.charting;

import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

/**
 * @author Bernard Halas
 * @date 8/24/17
 * This abstract class ensures that we're able to extract the data values and the legend information from the entry for a use in charts
 */

public abstract class Charter {

	Charter relay;

	/**
	 * Method to get the values for the consumption pie chart (either to calculate them or to relay the calculation to another Charter)
	 * @return DataSet of the values
	 */
	public ArrayList<PieEntry> getPieChartVals() {
		return (relay == null) ? generatePieChartVals() : relay.getPieChartVals();
	}

	/**
	 * Method to get the values for the consumption pie chart
	 * @return DataSet of the values
	 */
	public abstract ArrayList<PieEntry> generatePieChartVals();

	/**
	 * Method to get the colors for the consumption pie chart (either to extract them or to relay the extraction to another Charter)
	 * @return list of the colors
	 */
	public ArrayList<Integer> getPieChartColors() {
		return (relay == null) ? generatePieChartColors() : relay.getPieChartColors();
	}

	/**
	 * Method to get the colors for the consumption pie chart
	 * @return list of the colors
	 */
	public abstract ArrayList<Integer> generatePieChartColors();

	/**
	 * Method to get the label for the consumption pie chart (either to extract it or to relay the extraction to another Charter)
	 * @return label
	 */
	public String getPieChartLabel() {
		return (relay == null) ? generatePieChartLabel() : relay.getPieChartLabel();
	}

	/**
	 * Method to get the label for the consumption pie chart
	 * @return label
	 */
	public abstract String generatePieChartLabel();

	/**
	 * Method used to tunnel information extraction from a relay instead of this object
	 * @param target
	 */
	public void changeFocus(Charter target) {
		this.relay = target;
	}
}
