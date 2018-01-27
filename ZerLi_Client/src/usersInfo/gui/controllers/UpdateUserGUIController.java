package usersInfo.gui.controllers;

import java.awt.TextField;
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
import common.gui.controllers.ParentGUIController;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import orderNproducts.entities.Store;
import usersInfo.entities.Customer;
import usersInfo.entities.StoreWorker;
import usersInfo.entities.User;
import usersInfo.entities.User.UserType;

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
	
	/**if all setXXX functions called counters*/
	private int sets_invoked_cnt = 0,
			sets_needed_cnt = 0;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			ParentGUIController.currentGUI=this;
			tglActive.selectedProperty().addListener((ob,oldVal,newVal)->toggleChanged());
			Context.mainScene.setMenuPaneDisable(true);
			sets_needed_cnt++;
			Context.fac.user.getAllUsers();
			sets_needed_cnt++;
			Context.fac.store.getAllStores();
			cbPermissions.setItems(FXCollections.observableArrayList(UserType.values()));
		} catch (IOException e) {
			Context.mainScene.ShowErrorMsg();
			e.printStackTrace();
		}
	}
	
	
	private void checkIfNeedDisableFalse() {
		sets_invoked_cnt++;
		if(sets_needed_cnt==sets_invoked_cnt) {
			Context.mainScene.setMenuPaneDisable(false);
			sets_needed_cnt=sets_invoked_cnt=0;
		}
	}
	
	/**
	 * Function get {@link ArrayList} of {@link User}, check if he valid
	 * <p> set the {@link User}s in the {@link ComboBox} or save the specific {@link User} to update
	 * @param users - {@link ArrayList} of {@link User} from DB
	 */
	public void setUsers(ArrayList<User> users) {
		checkIfNeedDisableFalse();
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
	
	/**
	 * Function get {@link ArrayList} of {@link Store}, check if he is valid
	 * and set the {@link Store}s in the {@link ComboBox}
	 * @param stores - {@link ArrayList} of {@link Store} from DB
	 */
	public void setStores(ArrayList<Store> stores) {
		checkIfNeedDisableFalse();
		if(stores == null || stores.isEmpty()) {
			Context.mainScene.ShowErrorMsg();
			return;
		}
		if(Platform.isFxApplicationThread())
			cbStores.setItems(FXCollections.observableArrayList(stores));
		else Platform.runLater(()->cbStores.setItems(FXCollections.observableArrayList(stores)));
	}
	
	/**
	 * Function check the selected {@link User} data and if it's valid
	 * show his data in the {@link TextField}s 
	 */
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
	
	/**
	 * Function get Array of {@link Object} and check if his data is valid
	 * @param oarr - {@link Object}[]
	 * @return True/False
	 */
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
	
	/**
	 * Function the {@link User} that selected permission and update the view accordingly
	 */
	public void permissionChanged() {
		UserType perm = cbPermissions.getValue();
		if(!user.getPermissions().equals(UserType.StoreManager) &&
				!user.getPermissions().equals(UserType.ChainStoreManager)&&
				!user.getPermissions().equals(UserType.Customer)&&
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
					UserType.StoreWorker,UserType.ChainStoreWorker,UserType.CustomerServiceWorker, UserType.ServiceExpert));;
		}
		if(user.getPermissions().equals(UserType.ServiceExpert)) {
			cbPermissions.setItems(FXCollections.observableArrayList(UserType.StoreManager,
					UserType.StoreWorker,UserType.ChainStoreWorker,UserType.ServiceExpert));;
		}
		if(user.getPermissions().equals(UserType.ChainStoreWorker)) {
			cbPermissions.setItems(FXCollections.observableArrayList(UserType.StoreManager,
					UserType.StoreWorker,UserType.ChainStoreWorker, UserType.ServiceExpert));;
		}
		if(perm!=null) { 
			if(perm.equals(UserType.StoreWorker) || perm.equals(UserType.StoreManager) ) {
				//Is already Store worker/manager
				if(user.getPermissions().equals(perm)) {
					try {
						Context.mainScene.setMenuPaneDisable(true);
						sets_needed_cnt++;
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
						Context.mainScene.setMenuPaneDisable(true);
						sets_needed_cnt++;
						Context.fac.customer.getCustomerByUser(user.getUserID());
					}
					catch (IOException e) {
						Context.mainScene.ShowErrorMsg();
						e.printStackTrace();
					}
				}
			}
			if(user.getPermissions().equals(UserType.ChainStoreManager) || perm.equals(UserType.ChainStoreManager)
					|| perm.equals(UserType.ChainStoreWorker) || perm.equals(UserType.CustomerServiceWorker) || perm.equals(UserType.ServiceExpert) ){
				cbStores.setVisible(false);
			}
		}	
	}
	
	/**
	 * Function get {@link ArrayList} of {@link StoreWorker}, check if he is valid
	 * and set the {@link StoreWorker} permission and {@link Store} in the {@link ComboBox}s
	 * @param sws - {@link ArrayList} of {@link StoreWorker} from DB
	 */
	public void setStoreWorkers(ArrayList<StoreWorker> sws) {
		checkIfNeedDisableFalse();
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
	
	/**
	 * Function get {@link ArrayList} of {@link Customer}, check if he is valid
	 * and set the {@link Customer} for this specific class
	 * @param custs - {@link ArrayList} of {@link Customer} from DB
	 */
	public void setCustomers(ArrayList<Customer> custs)
	{
		checkIfNeedDisableFalse();
		if(custs == null || custs.size() != 1) {
			Context.mainScene.ShowErrorMsg();
			return;
		}
		this.cust=custs.get(0);
	}
	
	/**
	 *Function check if the {@link User} is Active and change the {@link ToggleButton} style
	 *accordingly
	 */
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
	
	/**
	 *Function check the changes in the {@link User} permission ,his activation and update the DB
	 * <p> If there are no changes - an appropriate message appears
	 */
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
			final UserType  p_sw = UserType.StoreWorker, p_sm = UserType.StoreManager,p_Csw1=UserType.ChainStoreWorker, p_Csm=UserType.ChainStoreManager,
					p_Csw2 = UserType.CustomerServiceWorker,p_se = UserType.ServiceExpert, oldperm = user.getPermissions();
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
				if(newperm.equals(p_Csw1)|| newperm.equals(p_Csw2)||newperm.equals(p_se)) {
					if(oldperm.equals(p_sw))
					{
						Context.fac.storeWorker.delete(this.sw.getUserID());;
					}
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
	
	/**
	 *Function get the new {@link StoreWorker} id 
	 *<p>attach it to the new {@link StoreWorker} and update his {@link Store}
	 * @param id - new {@link StoreWorker} ID
	 */
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
