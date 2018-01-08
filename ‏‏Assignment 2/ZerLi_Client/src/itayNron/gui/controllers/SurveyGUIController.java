package itayNron.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Context;
import entities.Store;
import entities.Survey;
import entities.Survey.SurveyType;
import gui.controllers.ParentGUIController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;

public class SurveyGUIController implements Initializable {

	private @FXML ChoiceBox<Float>[] cbs;
	private @FXML ChoiceBox<Float> cb1;
	private @FXML ChoiceBox<Float> cb2;
	private @FXML ChoiceBox<Float> cb3;
	private @FXML ChoiceBox<Float> cb4;
	private @FXML ChoiceBox<Float> cb5;
	private @FXML ChoiceBox<Float> cb6;
	
	private ObservableList<Float> list;
	@FXML DatePicker dpDate;

	@FXML public void sendSurvey(ActionEvent event) throws IOException {
		/*StoreWorker sw = Context.getUserAsStoreWorker();
		if(sw == null)
			return;*/
		float[] ans = new float[6];
		int i = 0;
		for (ChoiceBox<Float> cb : cbs) {
			ans[i] = cb.getValue();
			i++;
		}
		//Date date = Date.from(dpDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
		if(Context.getUserAsStoreWorker() !=null) {
			Survey sur = new Survey(ans, dpDate.getValue(),Context.getUserAsStoreWorker().getStoreID(),SurveyType.Answer);
			
			Context.fac.survey.add(sur);
		}
		else
			System.err.println("You are not a store worker");
		Context.mainScene.loadMainMenu();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Context.currentGUI = this;
		
		cbs = new ChoiceBox[]{cb1,cb2,cb3,cb4,cb5,cb6};
		ArrayList<Float> ar = new ArrayList<>();
		
		for (Float j = 1f; j <= 10f; j++)
			ar.add(j);
		list =FXCollections.observableArrayList(ar);
		
		for (int i = 0; i < cbs.length; i++) {
			cbs[i].setItems(list);
			cbs[i].getSelectionModel().selectFirst();
		}
		dpDate.setValue(LocalDate.now());
	}
}