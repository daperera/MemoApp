package gui.java.controller;

import database.Database;
import gui.java.service.NodePrototype.NodePrototype;
import gui.java.ui.WindowFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class MainWindowController {
	
	@FXML
	private VBox mainPanel;
	
	private NodePrototype mainPanelPrototype;
	
	public MainWindowController(NodePrototype mainPanelPrototype) {
		this.mainPanelPrototype = mainPanelPrototype;	
	}
	
	@FXML 
	private void initialize() {
		mainPanel.getChildren().add(mainPanelPrototype.clone());
	}
	
	@FXML
	private void switchUser(ActionEvent e) {
		
	}
	
	@FXML
	private void exit(ActionEvent e) {
		
	}
	
	@FXML
	private void manageMemoType(ActionEvent e) {
		
	}
	
	@FXML
	private void preferences(ActionEvent e) {
		
	}
	
	@FXML
	private void guide(ActionEvent e) {
		
	}
	
	@FXML
	private void about(ActionEvent e) {

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
