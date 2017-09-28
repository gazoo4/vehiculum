package sk.berops.android.vehiculum.dataModel.charting;

import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

/**
 * @author Bernard Halas
 * @date 8/24/17
 *
 * This abstract class ensures that we're able to extract the data values and the legend information from the entry for a use in charts
 */

public abstract class PieCharter {

	/**
	 * If the user wants to zoom in into the PieChart, we relay the data collection to another PieChartable object
	 * stored in relay property.
	 */
	private PieCharter relay;
	protected ArrayList<PieEntry> vals;
	protected ArrayList<Integer> colors;
	protected ArrayList<PieCharter> relays;
	protected String label;

	/**
	 * Method called when data objects {@link PieCharter#vals} and {@link PieCharter#colors} are empty and need to be populated with data for extraction
	 */
	public void refreshData() {
		vals = new ArrayList<>();
		colors = new ArrayList<>();
		relays = new ArrayList<>();
	}

	/**
	 * Method to get the values for the consumption pie chart (either to calculate them or to relay the calculation to another PieCharter)
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
	 * Method to get the colors for the consumption pie chart (either to extract them or to relay the extraction to another PieCharter)
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
	 * Method to get the label for the consumption pie chart (either to extract it or to relay the extraction to another PieCharter)
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
	 * In case of a zoom action in the chart, set the relay object and pull data from relay.
	 * If null argument is given, go one level up
	 * @param selection
	 * @return true if operation was processed successfully, false if otherwise
	 */
	public boolean zoomIn(Integer selection) {
		// If we are relaying the charting, ask the relay (child) to zoom-in.
		// Otherwise we are the leaf and we are asked to zoom-in.
		return (relay == null) ? setRelayByID(selection) : relay.zoomIn(selection);
	}

	/**
	 * The user requested to display PieChart data from a higher perspective (so we need to zoom-out)
	 * @return true if operation was processed successfully, false if otherwise
	 */
	public boolean zoomOut() {
		if (relay == null) {
			// We can't cut the leaf, we are the leaf
			return false;
		}

		boolean result = relay.zoomOut();

		if (! result) {
			// The child didn't process the request because the child is the leaf
			// We are processing the request and cutting the leaf
			relay = null;
		}

		return true;
	}

	/**
	 * Method to assign the relay object based on the selection ID
	 * This needs to be overridden in the child classes as for each child pieCharter the ID will represent
	 * different categories (summer/winter tyres, fuel types, ...)
	 * @param selection
	 * @return true if request was processed successfully
	 */
	public boolean setRelayByID(Integer selection) {
		try {
			relay = relays.get(selection);
		} catch (IndexOutOfBoundsException | NullPointerException ex) {
			return false;
		}

		return true;
	}
}
