package gui.java;

import gui.java.ui.WindowFactory;
import javafx.application.Application;
import javafx.stage.Stage;

public class GUI extends Application{
	
	public GUI() {
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		WindowFactory.createMainWindow(primaryStage);
	}
	
	public static void startApplication(String[] args) {
		launch(args);
	}
	
}
