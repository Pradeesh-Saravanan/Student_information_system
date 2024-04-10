package com.example.sis;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StudentController {
    @FXML
    private Label welcomeText;

    @FXML
    public void loginAdmin(){

        String sql = "SELECT * FROM admin WHERE username = ? and password = ?";

        connect = database.connectDb();

        try{ // IT WORKS GOOD : ) NOW LETS DESIGN THE DASHBOARD FORM : )
            Alert alert;

            prepare = connect.prepareStatement(sql);
            prepare.setString(1, username.getText());
            prepare.setString(2, password.getText());

            result = prepare.executeQuery();
//            CHECK IF FIELDS ARE EMPTTY
            if(username.getText().isEmpty() || password.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }else{
                if(result.next()){
//                    THEN PROCEED TO DASHBOARD FORM

                    getData.username = username.getText();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Login!");
                    alert.showAndWait();

//                    TO HIDE THE LOGIN FORM
                    loginBtn.getScene().getWindow().hide();
                    //LINK YOUR DASHBOARD
                    Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));

                    Stage stage = new Stage();
                    Scene scene = new Scene(root);

//                    root.setOnMousePressed((MouseEvent event) ->{
//                        x = event.getSceneX();
//                        y = event.getSceneY();
//                    });
//
//                    root.setOnMouseDragged((MouseEvent event) ->{
//                        stage.setX(event.getScreenX() - x);
//                        stage.setY(event.getScreenY() - y);
//                    });

                    stage.initStyle(StageStyle.TRANSPARENT);

                    stage.setScene(scene);
                    stage.show();

                }else{
                    // THEN ERROR MESSAGE WILL APPEAR
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong Username/Password");
                    alert.showAndWait();
                }
            }
        }catch(Exception e){e.printStackTrace();}

    }
//    protected void onHelloButtonClick() {
//        welcomeText.setText("Welcome to JavaFX Application!");
//    }

}