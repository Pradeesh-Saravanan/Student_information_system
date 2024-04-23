package com.example.demo;
import java.lang.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.sql.*;

//import static com.example.demo.database.connectDb;

//class database{
//    public static Connection connectDb(){
//        try {
////            Class.forName("com.mysql.jdbc.Driver");
//            Connection connect;
//            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/memory","root","Mysql@42");
//            return connect;
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }
//}
public class MyController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginBtn;

    @FXML
    public void loginAdmin() throws SQLException {
        String sql ="select * from admin where username=? and password =?";
        Connection connect;
        connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/memory","root","Mysql@42");
        try{
            Alert alert;
            PreparedStatement prepare = connect.prepareStatement(sql);
            prepare.setString(1,usernameField.getText());
            prepare.setString(2,passwordField.getText());
            ResultSet result = prepare.executeQuery();
            if(usernameField.getText().isEmpty() || passwordField.getText().isEmpty()){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Prepare fill all blank fields");
                alert.showAndWait();
            }
            else{
                if(result.next()){
                    getData.username = usernameField.getText();
                    alert =new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setContentText("Successfully login!");
                    alert.showAndWait();

                    loginBtn.getScene().getWindow().hide();
                    Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.setScene(scene);
                    stage.show();
                }
                else{
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong username or password");
                    alert.showAndWait();
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}