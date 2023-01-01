import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;  
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;  
import javafx.stage.Stage;  
import Classes.Post;
public class App extends Application {  
    
    @Override  
    public void start(Stage primaryStage) throws Exception {  
        // TODO Auto-generated method stub  
         
        Pane root;
        root = FXMLLoader.load(getClass().getResource("Views/LoginView.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("Stylesheets/LoginStylesheet.css").toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        /*
        
        Post root = new Post(2);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show(); */

    }  
    public static void main(String[] args) {  
        launch(args);  
        

    }
}  
