package gui.java.controller;

import database.Database;
import gui.java.service.NodePrototype.NodePrototype;
import gui.java.ui.WindowFactory;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;


public class MirrorWindowController {

	@FXML
	public VBox mainPanel;
	
	private NodePrototype mainPanelPrototype;
	
	public MirrorWindowController(NodePrototype mainPanelPrototype) {
		this.mainPanelPrototype = mainPanelPrototype;
	}
	
	public void initialize() {
		mainPanel.getChildren().add(mainPanelPrototype.clone());
	}
	
	@FXML
	private void synchronize() {
		Database.synchronize();
	}
	
	@FXML
	private void detach() {
		WindowFactory.createMirrorWindow(mainPanelPrototype);
	}
	
}
