package gui.java.controller;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;

import database.Database;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;

public class EditMemoTypeController {

	@FXML
	ListView<String> columnList;
	
	private String tableName;
	
	public EditMemoTypeController(String tableName) {
		this.tableName = tableName;
	}
	
	@FXML
	private void initialize() {
		ArrayList<String> columnsNames = Database.getColumnsNames(tableName);
		System.out.println(columnsNames);
		columnList.setItems(FXCollections.observableArrayList(columnsNames));
		columnList.getSelectionModel().select(0);
	}
	
	@FXML
	private void add() {
		//prompt dialog asking for a string, then create a column which name is this string
		createEnterNameDialog(columnName -> {
			//adding this column in the database
			Database.addColumn(tableName, columnName);
			
			//refreshing the listView
			initialize();
		});
	}


	@FXML
	private void rename() {
		//prompt dialog asking for a string, then rename the currently selected column to this string
		createEnterNameDialog(newColumnName -> {
			//loading the selected column name
			String oldColumnName = columnList.getSelectionModel().getSelectedItem();
			
			//renaming the column
			Database.renameColumn(tableName, oldColumnName, newColumnName);
			
			//refreshing the listView
			initialize();
		});
	}


	@FXML
	private void delete() {
		//prompt a confirmation dialog, then delete the currently selected column
		createDeleteColumnDialog(() -> {
			//loading the selected column name
			String column = columnList.getSelectionModel().getSelectedItem();
			
			//deleting this column in the database
			Database.deleteColumn(tableName, column);
			
			//refreshing the listView
			initialize();
		});
	}
	
	//prompt a dialog asking the users to enter a string
	private void createEnterNameDialog(Consumer<String> action) {
		//creating the dialog window
		TextInputDialog dialog = new TextInputDialog();

		//setting text
		dialog.setTitle("Name Selection Window");
		dialog.setHeaderText("Please enter the field name");
		dialog.setContentText("");

		//setting effect and prompting the dialog
		Optional<String> result = dialog.showAndWait();
		result.ifPresent(action);
	}

	//prompt a dialog asking the users whether or not he wants to delete the currently selected item in the database
	private void createDeleteColumnDialog(Runnable action) {

		//creating the alert window
		Alert alert = new Alert(AlertType.WARNING);

		//setting text
		alert.setTitle("Warning");
		alert.setHeaderText("You are trying to delete a table in the database. This action cannot be undone.");
		alert.setContentText("Do you want to continue ?");

		//setting effects of the OK button and prompting the alert
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){ // if OK button is pressed
			action.run();
		} else {
			// do nothing
		}

	}
	
}
