package sk.berops.android.vehiculum.io.file.xml;

import java.util.HashMap;
import java.util.Map;

import sk.berops.android.vehiculum.io.file.RuleContainer;

/**
 * @author Bernard Halas
 * @date 11/2/17
 */

public class XMLUpdateContainer implements RuleContainer {
	public static final int VERSION_FROM = 8;
	public static final int VERSION_TO = 9;

	public enum XMLUpdateRule {
		MULTI_CURRENCY_COST(regex match, regex replacement)
		private static Map<Integer, XMLUpdateRule> idToRuleMapping;
		private int id;

		XMLUpdateRule(int id, String rule) {
			this.id = id;
		}

		public static XMLUpdateRule getExpenseType(int id) {
			if (idToRuleMapping == null) {
				initMapping();
			}

			XMLUpdateRule result = null;
			result = idToRuleMapping.get(id);
			return result;
		}

		private static void initMapping() {
			idToRuleMapping = new HashMap<>();
			for (XMLUpdateRule rule : values()) {
				idToRuleMapping.put(rule.id, rule);
			}
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
	}

	@Override
	public int getVersionFrom() {
		return VERSION_FROM;
	}

	@Override
	public int getVersionTo() {
		return VERSION_TO;
	}

	@Override
	public void applyRules(String oldPathName, String newPathName) {

	}
}
