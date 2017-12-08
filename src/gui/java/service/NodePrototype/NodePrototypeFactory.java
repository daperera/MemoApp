package gui.java.service.NodePrototype;

import java.net.MalformedURLException;

public class NodePrototypeFactory {
	
	private final static String MAIN_WINDOW_MEMO_PANEL_HTML_PATH = "/gui/test.html";
	
	public static NodePrototype createMainPanelPrototype() {
		return createMemoPanelPrototype(MAIN_WINDOW_MEMO_PANEL_HTML_PATH);
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
