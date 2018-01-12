package common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import entities.CSMessage;
import entities.CSMessage.MessageType;

public class ServerController {
	/**
	 * 
	 * @param csMsg
	 * @return
	 * @throws Exception
	 */
	public static CSMessage setMessageToClient(CSMessage csMsg) throws Exception{
		MessageType msgType = csMsg.getType();
		ArrayList<Object> objArr = csMsg.getObjs();
		
		/*if there is entity that included in the request (not as attribute).
		 Additionally, if the request is not for DB*/  
		if(csMsg.getClasz()!=null)
			return sendRequest(csMsg);
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
	
	/**
	 * Decoding the request and sending the details to the correct {@code controller}.<br>
	 * Calling to {@link #callToMethod(Class, String, CSMessage)}<br>
	 * or {@link #callToMethod(Class, String, CSMessage, Object)}<br>
	 * with the message.
	 * @param csMsg includes the details of the message (funcName,
	 * parameters and {@link Class} of the entity)
	 * @return {@link CSMessage} with the response
	 * @throws Exception {@link #callToMethod(Class, String, CSMessage)}<br>or {@link #callToMethod(Class, String, CSMessage)}<br>or if there is no method called {@link CSMessage#getObjs()}.{@code get(0)}
	 */
	private static CSMessage sendRequest(CSMessage csMsg) throws Exception {
		ArrayList<Object> objArr = csMsg.getObjs();
		
		//AT FINAL PROJECT WILL BE - findHandleGetFunc with name.split(".)+"controllers"
		Class<?> c = getObjectClass(csMsg);
		
		if(c!=null && objArr!=null && !objArr.isEmpty()
				&& objArr.get(0) instanceof String) {
			String funcName = (String)objArr.get(0);	//objArr[0] is the function name
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
	
	/**
	 * With {@link Class} and {@link Method} we invoking the function by the parameters.
	 * In this function the requested function has no parameters.
	 * @param cls the {@link Class} of the requested function
	 * @param reqFuncName the name of the requested function 
	 * @param csMsg the request {@link Object}
	 * @throws Exception when method not found, the invoke failed (before or during running or no value returned from the invoke)
	 */
	private static void callToMethod(Class<?> cls, String reqFuncName, CSMessage csMsg) throws Exception {
		try {
			Method meth = cls.getMethod(reqFuncName);
			Object arr = meth.invoke(cls.newInstance());
			if(arr!=null && arr instanceof ArrayList) {
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
	
	/**
	 * With {@link Class} and {@link Method} we invoking the function by the parameters.
	 * In this function the requested function has no parameters.
	 * @param cls the {@link Class} of the requested function
	 * @param reqFuncName the name of the requested function 
	 * @param csMsg the request {@link Object}
	 * @param parameter the parameter of the requested function
	 * @throws Exception when method not found, the invoke failed (before or during running or no value returned from the invoke)
	 */
	private static void callToMethod(Class<?> cls, String reqFuncName, CSMessage csMsg, Object parameter) throws Exception {
		Method meth = null;
		try {
			meth = cls.getMethod(reqFuncName,parameter.getClass());
			Object arr = meth.invoke(cls.newInstance(), parameter);
			if(arr!=null && arr instanceof ArrayList) {
				csMsg.setObjs((ArrayList<Object>)arr);
			}
			else
				throw new Exception();
		}catch (NoSuchMethodException e) {
			meth = cls.getMethod(reqFuncName,Object.class);
			Object arr = meth.invoke(cls.newInstance(), parameter);
			if(arr!=null && arr instanceof ArrayList)
				csMsg.setObjs((ArrayList<Object>) arr);
			else
				throw new Exception();
		} catch (SecurityException |IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
			if(e.getMessage()==null && e.getCause().getMessage()==null)
				e.printStackTrace();//<------------FIX
			throw e;
		}
	}
	
	/**
	 * Finds {@link Class} in the path: {@code classPath+className+"Controller"}
	 * @param className is the {@link Class} name starting with <b>'.'</b>.
	 * @param classPath is the {@code package} name
	 * @return the {@link Class} object associated with the class or 
	 * interface with the given string name {@code classPath+className+"Controller"}.
	 * @throws Exception {@link Class#forName(String)}
	 */
	private static Class<?> findHandleGetFunc(String className, String classPath) throws Exception {
		try {
			return Class.forName(classPath+className+"Controller");
		} catch (Exception e) {
			return null;
			//throw e;
		}
	}
	
	/**
	 * Finds {@link Class} between all the {@code controllers packages: itayNron, izhar etc.}
	 *@deprecated In final project, all controllers will be at {@code controllers package}. Use {@link #findHandleGetFunc(String, String)} instead. 
	 *@param csMsg the request {@link Object}
	 *@return the {@link Class} object associated with the class or 
	 * interface with the given string name {@code classPath+className+"Controller"}.
	 * @throws Exception {@link #findHandleGetFunc(String, String)}
	 */
	private static Class<?> getObjectClass(CSMessage csMsg) throws Exception{
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
