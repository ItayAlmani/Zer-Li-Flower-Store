package kfir.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
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
	private User user = null, old_user=null;
	User old_user_to_update=null;
	private StoreWorker sw = null;
	private Store newStore=null;
	private Customer cust=null;

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
		else {
			if(Platform.isFxApplicationThread()) {
				if(users.size()==1){
					old_user_to_update = users.get(0);
				}
				else
					cbUsers.setItems(FXCollections.observableArrayList(users));
			}
			else Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if(users.size()==1){
						old_user_to_update = users.get(0);
						old_user_to_update.setPermissions(UserType.StoreWorker);
						try {
							Context.fac.user.update(old_user_to_update);
						} catch (IOException e) {
							Context.mainScene.ShowErrorMsg();
							e.printStackTrace();
						}
					}
					else
						cbUsers.setItems(FXCollections.observableArrayList(users));
				}	
			});
		}
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
		if(this.user != null) {
			old_user = this.user;
		}
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
			if(old_user!=null) {
				if(old_user.equals(user)==false) {
					permissionChanged();
				}
			}
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
				!user.getPermissions().equals(UserType.ChainStoreManager)&&
				!user.getPermissions().equals(UserType.Customer)&&
				!user.getPermissions().equals(UserType.ServiceExpert)&&
				!user.getPermissions().equals(UserType.CustomerServiceWorker)) {
			cbPermissions.setDisable(false);
			cbStores.setDisable(false);
		}
		else {
			cbPermissions.setDisable(true);
			cbStores.setDisable(true);
		}
		if(user.getPermissions().equals(UserType.StoreWorker)) {
			cbPermissions.setItems(FXCollections.observableArrayList(UserType.StoreManager,
					UserType.StoreWorker,UserType.ChainStoreWorker));;
		}
		if(user.getPermissions().equals(UserType.ChainStoreWorker)) {
			cbPermissions.setItems(FXCollections.observableArrayList(UserType.StoreManager,
					UserType.StoreWorker,UserType.ChainStoreWorker));;
		}
		if(perm!=null) { 
			if(perm.equals(UserType.StoreWorker) || perm.equals(UserType.StoreManager) ) {
				//Is already Store worker/manager
				if(user.getPermissions().equals(perm)) {
					try {
						Context.fac.storeWorker.getStoreWorkerByUser(user.getUserID());
					} catch (IOException e) {
						Context.mainScene.ShowErrorMsg();
						e.printStackTrace();
					}
				}
				cbStores.setVisible(true);
			}
			if(perm.equals(UserType.Customer)) {
				cbStores.setVisible(false);
				//Is already Customer
				if(user.getPermissions().equals(perm)) {
					try {
						Context.fac.customer.getCustomerByUser(user.getUserID());
					}
					catch (IOException e) {
						Context.mainScene.ShowErrorMsg();
						e.printStackTrace();
					}
				}
			}
			if(user.getPermissions().equals(UserType.ChainStoreManager) || perm.equals(UserType.ChainStoreManager)
					|| perm.equals(UserType.ChainStoreWorker) || perm.equals(UserType.CustomerServiceWorker) ){
				cbStores.setVisible(false);
			}
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
	
	public void setCustomers(ArrayList<Customer> custs)
	{
		if(custs == null || custs.size() != 1) {
			Context.mainScene.ShowErrorMsg();
			return;
		}
		this.cust=custs.get(0);
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
			if(newperm.equals(UserType.StoreWorker) && cbStores.getValue().equals(this.sw.getStore()))
			{
				Context.mainScene.setMessage("Nothing changed because the data is the same");
				return;
			}
		}
		try {
			final UserType  p_sw = UserType.StoreWorker, p_sm = UserType.StoreManager,p_Csw=UserType.ChainStoreWorker, p_Csm=UserType.ChainStoreManager,
					oldperm = user.getPermissions();
			//the permissions haven't changed
			if(oldperm.equals(newperm) == false) {
				if(newperm.equals(p_sm)) {
					if(cbStores.getValue() == null) {
						Context.mainScene.ShowErrorMsg();
						return;
					}
					this.newStore = cbStores.getValue();
					StoreWorker oldManager = newStore.getManager();
					Context.fac.user.getUser(oldManager.getUserID());
						if(oldperm.equals(p_sw))
						{
							this.newStore.setManager(this.sw);
							this.sw.setStore(newStore);
							Context.fac.storeWorker.update(this.sw);
							Context.fac.store.update(newStore);
						}
						else {
						sw = new StoreWorker(user, newStore);
						Context.fac.storeWorker.add(sw, false);
						this.newStore.setManager(sw);
						}	
				}
				if(newperm.equals(p_sw)) {
					if(cbStores.getValue() == null) {
						Context.mainScene.ShowErrorMsg();
						return;
					}
					this.newStore = cbStores.getValue();
					sw = new StoreWorker(user, newStore);
					Context.fac.storeWorker.add(sw, false);
				}
				if(newperm.equals(p_Csw)) {
					if(cbStores.getValue() == null) {
						Context.mainScene.ShowErrorMsg();
						return;
					}
					if(oldperm.equals(p_sw))
					{
						Context.fac.storeWorker.delete(this.sw.getUserID());;
					}
					this.newStore = cbStores.getValue();
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
						Context.fac.storeWorker.update(sw);
					}
				}
			}
			user.setPermissions(newperm);
			user.setActive(isAct);
			Context.fac.user.update(user);
			Context.mainScene.loadMainMenu();
			}
		catch (Exception e) {
			Context.mainScene.ShowErrorMsg();
			e.printStackTrace();
		}
	}
	
	public void setSWid(BigInteger swID){
		this.sw.setStoreWorkerID(swID);
		try {
			Context.fac.store.update(newStore);
		} catch (IOException e) {
			Context.mainScene.ShowErrorMsg();
			e.printStackTrace();
		}
	}
}
