package Controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.ResourceBundle;

import Classes.Post;
import Classes.Session;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.*;
public class HomeController implements Initializable{
    
    @FXML
    VBox feedVBox = new VBox();
    
    @FXML
    Button createpostButton = new Button();

    @FXML
    AnchorPane anchorPane = new AnchorPane();

    @FXML 
    VBox createpostVBox = new VBox();

    @FXML 
    Label usernameLabel = new Label();
    
    @FXML
    ImageView pcImage;

    @FXML
    ImageView bigprofile;

    @FXML
    TextField titleTextField = new TextField();
    
    @FXML 
    TextArea textTextArea = new TextArea();

    @FXML
    Button submitButton;

    ArrayList<Integer> post_order = new ArrayList<Integer>();
    static ArrayList<Post> postlist = new ArrayList<Post>();

    public static Session session;
    public static String session_type ;
    Connection con;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       
        try {
            
            chooseValidIndexes();
            createPostList();
            
            
            feedVBox.getChildren().addAll(postlist);

            //menuHBox.setId("menuHBox");
            feedVBox.setId("feedVBox");
            if (session_type == "signin") {
                session = SigninController.session;
                System.out.println("signin");
                System.out.println(session.username);
            }else{
                session = LoginController.session;
                System.out.println("login session");
                System.out.println(session.username);
            }
            setbigprofile();

            
            
        } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    int getMinId() throws SQLException, ClassNotFoundException{
        int min = -1;
        Class.forName("com.mysql.cj.jdbc.Driver");  
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studcrud","root","");
        PreparedStatement stmt = con.prepareStatement("SELECT MIN(id) FROM studcrud.post");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {

            min = rs.getInt("Min(id)");
        }
        
        return min;
    }

    int getMaxId() throws SQLException, ClassNotFoundException{
        int max = -1;
        Class.forName("com.mysql.cj.jdbc.Driver");  
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studcrud","root","");
        PreparedStatement stmt = con.prepareStatement("SELECT MAX(id) FROM studcrud.post");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            max = rs.getInt("Max(id)");
        }
        
        return max;
    }
    
    void Shuffle() throws ClassNotFoundException, SQLException{
        Iterator iter = post_order.iterator();
        //ATTENTION 
        post_order.add(null);
        int first_index = getMinId();
        int last_index = getMaxId();
        
        while(iter.hasNext()){
            post_order.add(first_index);
            first_index++;
            if (first_index > last_index) {
                break;
            }
        }
        
        post_order.remove(null);
        Collections.shuffle(post_order);
        
    }
    
    void chooseValidIndexes() throws ClassNotFoundException, SQLException{
        
        Class.forName("com.mysql.cj.jdbc.Driver");  
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studcrud","root","");
        PreparedStatement stmt;
        ResultSet rs;
        
        Shuffle();
        for (int i = 0; i < post_order.size(); i++) {
            
            stmt = con.prepareStatement("SELECT * from studcrud.post WHERE id = ?");
            stmt.setInt(1,post_order.get(i));
            rs = stmt.executeQuery();
            if (rs.next()) {
                
            }else{
                
                post_order.remove(post_order.get(i));
                i--;
            }
        }
        
    }

    void createPostList() throws ClassNotFoundException, SQLException{
        
        for (int i = 0; i < post_order.size(); i++) {
            postlist.add(new Post(post_order.get(i)));
            
        }
        
        
    }

    @FXML
    void createPost(ActionEvent event) throws IOException, SQLException, ClassNotFoundException{
        
        createpostVBox.setVisible(true);
        fetchUserRow();
    }

    void resizePCImage(){
        
        pcImage.setFitHeight(30);
        pcImage.setFitWidth(30);
        pcImage.setPreserveRatio(true);
    }

    void resizeBigImage(){
        bigprofile.setFitHeight(100);
        bigprofile.setFitWidth(246);
        bigprofile.setPreserveRatio(true);
    }
    
   void setbigprofile() throws ClassNotFoundException, SQLException{
    Class.forName("com.mysql.cj.jdbc.Driver");  
    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studcrud","root","");
    PreparedStatement stmt = con.prepareStatement("SELECT * FROM studcrud.user where username = ?");
    stmt.setString(1, session.username);
    ResultSet rs = stmt.executeQuery();

    while (rs.next()) {
        Blob blob = rs.getBlob("profile_pic");
        InputStream inputStream = blob.getBinaryStream();
        Image img = new Image(inputStream);
        bigprofile.setImage(img);;
        break;
    }
   } 

   void fetchUserRow() throws ClassNotFoundException, SQLException{
    Class.forName("com.mysql.cj.jdbc.Driver");  
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studcrud","root","");
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM studcrud.user where username = ?");
        stmt.setString(1, session.username);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            usernameLabel.setText(rs.getString("username"));
            Blob blob = rs.getBlob("profile_pic");
            InputStream inputStream = blob.getBinaryStream();
            Image img = new Image(inputStream);
            pcImage.setImage(img);
            break;
        }
   }

   @FXML
   void updatePostTable(ActionEvent event) throws ClassNotFoundException, SQLException{
    Class.forName("com.mysql.cj.jdbc.Driver");  
    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studcrud","root","");
    PreparedStatement stmt = con.prepareStatement("INSERT INTO studcrud.post(title, text, publisher_id) VALUES(?,?,?)");
    stmt.setString(1, titleTextField.getText());
    stmt.setString(2, textTextArea.getText());
    stmt.setInt(3, session.id);

    
    if(stmt.executeUpdate() == 1){
        System.out.println("updated succes");
        createpostVBox.setVisible(false);
    }
        
    
        
   }
   
   @FXML
   void hideVBox(ActionEvent event){
        createpostVBox.setVisible(false);
   }

   @FXML
   void switchToEditProfile(ActionEvent event) throws IOException{
        Stage stage;
        Scene scene;
        Parent root;   

        root = FXMLLoader.load(getClass().getResource("../Views/EditProfile.fxml"));
        stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        scene  = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../Stylesheets/EditProfileStylesheet.css").toExternalForm());
        stage.setScene(scene);
        stage.show(); 
   }
}
