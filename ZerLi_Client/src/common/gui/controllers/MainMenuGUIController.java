package common.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import common.Context;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import usersInfo.entities.User;

public class MainMenuGUIController implements Initializable{
	private @FXML Label lblWelcome;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		User u = Context.getUser();
		if(u!=null && u.getFullName() != null)
			lblWelcome.setText("Welcome "+u.getFullName()+" to ZerLi online flower store");
	}
}
