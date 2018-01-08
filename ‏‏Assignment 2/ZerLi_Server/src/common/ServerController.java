package common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import controllers.SurveyController;
import controllers.SurveyReportController;
import entities.CSMessage;
import entities.Order;
import entities.ProductInOrder;
import entities.Survey;
import entities.SurveyReport;
import entities.CSMessage.MessageType;
import izhar.OrderController;
import izhar.ProductInOrderController;

public class ServerController {
	private static CSMessage sendRequest(CSMessage csMsg) throws Exception {
		MessageType msgType = csMsg.getType();
		ArrayList<Object> objArr = csMsg.getObjs();
		Class<?> c = getObjectClass(csMsg);
		Method m = null;
		
		
		if(c!=null && objArr!=null && objArr.size()>=2 
				&& objArr.get(0) instanceof String) {
			try {
				m = c.getMethod((String)objArr.get(0),objArr.get(1).getClass());
				Object arr = m.invoke(c.newInstance(), objArr.get(1));
				if(arr!=null && arr instanceof ArrayList<?>) {
					csMsg.setObjs((ArrayList<Object>)arr);
					return csMsg;
				}
				else
					throw new Exception();
			} catch (NoSuchMethodException | SecurityException |IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
				e.printStackTrace();
			}
		}
		else {
			System.err.println("Problem in ServerController\n");
			throw new Exception();
		}
			
		return csMsg;
	}
	
	public static CSMessage setMessageToClient(CSMessage csMsg) throws Exception{
		MessageType msgType = csMsg.getType();
		ArrayList<Object> objArr = csMsg.getObjs();
		
		if(csMsg.getClasz()!=null) {
			if(csMsg.getClasz().equals(Order.class) ||
					csMsg.getClasz().equals(ProductInOrder.class)) {
				return sendRequest(csMsg);
			}
		}
		
		if(msgType.equals(MessageType.SELECT) || msgType.equals(MessageType.UPDATE)
				|| msgType.equals(MessageType.GetAI)) {
			if(objArr.size() == 1 && objArr.get(0) instanceof String) {
				String query = (String) objArr.get(0);
				objArr.clear();
				
				/*-------SELECT queries from DB-------*/
				if(msgType.equals(MessageType.SELECT) || msgType.equals(MessageType.GetAI))
					csMsg.setObjs(EchoServer.fac.dataBase.db.getQuery(query));
				
				/*-------UPDATE queries from DB-------*/
				else if(msgType.equals(MessageType.UPDATE)){
					try {
						EchoServer.fac.dataBase.db.updateQuery(query);
						objArr.add(true);
					} catch (Exception e) {
						objArr.add(false);
					}
					csMsg.setObjs(objArr);
				}
			}
			else {
				System.err.println("Not query");
				throw new Exception();
			}
		}
		else if(msgType.equals(MessageType.INSERT)) {
			if(csMsg.getObjs() != null &&
					csMsg.getObjs().size()!=0 && csMsg.getObjs().get(0) != null) {
				/*if(csMsg.getClasz().equals(Order.class) && 
						csMsg.getObjs().get(0) instanceof Order) {
					Order ord = (Order)(csMsg.getObjs().get(0));
					objArr.clear();
					objArr.add(EchoServer.addOrder(ord));
				}
				else*/ if(csMsg.getClasz().equals(Survey.class) && 
						csMsg.getObjs().get(0) instanceof Survey) {
					Survey survey = (Survey)(csMsg.getObjs().get(0));
					objArr.clear();
					objArr.add(SurveyController.addSurvey(survey));	
				}
				else if(csMsg.getClasz().equals(SurveyReport.class) && 
						csMsg.getObjs().get(0) instanceof SurveyReport) {
					SurveyReport sr = (SurveyReport)(csMsg.getObjs().get(0));
					objArr.clear();
					objArr.add(SurveyReportController.addSurveyReport(sr));
				}
				/*else if(csMsg.getClasz().equals(ProductInOrder.class) &&
						csMsg.getObjs().get(0) instanceof ProductInOrder) {
					ProductInOrder pio = (ProductInOrder)(csMsg.getObjs().get(0));
					objArr.clear();
					objArr.add(EchoServer.fac.prodInOrder.add(pio));
				}*/
				csMsg.setObjs(objArr);
			}
		}
		else if(msgType.equals(MessageType.DBStatus)) {
			if(objArr!=null)	objArr.clear();
			else				objArr = new ArrayList<>();
			objArr.add(EchoServer.fac.dataBase.db!=null);
			csMsg.setObjs(objArr);
		}
		else if(msgType.equals(MessageType.DBData)) {
			csMsg.setObjs(EchoServer.fac.dataBase.getDBData());
		}
		else if(msgType.equals(MessageType.SetDB)) {
			EchoServer.fac.dataBase.setDBData(objArr);
			csMsg.setObjs(objArr);
		}
		return csMsg;
	}
	
	private static Class<?> findHandleGetFunc(String className, String classPath) {
		try {
			return Class.forName(classPath+className+"Controller");
		} catch (Exception e) {
			return null;
		}
	}
	
	private static Class<?> getObjectClass(CSMessage csMsg){
		Class<?> c = null;
		if(csMsg.getClasz()!=null) {
			String className = csMsg.getClasz().getName();
			className=className.substring(className.lastIndexOf("."));
			if((c=findHandleGetFunc(className, "controllers"))==null)
				if((c=findHandleGetFunc(className, "itayNron"))==null)
					if((c=findHandleGetFunc(className, "izhar"))==null)
						if((c=findHandleGetFunc(className, "lior"))==null)
							c=findHandleGetFunc(className, "kfir");
		}
		return c;
	}
}
