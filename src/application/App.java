package application;
	
import java.io.InputStream;

import application.controllers.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class App extends Application {
  private BorderPane root = new BorderPane();
  private Stage primaryStage;
	
  @Override
	public void start(Stage primaryStage) {
		try {
			Scene scene = new Scene(navigateToHome());
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Text Editor");
			primaryStage.show();
			this.primaryStage = primaryStage;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
  
  public Stage getStage() {
    return primaryStage;
  }
	
	public Parent navigateToHome() {
	  try {
	    HomeController controller = (HomeController) setScene("./views/HomeView.fxml");
	    controller.setContext(this);
	  }
	  catch(Exception e) {
	    System.out.println(e.getMessage());
	  }
	  
	  return root;
	}
	
	public Initializable setScene(String page) throws Exception {
    FXMLLoader loader = new FXMLLoader();
    loader.setBuilderFactory(new JavaFXBuilderFactory());
    loader.setLocation(getClass().getResource(page));
    InputStream in = getClass().getResourceAsStream(page);
    
    Parent node;
    
    try {
      node = (Parent) loader.load(in);
    }
    finally {
      in.close();
    }
    root.setCenter(node);
    
    return (Initializable) loader.getController();
  }
	
	public static void main(String[] args) {
		launch(args);
	}
}
