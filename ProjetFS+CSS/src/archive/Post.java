package archive;



import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;



/*public class Post implements Initializable{

    @FXML
    ImageView post_icon;
    
    @FXML
    Label usernameLabel = new Label();

    @FXML
    Label credentialsLabel = new Label();

    @FXML
    Text textText;

    @FXML
    Label titleLabel;

    @FXML
    VBox postVBox;

    public AnchorPane root;

    PreparedStatement stmt;
    ResultSet rs;

    public Post(int id) throws SQLException, ClassNotFoundException{
        System.out.println("contructor executes");
        try {
            this.root = FXMLLoader.load(getClass().getResource("../Views/HomeView.fxml"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //showUserAtrributes(id);
    }

    ResultSet showUserAtrributes(int id) throws SQLException, ClassNotFoundException{
        System.out.println("show user executes");
        Class.forName("com.mysql.cj.jdbc.Driver");  
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/studcrud","root","");
        stmt = con.prepareStatement("SELECT * from studcrud.post WHERE id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        int publisher_id = 0;
        while (rs.next()) {
            //titleLabel.setText(rs.getString("title"));
            publisher_id= rs.getInt("publisher_id");
            System.out.println("value of post: " + id +"\n"+ "value of publisher: " + publisher_id);
            break;
        }

        stmt = con.prepareStatement("SELECT * from studcrud.user WHERE id = ? ");
        stmt.setInt(1, publisher_id);
        rs = stmt.executeQuery();

        while (rs.next()) {
            usernameLabel.setText(rs.getString("username"));
            System.out.println("username: "+rs.getString("username"));
            credentialsLabel.setText(rs.getString("credentials"));
            System.out.println("cerdentials: "+rs.getString("credentials"));

        }

        return rs;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        
        
    }
}*/

