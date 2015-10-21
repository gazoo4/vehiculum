package sk.berops.android.fueller.gui.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import java.util.List;

import sk.berops.android.fueller.R;

public class PreferenceWithHeaders extends PreferenceActivity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Populate the activity with the top-level headers.
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }

    /**
     * This fragment shows the preferences for the first header.
     */
    public static class Units extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Make sure default values are applied.  In a real app, you would
            // want this in a shared function that is used to retrieve the
            // SharedPreferences wherever they are needed.
            //PreferenceManager.setDefaultValues(getActivity(), R.xml.advanced_preferences, false);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences_units);
        }
    }

    /**
     * This fragment contains a second-level set of preference that you
     * can get to by tapping an item in the first preferences fragment.
     *//*
    public static class Prefs1FragmentInner extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Can retrieve arguments from preference XML.
            Log.i("args", "Arguments: " + getArguments());

            // Load the preferences from an XML resource
            //addPreferencesFromResource(R.xml.fragmented_preferences_inner);
        }
    }

    /**
     * This fragment shows the preferences for the second header.
     *//*
    public static class Prefs2Fragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Can retrieve arguments from headers XML.
            Log.i("args", "Arguments: " + getArguments());

            // Load the preferences from an XML resource
            //addPreferencesFromResource(R.xml.preference_dependencies);
        }
    }	*/
    
    /**
     * Android API 19+ security flaw fix. Every fragment should be listed. 
     */
    @Override
    protected boolean isValidFragment(String fragmentName) {
        return Units.class.getName().equals(fragmentName);
    }
}
