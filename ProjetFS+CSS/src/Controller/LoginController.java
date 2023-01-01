package Controller;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Classes.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.stage.Stage;

public class LoginController{

    @FXML
    Hyperlink Singin;

    @FXML
    TextField usernameTextField;

    @FXML
    PasswordField passwordField;

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
            scene.getStylesheets().add(getClass().getResource("../Stylesheets/HomeStylesheet.css").toExternalForm());
            stage.setScene(scene);
            stage.show();   
            
        }
    }

    Boolean authentification() throws ClassNotFoundException, SQLException{
        Boolean connect = false;
        Class.forName("com.mysql.cj.jdbc.Driver");  
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studcrud","root","");
        PreparedStatement stmt = con.prepareStatement("SELECT * from studcrud.user WHERE username = ?");
        stmt.setString(1, usernameTextField.getText());
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            if (rs.getString("username").equals(usernameTextField.getText())) {
                if(rs.getString("password").equals(passwordField.getText())){
                    connect = true;
                    session = new Session(rs.getString("username"),rs.getString("password"));
                    session.id = rs.getInt("id");
                    HomeController.session_type = "login";
                    break;
                }
            }    
        }
        return connect;
    }



}
