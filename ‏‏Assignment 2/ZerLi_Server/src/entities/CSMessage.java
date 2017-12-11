package entities;

import java.io.Serializable;
import java.util.ArrayList;

public class CSMessage implements Serializable{
	private MessageType type;
	private ArrayList<Object> objs;
	private Object CSController;
	
	/**To know what casting to do - the class of the sended object*/
	private Class<?> clasz;
	
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
		this.objs = objs;
	}

	
	public Object getCSController() {
		return CSController;
	}

	public void setCSController(Object cSController) {
		CSController = cSController;
	}

	@Override
	public String toString() {
		return "CSMessage [type=" + type + ", objs=" + objs + ", CSController=" + CSController + "]";
	}

	public Class<?> getClasz() {
		return clasz;
	}

	public void setClasz(Class<?> clasz) {
		this.clasz = clasz;
	}
}
