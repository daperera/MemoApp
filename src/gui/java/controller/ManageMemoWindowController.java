package gui.java.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

public class ManageMemoWindowController {
	
	@FXML
	ListView<String> paneSelector;
	
	@FXML
	String memoTypeSelectorLabel;
	
	@FXML
	String memoViewSelectorLabel;
	
	@FXML
	Pane memoTypePane;
	
	@FXML
	Pane memoViewPane;
	
	
	
	public ManageMemoWindowController() {
		
	}
	
	@FXML 
	private void initialize() {
		//add a listener to the paneSelector
		paneSelector.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				//if the memoType button is selected
				if(newValue.equals(memoTypeSelectorLabel)) {
					memoTypePane.toFront();
				}
				//if the memoView button is selected
				else if (newValue.equals(memoViewSelectorLabel)){
					memoViewPane.toFront();
				}
			}
			
		});
		
		//select the first item of the selector
		paneSelector.getSelectionModel().select(0);
		
	}
	
	@FXML
	private void addMemoView() {
		
	}

	@FXML
	private void renameMemoView() {
		
	}

	@FXML
	private void deleteMemoView() {
		
	}

	@FXML
	private void  editMemoView() {
		
	}

	@FXML
	private void memoViewPaneHelp() {
		
	}

	@FXML
	private void addMemoType() {
		
	}

	@FXML
	private void renameMemoType() {
		
	}

	@FXML
	private void deleteMemoType() {
		
	}

	@FXML
	private void  editMemoType() {
		
	}

	@FXML
	private void memoTypePaneHelp() {
		
	}
	
}
