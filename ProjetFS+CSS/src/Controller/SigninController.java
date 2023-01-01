package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.mysql.cj.xdevapi.Result;

import Classes.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.*;
public class SigninController {
    
    @FXML
    TextField firstnameTextField = new TextField();

    @FXML 
    PasswordField PasswordField = new PasswordField();

    @FXML
    TextField lastnameTextField = new TextField();

    @FXML
    DatePicker birthDatePicker = new DatePicker();

    @FXML
    TextField usernameTextField = new TextField();

    @FXML
    RadioButton maleRadio = new RadioButton(),femaleRadio = new RadioButton();

    @FXML
    Button signinButton;

    public static Session session;
    
    @FXML
    void Register(ActionEvent event) throws ClassNotFoundException, SQLException, IOException{
        Class.forName("com.mysql.cj.jdbc.Driver");  
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studcrud","root","");
        PreparedStatement stmt = con.prepareStatement("INSERT INTO studcrud.user(username, first_name, last_name, password, gender, date_of_birth) VALUES(?,?,?,?,?,?)");

        stmt.setString(1, usernameTextField.getText());
        stmt.setString(2, firstnameTextField.getText());
        stmt.setString(3, lastnameTextField.getText());
        stmt.setString(4, PasswordField.getText());
        stmt.setString(5, getRadio(event));
        stmt.setString(6, dateFormatter());
        
        int rs = stmt.executeUpdate();
        if(rs == 1){
            stmt = con.prepareStatement("SELECT * FROM studcrud.user WHERE username = ?");
            stmt.setString(1, usernameTextField.getText());
            ResultSet result = stmt.executeQuery();
            session = new Session(usernameTextField.getText(), PasswordField.getText());
            while (result.next()) {
                session.id = result.getInt("id");
            }
            HomeController.session_type = "signin";
            System.out.println("hioiiiioiioiio");
            switchToHome(event);
            
        }
        System.out.println(rs);
        
    }

    String getRadio(ActionEvent event){
        if (femaleRadio.isSelected()) {
            return "female";
        }
        else{
            return "male";
        }
    }

    public String dateFormatter(){
        LocalDate birthdate = birthDatePicker.getValue();
        String formattedBD = birthdate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return formattedBD;
    }

    void switchToHome(ActionEvent event) throws IOException{
        Stage stage;
        Scene scene;
        Pane root;   

        root = FXMLLoader.load(getClass().getResource("../Views/HomeView.fxml"));
        stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        
        scene  = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../Stylesheets/HomeStylesheet.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void switchToLogin(ActionEvent event) throws IOException{
        Stage stage;
        Scene scene;
        Pane root;   

        root = FXMLLoader.load(getClass().getResource("../Views/LoginView.fxml"));
        stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        
        scene  = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../Stylesheets/LoginStylesheet.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
