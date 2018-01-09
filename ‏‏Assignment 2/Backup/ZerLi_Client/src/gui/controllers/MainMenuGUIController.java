package gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import common.Context;
import javafx.fxml.Initializable;

public class MainMenuGUIController implements Initializable{
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Context.currentGUI = this;
	}
}
