package customersSatisfaction.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Context;
import common.gui.controllers.ParentGUIController;
import customersSatisfaction.entities.Survey;
import customersSatisfaction.entities.Survey.SurveyType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import orderNproducts.entities.Store;

public class SurveyGUIController implements Initializable {

	private @FXML ChoiceBox<Float>[] cbs;
	private @FXML ChoiceBox<Float> cb1, cb2, cb3, cb4, cb5, cb6;
	
	private ObservableList<Float> list;
	private @FXML DatePicker dpDate;

	/**
	 * Function to send filled {@link Survey} to DB
	 */
	public void sendSurvey() {
		float[] ans = new float[6];
		int i = 0;
		for (ChoiceBox<Float> cb : cbs) {
			ans[i] = cb.getValue();
			i++;
		}
		try {
			if(Context.getUserAsStoreWorker() !=null) {
				Survey sur = new Survey(ans, dpDate.getValue(),Context.getUserAsStoreWorker().getStore().getStoreID(),SurveyType.Answer);
				Context.fac.survey.add(sur);
				Context.mainScene.loadMainMenu();
			}
			else {
				Context.mainScene.loadMainMenu("You are not a store worker or manager");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		cbs = new ChoiceBox[]{cb1,cb2,cb3,cb4,cb5,cb6};
		ArrayList<Float> ar = new ArrayList<>();
		
		for (Float j = 1f; j <= 10f; j++)
			ar.add(j);
		list =FXCollections.observableArrayList(ar);
		
		for (int i = 0; i < cbs.length; i++) {
			cbs[i].setItems(list);
			cbs[i].getSelectionModel().selectFirst();
		}
		
		dpDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                //disable today and every date before
                setDisable(empty || date.isAfter(LocalDate.now()));
            }
        });
		dpDate.setValue(LocalDate.now());
	}
}