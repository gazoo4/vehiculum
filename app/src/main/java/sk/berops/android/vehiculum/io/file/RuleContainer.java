package sk.berops.android.vehiculum.io.file;

/**
 * @author Bernard Halas
 * @date 11/2/17
 */

public interface RuleContainer {
	void applyRules(String oldPathName, String newPathName);
}
