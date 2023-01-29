package Controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import java.sql.Blob;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.PasswordField;
import javafx.scene.Node;
import Classes.Session;

public class EditProfileController implements Initializable{
    
    @FXML 
    Circle circle = new Circle();
    @FXML
    TextField firstnameTextField = new TextField();

    @FXML 
    PasswordField passwordField = new PasswordField();

    @FXML 
    TextField credentialsField = new TextField();

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

    @FXML
    Button profile_picButton = new Button();
    
    @FXML
    ImageView profile_picImageView = new ImageView();

    FileInputStream fileInputStream;
    
    Session session;

    EventHandler<MouseEvent> event;

    @FXML
    void editValues(ActionEvent event) throws ClassNotFoundException, SQLException, IOException{
        Class.forName("com.mysql.cj.jdbc.Driver");  
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studcrud","root","");
        PreparedStatement stmt;
        if (credentialsField.getText().equals("") == false) {
            stmt = con.prepareStatement("UPDATE studcrud.user SET credentials = ? WHERE user.id = ?");
            stmt.setString(1, credentialsField.getText());
            stmt.setInt(2, session.id);
            stmt.executeUpdate();
        }
        if(usernameTextField.getText().equals("") == false){
            stmt = con.prepareStatement("UPDATE studcrud.user SET username = ? WHERE user.id = ?");
            stmt.setString(1, usernameTextField.getText());
            stmt.setInt(2, session.id);
            stmt.executeUpdate();
            
        }

        if (firstnameTextField.getText().equals("") == false) {
            stmt = con.prepareStatement("UPDATE studcrud.user SET first_name = ? WHERE user.id = ?");
            stmt.setString(1, firstnameTextField.getText());
            stmt.setInt(2, session.id);
            stmt.executeUpdate();
        }

        if (passwordField.getText().equals("") == false) {
            stmt = con.prepareStatement("UPDATE studcrud.user SET password = ? WHERE user.id = ? ");
            stmt.setString(1, passwordField.getText());
            stmt.setInt(2, session.id);
            stmt.executeUpdate();
        }

        if (lastnameTextField.getText().equals("") == false) {
            stmt = con.prepareStatement("UPDATE studcrud.user SET last_name = ? WHERE user.id = ?");
            stmt.setString(1, lastnameTextField.getText());
            stmt.setInt(2, session.id);
            stmt.executeUpdate();
        }
        if(birthDatePicker.getValue() != null){
            
            stmt = con.prepareStatement("UPDATE studcrud.user SET date_of_birth = ? WHERE user.id = ?");
            stmt.setString(1, dateFormatter());
            stmt.setInt(2, session.id);
            stmt.executeUpdate();
        }
        if (maleRadio.isSelected()) {
            stmt = con.prepareStatement("UPDATE studcrud.user SET gender = ? WHERE user.id = ?");
            stmt.setString(1, "male");
            stmt.setInt(2, session.id);
            stmt.executeUpdate();
        }else if(femaleRadio.isSelected()){
            stmt = con.prepareStatement("UPDATE studcrud.user SET gender = ? WHERE user.id = ?");
            stmt.setString(1, "female");
            stmt.setInt(2, session.id);
            stmt.executeUpdate();
        }

        if (profile_picImageView.getImage() != null) {
            stmt = con.prepareStatement("UPDATE studcrud.user SET profile_pic = ? WHERE user.id = ?");
            stmt.setBinaryStream(1, fileInputStream, fileInputStream.available());
            stmt.setInt(2, session.id);
            stmt.executeUpdate();
            System.out.println("length of stream 1:   " + fileInputStream.available());
            System.out.println("length of stream 2:   " + fileInputStream.available());
            System.out.println("length:   " + fileInputStream.available());
            System.out.println("photo changeed probably");
        }
        else{
            System.out.println("photo not changed");
        }

    
    }
  

    public String dateFormatter(){       
        LocalDate birthdate = birthDatePicker.getValue();
        String formattedBD = birthdate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return formattedBD;   
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        session = new Session(HomeController.session.username, HomeController.session.password);
        session.id = HomeController.session.id;
        createEventHandler();
        try {
            createPromptText();
            initiateProfilePic();
        } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void createPromptText() throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.cj.jdbc.Driver");  
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studcrud","root","");
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM studcrud.user WHERE id = ?");
        stmt.setInt(1, session.id);

        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            usernameTextField.setPromptText(rs.getString("username"));
            firstnameTextField.setPromptText(rs.getString("first_name"));
            lastnameTextField.setPromptText(rs.getString("last_name"));
            passwordField.setPromptText(rs.getString("password"));
            birthDatePicker.setValue(rs.getDate("date_of_birth").toLocalDate());
            credentialsField.setPromptText(rs.getString("credentials"));
            if (rs.getString("gender") == "male") {
                maleRadio.setSelected(true);
            }else{
                femaleRadio.setSelected(true);
            }

        }
    }

    @FXML
    public void switchToHome(ActionEvent event) throws IOException{
        Parent root;
        Scene scene;
        Stage stage;

        root = FXMLLoader.load(getClass().getResource("../Views/HomeView.fxml"));
        stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        scene  = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../Stylesheets/HomeStyleSheet.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML 
    void openFileChooser(ActionEvent event) throws SQLException, ClassNotFoundException, IOException{
        FileChooser fileChooser = new FileChooser();
        Stage stage = (Stage)(((Node)event.getSource()).getScene().getWindow());
        File file = fileChooser.showOpenDialog(stage);
        fileInputStream = new FileInputStream(file);
        FileInputStream fileInputStream2 = new FileInputStream(file);
        profile_picImageView.setImage(new Image(fileInputStream2)); 
        System.out.println("length of stream 1:   " + fileInputStream.available());
        System.out.println("length of stream 2:   " + fileInputStream.available());
  
    }

    void initiateProfilePic() throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.cj.jdbc.Driver");  
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studcrud","root","");
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM studcrud.user where id = ?");
        stmt.setInt(1, session.id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Blob blob = rs.getBlob("profile_pic");
            InputStream input = blob.getBinaryStream();
            circle.setFill(new ImagePattern(new Image(input)));
        }
    }

    void createEventHandler(){
        event = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // TODO Auto-generated method stub
                if(event.getSource() == circle){
                    FileChooser fileChooser = new FileChooser();
                    Stage stage = (Stage)(((Node)event.getSource()).getScene().getWindow());
                    File file = fileChooser.showOpenDialog(stage);
                    try {
                        fileInputStream = new FileInputStream(file);
                        FileInputStream fileInputStream2 = new FileInputStream(file);
                        circle.setFill(new ImagePattern(new Image(fileInputStream2)));
                        System.out.println("length of stream 1:   " + fileInputStream.available());
                        System.out.println("length of stream 2:   " + fileInputStream.available());
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    
                }
            }
        };
        circle.setOnMouseClicked(event);
       }

       
    }
