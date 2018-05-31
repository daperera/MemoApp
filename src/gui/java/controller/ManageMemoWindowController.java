package gui.java.controller;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;

import database.Database;
import gui.java.ui.WindowFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
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
	
	@FXML
	ListView<String> memoTypeList;
	
	@FXML
	ListView<String> memoViewList;
	
	
	public ManageMemoWindowController() {
		
	}
	
	@FXML 
	private void initialize() {
		initializePaneSelector();
		initializeMemoViewPane();
		initializeMemoTypePane();
		
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
	private void addMemoType() {
		createEnterNameDialog(name -> addMemoType(name));
	}


	@FXML
	private void renameMemoType() {
		//create delete selected memo type confirmation dialog
		createEnterNameDialog(name -> renameSelectedMemoType(name));
	}


	@FXML
	private void deleteMemoType() {
		//create delete selected memo type confirmation dialog
		createDeleteConfirmationDialog(() -> deleteSelectedMemoType());
	}

	@FXML
	private void  editMemoType() {
		WindowFactory.createEditMemoTypeWindow(memoTypeList.getSelectionModel().getSelectedItem());
	}
	
	private void initializePaneSelector() {
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
	
	private void initializeMemoTypePane() {
		ArrayList<String> tablesNames = Database.getTablesNames();
		memoTypeList.setItems(FXCollections.observableArrayList(tablesNames));
		memoTypeList.getSelectionModel().select(0);
	}
	
	private void initializeMemoViewPane() {
		
	}
	
	//prompt a dialog asking the users to enter a string
	private void createEnterNameDialog(Consumer<String> action) {
		//creating the dialog window
		TextInputDialog dialog = new TextInputDialog();

		//setting text
		dialog.setTitle("Name Selection Window");
		dialog.setHeaderText("Please enter a name");
		dialog.setContentText("");

		//setting effect and prompting the dialog
		Optional<String> result = dialog.showAndWait();
		result.ifPresent(action);
	}
	
	//prompt a dialog asking the users whether or not he wants to delete the currently selected element in the database
	private void createDeleteConfirmationDialog(Runnable action) {
		
		//creating the alert window
		Alert alert = new Alert(AlertType.WARNING);
		
		//setting text
		alert.setTitle("Warning");
		alert.setHeaderText("You are trying to delete an element in the database. This action cannot be undone.");
		alert.setContentText("Do you want to continue ?");
		
		//setting effects of the OK button and prompting the alert
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){ // if OK button is pressed
			action.run();
		} else {
		    // do nothing
		}
		
	}
	
	private void addMemoType(String name) {
		//adding this table in the database
		Database.addTable(name);
		
		//refreshing the listView
		initializeMemoTypePane();
	}
	
	private void deleteSelectedMemoType() {
		//loading the selected table name
		String tableName = memoTypeList.getSelectionModel().getSelectedItem();
		
		//deleting this table in the database
		Database.deleteTable(tableName);
		
		//refreshing the listView
		initializeMemoTypePane();
	}
	
	private void renameSelectedMemoType(String newTableName) {
		//loading the selected table name
		String oldTableName = memoTypeList.getSelectionModel().getSelectedItem();
		
		//renaming the table
		Database.renameTable(oldTableName, newTableName);
		
		//refreshing the listView
		initializeMemoTypePane();
	}
	

	
}
