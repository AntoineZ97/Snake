package snake;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainApp extends Application {
		
	public void start (Stage primaryStage) throws Exception {
		
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("RootLayout.fxml"));
		AnchorPane rootLayout= (AnchorPane) loader.load();
		
		Scene scene = new Scene(rootLayout);
		primaryStage.setTitle("Snake");
		primaryStage.setScene(scene);
		primaryStage.show();

		Controller controller = (Controller)loader.getController();
		controller.setStage(primaryStage);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}