package gui.java.ui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public final class Window {
		
		//a convenience constructor for initializing a window 
		public Window(Stage stage, String FXML_PATH, Object controller) {
			try {
				//loading the controller of this stage
				FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_PATH));
				loader.setController(controller);
				
				//loading the root node with this controller
				Parent root = loader.load();
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.sizeToScene();
				
				//setting a custom icon
				stage.getIcons().add(new Image(this.getClass().getResource("/gui/resources/data/main-icon.png").toString()));
				
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
}
