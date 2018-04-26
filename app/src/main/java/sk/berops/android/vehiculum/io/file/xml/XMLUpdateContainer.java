package sk.berops.android.vehiculum.io.file.xml;

import java.util.HashMap;
import java.util.Map;

import sk.berops.android.vehiculum.io.file.RuleContainer;

/**
 * @author Bernard Halas
 * @date 11/2/17
 */

public class XMLUpdateContainer implements RuleContainer {
	public enum XMLUpdateRule {
		MULTI_CURRENCY_COST(0, "<cost>(\\d+.?\\d*)</cost>", "regex replacement", 8, 9);
		private static Map<Integer, XMLUpdateRule> idToRuleMapping;
		private int id;
		private int versionFrom;
		private int versionTo;
		private String pattern;
		private String replacement;

		XMLUpdateRule(int id, String pattern, String replacement, int versionFrom, int versionTo) {
			this.id = id;
			this.versionFrom = versionFrom;
			this.versionTo = versionTo;
			this.pattern = pattern;
			this.replacement = replacement;
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
	public void applyRules(String oldPathName, String newPathName) {

	}
}
