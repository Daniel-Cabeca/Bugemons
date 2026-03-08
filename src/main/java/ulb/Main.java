package ulb;
import javafx.application.Application;
import ulb.view.handler.WindowContainer;
import ulb.view.windows.MainWindow;

public class Main {
	public static void main(String[] args){
		WindowContainer windowContainer = new WindowContainer();
		Application.launch(MainWindow.class,args);
	}
}
