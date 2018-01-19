package kfir.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;

import common.Context;
import entities.Customer;
import entities.Store;
import entities.StoreWorker;
import entities.User;
import entities.User.UserType;
import gui.controllers.ParentGUIController;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class UpdateUserGUIController implements Initializable{

	@FXML JFXComboBox<User> cbUsers;
	@FXML VBox paneUDet;
	@FXML JFXTextField txtUID, txtPID,txtUName,txtPass,txtFName,txtLName;
	@FXML JFXComboBox<UserType> cbPermissions;
	@FXML JFXComboBox<Store> cbStores;
	@FXML JFXToggleButton tglActive;
	private User user = null;
	private StoreWorker sw = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			ParentGUIController.currentGUI=this;
			tglActive.selectedProperty().addListener((ob,oldVal,newVal)->toggleChanged());
			Context.fac.user.getAllUsers();
			Context.fac.store.getAllStores();
			cbPermissions.setItems(FXCollections.observableArrayList(UserType.values()));
		} catch (IOException e) {
			Context.mainScene.ShowErrorMsg();
			e.printStackTrace();
		}
		
	}
	
	public void setUsers(ArrayList<User> users) {
		if(users == null || users.isEmpty()) {
			Context.mainScene.ShowErrorMsg();
			return;
		}
		if(Platform.isFxApplicationThread())
			cbUsers.setItems(FXCollections.observableArrayList(users));
		else Platform.runLater(()->cbUsers.setItems(FXCollections.observableArrayList(users)));
	}
	
	public void setStores(ArrayList<Store> stores) {
		if(stores == null || stores.isEmpty()) {
			Context.mainScene.ShowErrorMsg();
			return;
		}
		if(Platform.isFxApplicationThread())
			cbStores.setItems(FXCollections.observableArrayList(stores));
		else Platform.runLater(()->cbStores.setItems(FXCollections.observableArrayList(stores)));
	}
	
	public void userSelected() {
		this.user = cbUsers.getValue();
		paneUDet.setVisible(false);
		if(user != null){
			String uid = user.getUserID().toString(), pid = user.getPrivateID(),
					uname = user.getUserName(), pass = user.getPassword(),
					fname = user.getFirstName(), lname = user.getLastName();
			UserType perm = user.getPermissions();
			Boolean isAct = user.isActive();
			if(isDataValid(new Object[] {uid,pid,uname,pass,fname,lname,perm,isAct})==false) {
				Context.mainScene.ShowErrorMsg();
				paneUDet.setVisible(false);
				return;
			}
			txtUID.setText(uid);
			txtPID.setText(pid);
			txtUName.setText(uname);
			txtPass.setText(pass);
			txtFName.setText(fname);
			txtLName.setText(lname);
			tglActive.setSelected(isAct);
			cbPermissions.setValue(perm);
			paneUDet.setVisible(true);
		}
	}
	
	private boolean isDataValid(Object[] oarr) {
		for (Object o : oarr) {
			if(o == null)
				return false;
			if(o instanceof String)
				if(((String)o).isEmpty())
					return false;
		}
		return true;
	}
	
	public void permissionChanged() {
		UserType perm = cbPermissions.getValue();
		if(!user.getPermissions().equals(UserType.StoreManager) &&
				!user.getPermissions().equals(UserType.ChainStoreManager)) {
			cbPermissions.setDisable(false);
			cbStores.setDisable(false);
		}
		else {
			cbPermissions.setDisable(true);
			cbStores.setDisable(true);
		}
		if(perm!=null) { 
			if(perm.equals(UserType.StoreWorker)) {
				//Is already Store worker/manager
				if(user.getPermissions().equals(perm)) {
					try {
						Context.fac.storeWorker.getStoreWorkerByUser(user.getUserID());
					} catch (IOException e) {
						Context.mainScene.ShowErrorMsg();
						e.printStackTrace();
					}
				}
				else
					cbStores.setVisible(true);
			}
			else if(perm.equals(UserType.StoreManager))
				cbStores.setVisible(true);
			else	
				cbStores.setVisible(false);
		}
	}
	
	public void setStoreWorkers(ArrayList<StoreWorker> sws) {
		if(sws == null || sws.size() != 1 || sws.get(0).getStore() == null) {
			Context.mainScene.ShowErrorMsg();
			return;
		}
		this.sw=sws.get(0);
		if(Platform.isFxApplicationThread()) {
			if(sw.getPermissions().equals(UserType.StoreManager) || 
					sw.getPermissions().equals(UserType.ChainStoreManager)) {
				cbPermissions.setDisable(true);
				cbStores.setDisable(true);
			}
			cbStores.setValue(null);
			for (Store s : cbStores.getItems()) {
				if(s.getStoreID().intValue()==sw.getStore().getStoreID().intValue()) {
					cbStores.setValue(s);
					break;
				}
			}
			cbStores.setVisible(true);
			paneUDet.setVisible(true);
		}
		else Platform.runLater(()->{
			if(sw.getPermissions().equals(UserType.StoreManager) || 
					sw.getPermissions().equals(UserType.ChainStoreManager)) {
				cbPermissions.setDisable(true);
				cbStores.setDisable(true);
			}
			cbStores.setValue(null);
			for (Store s : cbStores.getItems()) {
				if(s.getStoreID().intValue()==sw.getStore().getStoreID().intValue()) {
					cbStores.setValue(s);
					break;
				}
			}
			cbStores.setVisible(true);
		});
	}
	
	public void toggleChanged() {
		if(tglActive.isSelected()) {
			tglActive.setText("Active");
			tglActive.setTextFill(Color.ORANGE);
		}
		else {
			tglActive.setText("Not Active");
			tglActive.setTextFill(Color.RED);
		}
	}
	
	public void updateUser() {
		UserType newperm = cbPermissions.getValue();
		boolean isAct = tglActive.isSelected();
		if(newperm==null) {
			Context.mainScene.ShowErrorMsg();
			return;
		}
		if(user.getPermissions().equals(newperm) && user.isActive()==isAct) {
			Context.mainScene.setMessage("Nothing changed because the data is the same");
			return;
		}
		//try {
			UserType p_cus = UserType.Customer, p_sw = UserType.StoreWorker, p_sm = UserType.StoreManager,
					oldperm = user.getPermissions();
			
			//the permissions haven't changed
			if(oldperm.equals(newperm) == false) {
				//==========================================================
				if(oldperm.equals(p_cus)) {
					//Context.fac.customer.delete(user.getUserID());
					System.out.println("now i am "+oldperm+" -> delete");
				}
				else if(oldperm.equals(p_sw)) {
					//Context.fac.storeWorker.delete(user.getUserID());
					System.out.println("now i am "+oldperm+" -> delete");
				}
				//==========================================================
				
				if(newperm.equals(p_cus)) {
					//Context.fac.customer.add(user.getUserID());
					System.out.println("now i am "+newperm+" -> add");
				}
				else if(newperm.equals(p_sw) || newperm.equals(p_sm)) {
					if(cbStores.getValue() == null) {
						Context.mainScene.ShowErrorMsg();
						return;
					}
					Store newStore = cbStores.getValue();
					sw = new StoreWorker(user, newStore);
					
					//new store manager of the store
					if(newperm.equals(p_sm)) {
						StoreWorker oldManager = newStore.getManager();
						newStore.setManager(sw);
						//Context.fac.store.update(newStore);
						System.out.println("now i'm a manager of the store "+ newStore.getName());
						
						//old store manager became store worker
						oldManager.setPermissions(p_sw);
						//Context.fac.user.update(oldManager);
						System.out.println(oldManager.getFullName() + " now is worker at "+ oldManager.getStore());
					}
					//Context.fac.storeWorker.add(sw);
					System.out.println("now i am "+newperm+" -> add");
				}
			}
			//same permission
			else {
				if(newperm.equals(p_sw)) {
					Store newStore = cbStores.getValue();
					int old_sid = sw.getStore().getStoreID().intValue();
					if(cbStores.getValue() == null) {
						Context.mainScene.ShowErrorMsg();
						return;
					}
					//store changed
					if(newStore.getStoreID().intValue()!=old_sid) {
						sw.setStore(newStore);
						//Context.fac.storeWorker.update(sw);
						System.out.println("now my store is "+ newStore.getName());
					}
				}
			}
			user.setPermissions(newperm);
			user.setActive(isAct);
			//Context.fac.user.update(user);
			/*}
		catch (IOException e) {
			Context.mainScene.ShowErrorMsg();
			e.printStackTrace();
		}*/
	}
}