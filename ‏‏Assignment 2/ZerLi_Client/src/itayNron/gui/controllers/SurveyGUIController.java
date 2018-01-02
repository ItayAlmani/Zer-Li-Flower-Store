package itayNron.gui.controllers;

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
import javafx.scene.control.ChoiceBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;

public class SurveyGUIController extends ParentGUIController {

	private @FXML ChoiceBox<Integer>[] cbs;
	private @FXML ChoiceBox<Integer> cb1;
	private @FXML ChoiceBox<Integer> cb2;
	private @FXML ChoiceBox<Integer> cb3;
	private @FXML ChoiceBox<Integer> cb4;
	private @FXML ChoiceBox<Integer> cb5;
	private @FXML ChoiceBox<Integer> cb6;
	
	private ObservableList<Integer> list;
	@FXML DatePicker dpDate;

	@FXML public void sendSurvey(ActionEvent event) {
		/*StoreWorker sw = Context.getUserAsStoreWorker();
		if(sw == null)
			return;*/
		int[] ans = new int[6];
		int i = 0;
		for (ChoiceBox<Integer> cb : cbs) {
			ans[i] = cb.getValue();
			i++;
		}
		//Date date = Date.from(dpDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
		Survey sur = new Survey(ans, dpDate.getValue(), new Store(BigInteger.ONE),SurveyType.Answer);
		
		Context.fac.survey.addSurvey(sur);
		
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	        	loadMainMenu();
	        }
		});
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Context.currentGUI = this;
		
		cbs = new ChoiceBox[]{cb1,cb2,cb3,cb4,cb5,cb6};
		ArrayList<Integer> ar = new ArrayList<>();
		
		for (Integer j = 1; j <= 10; j++)
			ar.add(j);
		list =FXCollections.observableArrayList(ar);
		
		for (int i = 0; i < cbs.length; i++) {
			cbs[i].setItems(list);
			cbs[i].getSelectionModel().selectFirst();
		}
		dpDate.setValue(LocalDate.now());
	}
}