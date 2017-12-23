package gui.java.service.NodePrototype;

import java.net.MalformedURLException;
import java.net.URL;

import gui.java.service.DatabaseProxy;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.scene.Node;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

class MemoPanelPrototype implements NodePrototype {

	
	private final static String JAVASCRIPT_PROXY_NAME = "database"; 
	private final static String JAVASCRIPT_READY_FUNCTION_NAME = "ready";
	private final String EngineLoadArg;
	
	MemoPanelPrototype(String htmlFilePath) throws MalformedURLException {
		URL url = new URL(htmlFilePath);
		EngineLoadArg = url.toExternalForm();
	}
	
	
	@Override
	public Node clone() {
		//creating an embeded web browser
	    WebView browser = new WebView();
	    WebEngine webEngine = browser.getEngine();

	    // injecting a DatabaseProxy object in the browser, once the html page is loaded.
	    // Javascript functions of the loaded html page can make upcall to this object 
	    // through the variable whose name is referenced by JAVASCRIPT_PROXY_NAME.
	    // Once the script are loaded, the function 
	    webEngine.getLoadWorker().stateProperty().addListener(
	    		(ObservableValue<? extends State> ov, State oldState, 
	    				State newState) -> {
	    					if (newState == State.SUCCEEDED) {
	    						JSObject window = (JSObject) webEngine.executeScript("window");
	    						window.setMember(JAVASCRIPT_PROXY_NAME, new DatabaseProxy());
	    						window.call(JAVASCRIPT_READY_FUNCTION_NAME);
	    					}
	    				});
	    //loading the html page
	    webEngine.load(EngineLoadArg);
	    
	    //set the html page size
	    browser.setPrefSize(800, 800);
	    
	    //returning browser as a Node
	    return browser;
	}
	
}
