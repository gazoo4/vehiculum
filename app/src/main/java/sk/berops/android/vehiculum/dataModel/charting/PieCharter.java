package sk.berops.android.vehiculum.dataModel.charting;

import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;

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
	 * @return true if operation was processed successfully
	 */
	public boolean invokeSelection(Integer selection) {
		if (relay != null) {
			boolean result = relay.invokeSelection(selection);
			if (!result && selection == null) {
				// Request to remove a leaf (i.e. to move the chart focus one level higher)
				// Relay was asked to process the request, but it failed and the request was to shorten
				// the relay path by 1 level (selection == null), i.e. to display the data of
				// an element one level higher, it means we are becoming the leaf in the chain
				relay = null;
				return true;
			} else {
				// The relay either processed the result correctly or the request wasn't about shortening
				// the relay path. So we're just passing the result of the operation up the chain
				return result;
			}
		} else {
			// relay == null
			if (selection == null) {
				// We are asked to cut the relay path by one level, however we're are the leaf
				// so we can't process the request.
				return false;
			} else {
				// There's no relay and we've been asked to set one
				return setRelayByID(selection);
			}
		}
	}

	/**
	 * Method to assign the relay object based on the selection ID
	 * This needs to be overriden in the child classes as for each child pieCharter the ID will represent
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
