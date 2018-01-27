package common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Object that will use to communicate between the Client and the Server.
 * Implements <code>Serializable</code> so could be sent by Socket
 */
public class CSMessage implements Serializable{
	private static final long serialVersionUID = 3L;
	
	/** Type of the request/respond of Client/Server */
	public enum MessageType {
		UPDATE, SELECT, DBData,Exception,SetDB, DBStatus, INSERT;
	}
	
	/** Will categorize the type of the message by <code>MessageType</code> enum */
	private MessageType type;
	
	/**
	 * <p><b>As request:</b> Can be null (for example when requesting SetDB), 
	 * and can include the function name (for example when requesting SELECT), 
	 * and can include the function's name and parameters (depends on the function),
	 * and maybe another options.</p>
	 * 
	 * <p><b>As respond:</b> Can include one parameter (for example boolean for UPDATE or INSERT respond), 
	 * and can include many Objects (for example respond to SELECT query),
	 * and maybe another options.</p>
	 */
	private ArrayList<Object> objs;
	
	/**To know what casting to do - the class of the responded object(s)*/
	private Class<?> clasz = null;
	
	public CSMessage(MessageType type, ArrayList<Object> objs) {
		super();
		this.type = type;
		this.objs = objs;
		this.clasz=null;
	}
	
	public CSMessage(MessageType type, ArrayList<Object> objs, Class<?> clasz) {
		super();
		this.type = type;
		this.objs = objs;
		this.clasz=clasz;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public ArrayList<Object> getObjs() {
		return objs;
	}

	public void setObjs(ArrayList<Object> objs) {
		this.objs = new ArrayList<>();
		for (Object o : objs)
			this.objs.add(o);
	}
	
	public Class<?> getClasz() {
		return clasz;
	}

	public void setClasz(Class<?> clasz) {
		this.clasz = clasz;
	}
}
