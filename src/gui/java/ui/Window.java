package gui.java.ui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Window {
		
		//a convenience constructor for initializing a window 
		public Window(Stage stage, String FXML_PATH, Object controller) {
			try {
				//
				FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_PATH));
				loader.setController(controller);
				
				//loading the root node with this controller
				Parent root = loader.load();
				Scene scene = new Scene(root,400,400);
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
}
