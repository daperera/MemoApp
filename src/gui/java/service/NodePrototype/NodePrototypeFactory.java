package gui.java.service.NodePrototype;

import java.net.MalformedURLException;

import processing.Config;

public class NodePrototypeFactory {
	
	
	public static NodePrototype createMainPanelPrototype() {
		// initialize a MemoPanel loading the default html file
		String mainWindowMemoPanelHtmlPath = Config.getDefaultMemoPanelHtmlPath();
		NodePrototype mainPanel = createMemoPanelPrototype(mainWindowMemoPanelHtmlPath);
		return mainPanel;
	}
	
	public static NodePrototype createMemoPanelPrototype(String htmlPath) {
		try {
			return new MemoPanelPrototype(htmlPath);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
