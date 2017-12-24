package izhar.gui;
/**
 * The Customer will choose between Immediate delivery or Pre-order. If pre-order has been chosen, the GUI will reveal the datepickers
 */
public class SelectDeliveryTimeGUI {

	private RadioButton rbOption = {Immediate,Pre-Order};
	private Button btnSelect = Select;
	private Datepicker dpDate;
	private Datepicker dpTime;
	private Button btnSend = Send;

	/**
	 * 
	 * @param order
	 */
	public void loadGUI(Order order) {
		// TODO - implement SelectDeliveryTimeGUI.loadGUI
		throw new UnsupportedOperationException();
	}

	/**
	 * Will ask OrderController to load the correct GUI by selected option
	 * @param rbOption
	 */
	public void selectDeliveryType(RadioButton rbOption) {
		// TODO - implement SelectDeliveryTimeGUI.selectDeliveryType
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param dpDate
	 * @param dpTime
	 */
	public void selectDeliveryDateTime(Datepicker dpDate, Datepicker dpTime) {
		// TODO - implement SelectDeliveryTimeGUI.selectDeliveryDateTime
		throw new UnsupportedOperationException();
	}

	public void loadTimePickers() {
		// TODO - implement SelectDeliveryTimeGUI.loadTimePickers
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param time
	 */
	public boolean checkTimeUnder3Hours(DateTime time) {
		// TODO - implement SelectDeliveryTimeGUI.checkTimeUnder3Hours
		throw new UnsupportedOperationException();
	}

	public void showUnder3HoursErrMsg() {
		// TODO - implement SelectDeliveryTimeGUI.showUnder3HoursErrMsg
		throw new UnsupportedOperationException();
	}

}