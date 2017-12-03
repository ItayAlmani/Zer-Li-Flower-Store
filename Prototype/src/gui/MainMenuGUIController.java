package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainMenuGUIController implements Initializable  {
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {		
	}
	
	public void showProducts(ActionEvent event) throws Exception {
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		Pane root = FXMLLoader.load(getClass().getResource("/gui/ProductsFormGUI.fxml"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);		
		primaryStage.show();
		
	}
	
	public void showConnectionGUI(ActionEvent event) throws Exception{
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		Pane root = FXMLLoader.load(getClass().getResource("/gui/ConnectionConfigGUI.fxml"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);		
		primaryStage.show();
	}
	
	public void start(Stage primaryStage) throws Exception {	
		Parent root = FXMLLoader.load(getClass().getResource("/gui/MainMenuGUI.fxml"));
				
		Scene scene = new Scene(root);
		//primaryStage.setTitle("Academic Managment Tool");
		primaryStage.setScene(scene);
		
		primaryStage.show();		
	}
}
