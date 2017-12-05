package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class TemplateGUI {
	
	@FXML
	protected Button btnExit;
	
	public void ExitProg(ActionEvent event) throws Exception {
		Stage stage = (Stage) btnExit.getScene().getWindow();
		stage.close();
		System.exit(0);			
	}
}
