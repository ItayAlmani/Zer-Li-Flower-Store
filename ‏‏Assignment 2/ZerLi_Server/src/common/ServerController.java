package common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import controllers.*;
import entities.*;
import entities.CSMessage.MessageType;
import itayNron.SurveyController;
import itayNron.SurveyReportController;

public class ServerController {
	public static CSMessage setMessageToClient(CSMessage csMsg) throws Exception{
		MessageType msgType = csMsg.getType();
		ArrayList<Object> objArr = csMsg.getObjs();
		
		if(csMsg.getClasz()!=null) {
			if(csMsg.getClasz().equals(Order.class) ||
					csMsg.getClasz().equals(ProductInOrder.class) ||
					csMsg.getClasz().equals(Product.class) ||
					csMsg.getClasz().equals(Stock.class) ||
					csMsg.getClasz().equals(Store.class) ||
					csMsg.getClasz().equals(Survey.class) ||
					csMsg.getClasz().equals(SurveyReport.class) ||
					csMsg.getClasz().equals(OrderReport.class)||
					csMsg.getClasz().equals(IncomesReport.class)||
					csMsg.getClasz().equals(SatisfactionReport.class)){
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
		/*else if(msgType.equals(MessageType.INSERT)) {
			if(csMsg.getObjs() != null &&
					csMsg.getObjs().size()!=0 && csMsg.getObjs().get(0) != null) {
				if(csMsg.getClasz().equals(Order.class) && 
						csMsg.getObjs().get(0) instanceof Order) {
					Order ord = (Order)(csMsg.getObjs().get(0));
					objArr.clear();
					objArr.add(EchoServer.addOrder(ord));
				}
				else if(csMsg.getClasz().equals(Survey.class) && 
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
				}
				csMsg.setObjs(objArr);
			}
		}*/
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
	
	private static CSMessage sendRequest(CSMessage csMsg) throws Exception {
		ArrayList<Object> objArr = csMsg.getObjs();
		Class<?> c = getObjectClass(csMsg);
		Method m = null;
		
		
		if(c!=null && objArr!=null && objArr.size()>=1 
				&& objArr.get(0) instanceof String) {
			String funcName = (String)objArr.get(0);
			if(objArr.size()==1)
				callToMethod(c, funcName, csMsg);
			else if(objArr.size()==2)
				callToMethod(c, funcName,csMsg,objArr.get(1));
		}
		else {
			System.err.println("Problem in ServerController\n");
			throw new Exception();
		}
			
		return csMsg;
	}
	
	private static void callToMethod(Class<?> c, String funcName, CSMessage csMsg) throws Exception {
		try {
			Method m = c.getMethod(funcName);
			Object arr = m.invoke(c.newInstance());
			if(arr!=null && arr instanceof ArrayList<?>) {
				csMsg.setObjs((ArrayList<Object>)arr);
			}
			else
				throw new Exception();
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
			if(e.getMessage()==null && e.getCause().getMessage()==null)
				e.printStackTrace();
			throw e;
		}
		
	}
	
	private static void callToMethod(Class<?> c, String funcName, CSMessage csMsg, Object o) throws Exception {
		Method m = null;
		try {
			m = c.getMethod(funcName,o.getClass());
			Object arr = m.invoke(c.newInstance(), o);
			if(arr!=null && arr instanceof ArrayList<?>) {
				csMsg.setObjs((ArrayList<Object>)arr);
			}
			else
				throw new Exception();
		}catch (NoSuchMethodException e) {
			m = c.getMethod(funcName,Object.class);
			Object arr = m.invoke(c.newInstance(), o);
			if(arr!=null && arr instanceof ArrayList<?>)
				csMsg.setObjs((ArrayList<Object>) arr);
			else
				throw new Exception();
		} catch (SecurityException |IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
			if(e.getMessage()==null && e.getCause().getMessage()==null)
				e.printStackTrace();//<------------FIX
			throw e;
		}
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
