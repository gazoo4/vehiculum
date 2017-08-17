package sk.berops.android.vehiculum.gui.common;

import java.text.DecimalFormat;

import sk.berops.android.vehiculum.configuration.Preferences;
import sk.berops.android.vehiculum.dataModel.Currency;
import sk.berops.android.vehiculum.dataModel.expense.Cost;

public class TextFormatter {

	/**
	 * Automatic price/cost formatter
	 * @param value the value you'd like to format
	 * @param formattingRule usual formatting rules. if null, or empty string, the default value applies: "###.##"
	 * @return formatted string
	 */
	public static String format(double value, String formattingRule) {
		String result;
		if ((formattingRule == null) || (formattingRule.contentEquals(""))) {
			formattingRule = "###.##";
		}
		DecimalFormat df = new DecimalFormat(formattingRule);
		result = df.format(value);
		return result;
	}
}
