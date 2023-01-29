package Controller;
import Classes.*;
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
import java.util.logging.Handler;

import Classes.Post;
import Classes.Session;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.*;
public class HomeController implements Initializable{
    
    @FXML 
    Circle circle = new Circle();
    
    @FXML
    VBox feedVBox = new VBox(50);
    
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
    ImageView bigprofile = new ImageView();

    @FXML
    TextField titleTextField = new TextField();
    
    @FXML 
    TextArea textTextArea = new TextArea();

    @FXML
    Button submitButton;

    @FXML 
    Button profilebutton;

    @FXML
    Button showMyPostsButton = new Button();

    @FXML 
    Button disconnectButton = new Button();
    
    ArrayList<Integer> post_order = new ArrayList<Integer>();
    ArrayList<Post> postlist = new ArrayList<Post>();

    public static Session session ;
    public static String session_type ;
    Connection con;

    EventHandler<MouseEvent> event;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {     
        try {
            createEventHandler();
            chooseValidIndexes();
            createPostList();
            
            feedVBox.getChildren().addAll(postlist);
           
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
            getLikedPosts();
            
            
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
        
        Image img = bigprofile.getImage();
        ImagePattern imgp = new ImagePattern(img);
        if (!imgp.getImage().equals(imgp)) {
            System.out.println("the image isn't null");
        }
        
        circle.setFill(imgp);
        profilebutton.setShape(circle);
        circle.setEffect(new DropShadow(10d,0d,0d,Color.RED));
        circle.setStroke(Color.RED);
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
        circle.setFill(new ImagePattern(img));
        bigprofile.setImage(img);
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

  

   void getLikedPosts() throws ClassNotFoundException, SQLException{
    Class.forName("com.mysql.cj.jdbc.Driver");  
    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studcrud","root","");
    PreparedStatement stmt = con.prepareStatement("SELECT * FROM studcrud.likes where liked_by = ?");
    stmt.setInt(1, session.id);
    ResultSet rs = stmt.executeQuery();
    System.out.println("posts liked");
    ArrayList<Integer> liked_posts = new ArrayList<Integer>();
    while (rs.next()) {
        liked_posts.add(rs.getInt("post_liked"));
    }
    for (Integer integer : liked_posts) {
        System.out.println("post : " + integer);
    }
    tagLikedPosts(liked_posts);

   }

   void tagLikedPosts(ArrayList<Integer> liked_posts){
        for (int i = 0; i < liked_posts.size(); i++) {
            for (int j = 0; j < postlist.size(); j++) {
                if (liked_posts.get(i) == (postlist.get(j).post_id)) {
                    postlist.get(j).likeButton.setGraphic(new ImageView(new Image("C:/Users/Bentouhami/Documents/3rd year/Chraibi/Projet fin de Semestre/ProjetFS+CSS/src/Icons/liked.png")));
                    postlist.get(j).likeButton.state = "liked";
                    System.out.println("liked post id:" + postlist.get(j).post_id );
                }
            }
        }
   }

   /*
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
   } */
   @FXML
   void switchToLogin(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("../Views/LoginView.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../Stylesheets/LoginStylesheet.css").toExternalForm());
        stage.setScene(scene);
        stage.show(); 
   }
   
   
   void createEventHandler(){
    event = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            // TODO Auto-generated method stub
            if(event.getSource() == circle){
                Stage stage;
                Scene scene;
                Parent root;   

                try {
                    root = FXMLLoader.load(getClass().getResource("../Views/EditProfile.fxml"));
                    stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
                    scene  = new Scene(root);
                    scene.getStylesheets().add(getClass().getResource("../Stylesheets/EditProfileStylesheet.css").toExternalForm());
                    stage.setScene(scene);
                    stage.show();
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
