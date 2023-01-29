package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class randomview implements Initializable{
    @FXML 
    Circle circle = new Circle();
    
    @FXML
    ImageView Image;

    @FXML
    Button button;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        Image img = new Image("Images/login_background.jpg");
        Image = new ImageView(img);
        
        circle.setFill(new ImagePattern(img));
        Circle cir = new Circle(1.5);
        cir.setFill(new ImagePattern(img));
        button.setShape(cir);
        ImageView ima = new ImageView(img);
        ima.setFitHeight(150);
        ima.setFitWidth(150);
        button.setGraphic(ima);
        
        
    }
    
}
