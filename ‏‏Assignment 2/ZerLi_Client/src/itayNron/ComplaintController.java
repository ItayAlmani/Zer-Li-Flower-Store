package itayNron;

import java.io.IOException;
import java.util.ArrayList;

import common.Context;
import controllers.ParentController;
import entities.CSMessage;
import entities.Complaint;
import entities.SurveyReport;
import entities.CSMessage.MessageType;
import itayNron.interfaces.IComplaint;

public class ComplaintController extends ParentController implements IComplaint {

	@Override
	public void add(Complaint complaint, boolean getCurrentID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		ArrayList<Object> arr = new ArrayList<>();
		arr.add(complaint);
		arr.add(getCurrentID);
		myMsgArr.add(arr);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.INSERT, myMsgArr,Complaint.class));
	}
	
	@Override
	public void updateComplaint(Complaint complaint) {
	}	

	@Override
	public void getComplaintsByStore(int storeid) {
	}
	
	@Override
	public void sendComplaints(ArrayList<Complaint> complaints) {
	}

	@Override
	public void getNotTreatedComplaints() {
	}

	@Override
	public void getAllComplaints() {
	}

	@Override
	public void handleGet(ArrayList<Object> obj) {
		// TODO Auto-generated method stub
		
	}
}