package Controller;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

public class EditProfileController {
    
    @FXML
    TextField firstnameTextField = new TextField();

    @FXML 
    PasswordField passwordField = new PasswordField();

    PasswordField cpasswordField = new PasswordField();

    @FXML
    TextField lastnameTextField = new TextField();

    @FXML
    DatePicker birthDatePicker = new DatePicker();

    @FXML
    TextField usernameTextField = new TextField();

    @FXML
    RadioButton maleRadio = new RadioButton(),femaleRadio = new RadioButton();

    @FXML
    Button editButton;

    void editValues(ActionEvent event) throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.cj.jdbc.Driver");  
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studcrud","root","");
        PreparedStatement stmt;
        ResultSet rs;
        if(usernameTextField.getText().equals(null)){

        }else{
            stmt = con.prepareStatement("UPDATE studcrud.user SET username = ? WHERE user.id = ?");
            stmt.setString(1, usernameTextField.getText());
            stmt.executeUpdate();
        }

        if (firstnameTextField.getText().equals(null)) {
            
        }else{
            stmt = con.prepareStatement("UPDATE studcrud.user SET first_name = ? WHERE user.id = ?");
            stmt.setString(1, firstnameTextField.getText());
            stmt.executeQuery()
        }
        if (passwordField.getText().equals(null)) {
            
        }else{
            if (passwordField.getText().equals(cpasswordField.getText())) {
                stmt = con.prepareStatement("UPDATE studcrud.user SET password = ? WHERE user.id = ? ")
            }
            
        }
    }
}
