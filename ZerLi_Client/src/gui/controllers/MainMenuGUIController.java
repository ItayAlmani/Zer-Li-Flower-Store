package gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import common.Context;
import entities.Customer;
import entities.User;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainMenuGUIController implements Initializable{
	private @FXML Label lblWelcome;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		User u = Context.getUser();
		if(u!=null && u.getFullName() != null)
			lblWelcome.setText("Welcome "+u.getFullName()+" to ZerLi online flower store");
	}
}
