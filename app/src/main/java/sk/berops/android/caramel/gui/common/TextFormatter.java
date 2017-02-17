package sk.berops.android.caramel.gui.common;

import java.text.DecimalFormat;

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
