package processing;

import java.util.prefs.Preferences;

public class Config {
	
	private static final Preferences preferences = Preferences.userRoot();

	private static final String DEFAULT_MEMOPANEL_HTML_LOCATION = "file:C:/Users/dapinator/workspace/MemoApp/external_resources/test.html";
	private static final String DEFAULT_USER_DIRECTORY_LOCATION = "file:C:/Users/dapinator/workspace/MemoApp/external_resources"; 
	
	public static String getDefaultMemoPanelHtmlPath() {
		return DEFAULT_MEMOPANEL_HTML_LOCATION;
	}
	
	public static String getUserDirectoryLocation() {
		return preferences.get("userDirectoryLocation", DEFAULT_USER_DIRECTORY_LOCATION);
	}
	
	public static void setUserDirectoryLocation(String path) {
		preferences.put("userDirectoryLocation", path);
	}
	
}