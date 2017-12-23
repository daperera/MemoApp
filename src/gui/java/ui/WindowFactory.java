package gui.java.ui;

import gui.java.controller.MainWindowController;
import gui.java.controller.ManageMemoWindowController;
import gui.java.controller.MirrorWindowController;
import gui.java.controller.PreferenceWindowController;
import gui.java.service.NodePrototype.NodePrototype;
import gui.java.service.NodePrototype.NodePrototypeFactory;
import javafx.application.Platform;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class WindowFactory {
	
	private static final String MAINWINDOW_FXML = "/gui/resources/fxml/mainWindow.fxml";
	private static final String MIRRORWINDOW_FXML = "/gui/resources/fxml/mirrorWindow.fxml";
	private static final String PREFERENCEWINDOW_FXML = "/gui/resources/fxml/preferenceWindow.fxml";
	private static final String MANAGEMEMOWINDOW_FXML = "/gui/resources/fxml/manageMemoWindow.fxml";
	
	/**
	 * This function initialize the primaryStage to make it the main window
	 * of the application.
	 * The application exit when this window is closed
	 * 
	 * @param primaryStage : this is the argument of the start function of 
	 * 						 the javafx.Application object
	 * 
	 * @return a new window object corresponding to the main window of the application
	 */
	public static Window createMainWindow(Stage primaryStage) {
		// injecting a default mainPanel and a configuration object to the controller of the main window
		NodePrototype mainPanelPrototype = NodePrototypeFactory.createMainPanelPrototype();
		
		Object controller = new MainWindowController(mainPanelPrototype);
		
		//set the application to exit when this window is closed
		primaryStage.setOnHidden(e -> Platform.exit());
		
		//set the name of the window
		primaryStage.setTitle("Mnema");
		
		//creating main window
		Window mainWindow = new Window(primaryStage, MAINWINDOW_FXML, controller);
		
		return mainWindow;
	}
	
	
	/**
	 * This convenience function create a window mirroring an other window. Specifically,
	 * it copies the mirrored window mainPanel into its own mainPanel, making it look 
	 * identical.
	 * This function is typically invoked when the "detach" button of a window possessing 
	 * a banner is clicked.
	 * 
	 * @param mainPanelPrototype : NodePrototype object allowing the controller to recover
	 * 							   the mainPanel of the window it mirrors, through its function
	 * 							   clone().
	 * 
	 * @return a new Window object corresponding to the mirrored window 
	 */
	public static Window createMirrorWindow(NodePrototype mainPanelPrototype) {
		// creating a new stage 
		Stage secondaryStage = new Stage();
		
		// injecting the mainPanel of the window being mirrored to the 
		// controller of the mirror window.
		Object controller = new MirrorWindowController(mainPanelPrototype);
		
		//creating mirror window
		Window mirrorWindow = new Window(secondaryStage, MIRRORWINDOW_FXML, controller);
		return mirrorWindow;
	}


	public static Window createPreferenceWindow() {
		// creating a new stage 
		Stage secondaryStage = new Stage();
		
		//setting a controller
		Object controller = new PreferenceWindowController(secondaryStage);

		//making it an modal window
		secondaryStage.initModality(Modality.APPLICATION_MODAL);
		
		//this stage cannot be resized
		secondaryStage.setResizable(false);
		
		//creating window
		Window preferenceWindow = new Window(secondaryStage, PREFERENCEWINDOW_FXML, controller);

		return preferenceWindow;
	}


	public static Window createManageMemoWindow() {
		// creating a new stage 
		Stage secondaryStage = new Stage();

		//setting a controller
		Object controller = new ManageMemoWindowController();

		//making it an modal window
		secondaryStage.initModality(Modality.APPLICATION_MODAL);
		
		//this stage cannot be resized
		secondaryStage.setResizable(false);
		
		//creating window
		Window manageMemoWindow = new Window(secondaryStage, MANAGEMEMOWINDOW_FXML, controller);

		return manageMemoWindow;
	}
	
	
	
}
