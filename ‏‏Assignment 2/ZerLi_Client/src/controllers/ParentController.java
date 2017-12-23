package controllers;

import java.util.ArrayList;

import common.Context;
import gui.controllers.ParentGUIController;

public abstract class ParentController {
	protected static ArrayList<Object> myMsgArr = new ArrayList<>();
	
	public static void sendResultToClient(boolean response) {
		if(response==true)
			((ParentGUIController)Context.currentGUI).ShowSuccessMsg();
		else
			((ParentGUIController)Context.currentGUI).ShowErrorMsg();
	}
}
