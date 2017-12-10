package processing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public abstract class Config {
	
	private static final String CONFIG_FILE_LOCATION = "data/default.config";
	private static final String DEFAULT_MEMOPANEL_HTML_LOCATION = "file:C:/Users/daper/workspace/MemoApp/external_resources/test.html";
	private static final String DEFAULT_USER_DIRECTORY_LOCATION = "file:C:/Users/daper/workspace/MemoApp/external_resources";
	
	public Config() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(CONFIG_FILE_LOCATION));
			load(br);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected abstract void load(BufferedReader br) throws IOException;

	public static String getDefaultMemoPanelHtmlPath() {
		//TODO: DEFAULT IMPLEMENTATION
		return DEFAULT_MEMOPANEL_HTML_LOCATION;
	}
	
	public static String getUserDirectoryLocation() {
		return DEFAULT_USER_DIRECTORY_LOCATION;
	}
	
}