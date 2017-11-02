package sk.berops.android.vehiculum.io.file;

/**
 * @author Bernard Halas
 * @date 11/2/17
 */

public interface RuleContainer {
	int getVersionFrom();
	int getVersionTo();
	void applyRules(String oldPathName, String newPathName);
}
