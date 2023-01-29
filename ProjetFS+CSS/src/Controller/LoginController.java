package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Classes.Session;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginController{

    @FXML
    Hyperlink Singin;

    @FXML
    private TextField usernameTextField = new TextField();

    @FXML
    private PasswordField passwordField = new PasswordField();
    
    @FXML
    Label incorrectUsername = new Label();

    @FXML
    Label incorrectPassword = new Label();

    @FXML
    Button loginButton = new Button();

    public static Session session;

    

    @FXML
    void switchToSignin(ActionEvent event) throws Exception{
        
        Stage stage;
        Scene scene;
        Parent root;   

        root = FXMLLoader.load(getClass().getResource("../Views/SigninView.fxml"));
        stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        scene  = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../Stylesheets/SigninStylesheet.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        
    }

    @FXML
    void switchToHome(ActionEvent event) throws ClassNotFoundException, SQLException, IOException{
        if(authentification() == true){
            Stage stage;
            Scene scene;
            Parent root;   

            root = FXMLLoader.load(getClass().getResource("../Views/HomeView.fxml"));
            stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
            scene  = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("../Stylesheets/HomeStyleSheet.css").toExternalForm());
            stage.setScene(scene);
            stage.show();   
            
        }
    }

    Boolean authentification() throws ClassNotFoundException, SQLException{
        incorrectPassword.setVisible(false);
        incorrectUsername.setVisible(false);
        Boolean connect = false;
        Class.forName("com.mysql.cj.jdbc.Driver");  
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studcrud","root","");
        PreparedStatement stmt = con.prepareStatement("SELECT * from studcrud.user WHERE username = ?");
        stmt.setString(1, usernameTextField.getText());
        
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            TranslateTransition transition = new TranslateTransition();
            transition.setToX(10);
            transition.setDuration(Duration.seconds(0.05));
            transition.setAutoReverse(true);
            transition.setNode(usernameTextField);
            transition.setCycleCount(10);
            transition.play();
            incorrectUsername.setVisible(true);

        }
        rs = stmt.executeQuery();
        while(rs.next()){
            if (rs.getString("username").equals(usernameTextField.getText())) {
                
                if(rs.getString("password").equals(passwordField.getText())){
                    connect = true;
                    session = new Session(rs.getString("username"),rs.getString("password"));
                    session.id = rs.getInt("id");
                    HomeController.session_type = "login";
                    break;
                }else{
                    TranslateTransition transition = new TranslateTransition();
                    transition.setToX(10);
                    transition.setDuration(Duration.seconds(0.05));
                    transition.setAutoReverse(true);
                    transition.setNode(passwordField);
                    transition.setCycleCount(10);
                    transition.play();
                    incorrectPassword.setVisible(true);
                }
            } 
        } 
        return connect;
    }
}
