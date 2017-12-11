package controllers;

import common.Context;
import gui.controllers.ParentGUIController;

public class ParentController {

	public static void sendResultToClient(boolean response) {
		if(response==true)	((ParentGUIController)Context.CurrentGUI).ShowSuccessMsg();
		else				((ParentGUIController)Context.CurrentGUI).ShowErrorMsg();
	}
}
