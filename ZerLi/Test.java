import controllers.LoginController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Test extends Application{
	public static void main( String args[] ) throws Exception
	   { 
		launch(args);		
	  } // end main

	@Override
	public void start(Stage primaryStage) throws Exception {
		LoginController lc = new LoginController();
		lc.start(primaryStage);
		
	}
	
}
