package Classes;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.cj.x.protobuf.MysqlxSql.StmtExecute;

import Controller.HomeController;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Post extends VBox{

    Circle circle = new Circle();
    ImageView profile_image;
    Label username = new Label("username");
    Label credentials = new Label("credentials");
    Label title = new Label("title");
    ImageView postimage;
    Text posttext = new Text();
    HBox bottom_hbox = new HBox();
    public LikeButton likeButton = new LikeButton();
    Label numberOfLikes = new Label("Number of likes");
    public int post_id;

    VBox inner_vbox;
    HBox hbox;
    static public EventHandler<MouseEvent> handler;  


    void setId(){
        profile_image.setId("profile_image");
        username.setId("usernamee");
        credentials.setId("credentials");
        title.setId("title");
        posttext.setId("posttext");
        inner_vbox.setId("inner_vbox");
        hbox.setId("hbox");
        this.setId("post");
    }
  
    public Post(int id) throws ClassNotFoundException, SQLException{
        this.post_id = id;
        fetchUserRow(id);
        fetchPostRow(id);
        resizeProfileImage();
        
        createLayout();
        setId();
        
        likeButton.setGraphic(new ImageView(new Image("C:/Users/Bentouhami/Documents/3rd year/Chraibi/Projet fin de Semestre/ProjetFS+CSS/src/Icons/notliked.png")));
        createEventHandler();
        updateNumberOfLikes();
        setStyle();
    }


    void fetchUserRow(int id) throws SQLException, ClassNotFoundException{
        Class.forName("com.mysql.cj.jdbc.Driver");  
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studcrud","root","");
        PreparedStatement stmt = con.prepareStatement("select * from studcrud.post where id = ? ");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        int publisher_id = 0;
        while (rs.next()) {
            publisher_id = rs.getInt("publisher_id");
            break;
        }
        
        stmt = con.prepareStatement("SELECT * FROM studcrud.user where id = ?");
        stmt.setInt(1, publisher_id);
        rs = stmt.executeQuery();
        while(rs.next()){
            username.setText(rs.getString("username"));
            credentials.setText(rs.getString("credentials"));
            Blob blob = rs.getBlob("profile_pic");
            InputStream inputStream = blob.getBinaryStream();
            Image img = new Image(inputStream);
            circle.setFill(new ImagePattern(img));
            profile_image = new ImageView(img); 
            break;
        }
            
    }

    void fetchPostRow(int id) throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.cj.jdbc.Driver");  
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studcrud","root","");
        PreparedStatement stmt = con.prepareStatement("select * from studcrud.post where id = ? ");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            title.setText(rs.getString("title"));
            Blob blob = rs.getBlob("image");
            InputStream input = blob.getBinaryStream();
            Image img = new Image(input);
            postimage = new ImageView(img);
            resizePostImage();
            posttext.setText(rs.getString("text"));
            posttext.setWrappingWidth(450);
        }
        
    }


    void resizeProfileImage(){
        profile_image.setFitHeight(35);
        profile_image.setFitWidth(35);
        profile_image.setPreserveRatio(true);
    }

    void resizePostImage(){
        postimage.setFitHeight(400);
        postimage.setFitWidth(400);
        postimage.setPreserveRatio(true);
    }

    void createLayout(){
        this.setPrefHeight(0);
        this.setPrefWidth(500);

        inner_vbox = new VBox();
        inner_vbox.getChildren().addAll(username,credentials);
        
        hbox = new HBox();
        hbox.getChildren().addAll( profile_image,inner_vbox);

        bottom_hbox.getChildren().addAll(likeButton,numberOfLikes);
       
        this.getChildren().addAll(hbox,title,posttext,postimage,bottom_hbox);

    }

    void createEventHandler() throws SQLException,ClassNotFoundException{
        handler = new EventHandler<MouseEvent>() {  
  
            @Override  
            public void handle(MouseEvent event) {  
                if(event.getSource() == likeButton){
                    if (likeButton.state == "unliked") {
                        try {
                            Class.forName("com.mysql.cj.jdbc.Driver");
                            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studcrud","root","");
                            PreparedStatement stmt = con.prepareStatement("INSERT INTO studcrud.likes(liked_by,post_liked) VALUES(?,?)");
                            stmt.setInt(1, HomeController.session.id);
                            stmt.setInt(2, post_id);
                            if(stmt.executeUpdate() == 1){
                                likeButton.state = "liked";
                                likeButton.setGraphic(new ImageView(new Image("C:/Users/Bentouhami/Documents/3rd year/Chraibi/Projet fin de Semestre/ProjetFS+CSS/src/Icons/liked.png")));
                                
                            }
                        } catch (ClassNotFoundException | SQLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }  
                    else{   
                        try {
                            Class.forName("com.mysql.cj.jdbc.Driver");
                            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studcrud","root","");
                            PreparedStatement stmt = con.prepareStatement("DELETE FROM studcrud.likes WHERE (liked_by = ? and post_liked = ?)");
                            stmt.setInt(1, HomeController.session.id);
                            stmt.setInt(2, post_id);
                            if (stmt.executeUpdate() == 1) {
                                likeButton.state = "unliked";
                                likeButton.setGraphic(new ImageView(new Image("C:/Users/Bentouhami/Documents/3rd year/Chraibi/Projet fin de Semestre/ProjetFS+CSS/src/Icons/notliked.png")));
                                
                            }
                        } catch (ClassNotFoundException | SQLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    try {
                        updateNumberOfLikes();
                    } catch (ClassNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    
                }
                else if (event.getSource() == circle) {
                    
                }
            }  
              
        };
        likeButton.setOnMouseClicked(handler);
    }

    void updateNumberOfLikes() throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studcrud","root","");
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM studcrud.likes WHERE post_liked = ?");
        stmt.setInt(1, post_id);
        ResultSet rs = stmt.executeQuery();
        int number = 0;
        while(rs.next()) {
            number ++;  
        }
        numberOfLikes.setText(number + " Likes");
        
    }
   
    void setStyle(){
        username.setStyle("-fx-text-fill: #5c00b9;");
        username.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 15));
        
        credentials.setStyle("-fx-underline: true; -fx-text-fill: #962dff;");

        title.setStyle("fx-font-family: Monaco;");
        title.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 15));
        
    }
}
