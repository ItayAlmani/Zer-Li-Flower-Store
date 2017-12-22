package gui.controllers;
public abstract class ViewItemsGUI {

	private ImageView[] imgImages;
	private Label[] lblNames;
	private Label[] lblPrices;

	public void loadGUI() {
		// TODO - implement ViewItemsGUI.loadGUI
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param items
	 */
	public abstract void loadItemsToGUI(Item[] items);

}