package gui.java.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import processing.Config;

public final class PreferenceWindowController {
	
	@FXML
	private BorderPane root;
	
	@FXML
	private TextField userDirectoryLocationTextField;
	
	@FXML
	private ToggleButton applyButton;
	
	// all modifications are registered to this variable and applied 
	// when the "ok" or "apply" buttons are pressed
	// modification are stored as commands
	private ArrayList<Command> unappliedModifications;
	
	private Stage preferenceWindow;
	
	public PreferenceWindowController(Stage preferenceWindow) {
		unappliedModifications = new ArrayList<Command>();
		this.preferenceWindow = preferenceWindow;
	}
	
	@FXML
	public void initialize() {
		//initializing the textField content
		userDirectoryLocationTextField.setText(Config.getUserDirectoryLocation());
		//listen to the modification of the text field, and issue an update command each time it is modified
		userDirectoryLocationTextField.textProperty().addListener((observable, oldValue, newValue) -> {
		    registerModification(() -> Config.setUserDirectoryLocation(newValue));
		});
		
		//making the applyButton look as if it was already selected (when no modifications have been done)
		applyButton.setSelected(true);
		
		//modify the default exiting process
		//when THE USER closes the stage, the exit function is called
		preferenceWindow.setOnCloseRequest(event -> {
		    exit(event);
		});
	}
	
	//function called when the "Browse" button is clicked
	@FXML
	private void browseUserDirectoryLocation() {
		//opening a dialog to allow the user to select a directory
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File directory = directoryChooser.showDialog(preferenceWindow);
		
		//updating the content of the userDirectoryLocation textField
		if(directory != null){
			String path = directory.getAbsolutePath();
			userDirectoryLocationTextField.setText(path);
        }
	}
	
	
	//function called when the "Apply" button is clicked
	@FXML
	private void apply() {
		//applying the modifications
		applyModifications();
		
		//closing the window
		preferenceWindow.close();
	}
	
	//function called when the stage is closed
	private void exit(WindowEvent event) {
		event.consume();
		// if there are some unsaved modifications, we create a confirmation dialog
		if(!unappliedModifications.isEmpty()) {
			createSaveConfirmationDialog();
		}
		//otherwise, we close the window
		else {
			preferenceWindow.close();
		}
	}
	
	//prompt a dialog asking the users wether or not he wants to save the registered modifications 
	private void createSaveConfirmationDialog() {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		
		//setting text
		alert.setTitle("Confirmation");
		alert.setHeaderText("The preferences have been modified");
		alert.setContentText("Do you want to save these modifications?");
		
		//setting buttons
		ButtonType buttonTypeYes = new ButtonType("Yes");
		ButtonType buttonTypeNo = new ButtonType("No");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo, buttonTypeCancel);
		
		Optional<ButtonType> result = alert.showAndWait();
		
		//setting button effects
		if (result.get() == buttonTypeYes){ //if the YES button is pressed
			//saving and exiting
			applyModifications();
			preferenceWindow.close();
		} 
		
		else if (result.get() == buttonTypeNo) { //if the NO button is pressed
			//we close the alert
			preferenceWindow.close();
		} 
		
		else { //if the dialog is CANCELED
			// exiting without saving
			alert.close();
			 
		}
	}
	
	// this function register a modification to the container unappliedModification
	// if there is at least one unapplied modification, the "apply" button should be
	// highlighted
	private void registerModification(Command c) {
		unappliedModifications.add(c);
		applyButton.setSelected(false);
	}
	
	//this function applies the modifications registered in unappliedModifications
	private void applyModifications() {
		Iterator<Command> it = unappliedModifications.iterator();
		while (it.hasNext()) {
		   it.next().execute(); // must be called before you can call i.remove()
		   it.remove();
		}

	}
	
	
	private interface Command {
		public void execute();
	}
	
}
