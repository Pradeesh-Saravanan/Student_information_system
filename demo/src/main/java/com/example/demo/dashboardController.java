/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;

import javax.imageio.ImageIO;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class dashboardController implements Initializable {
    @FXML
    private AnchorPane main_form;
    @FXML
    private Label username;
    @FXML
    private Button home_btn;
    @FXML
    private Button addStudents_btn;
    @FXML
    private Button availableCourse_btn;
    @FXML
    private Button studentGrade_btn;
    @FXML
    private Button logout;
    @FXML
    private AnchorPane home_form;
    @FXML
    private Label home_totalEnrolled;
    @FXML
    private Label home_totalFemale;
    @FXML
    private Label home_totalMale;
    @FXML
    private AnchorPane addStudents_form;
    @FXML
    private TableView<studentData> addStudents_tableView;
    @FXML
    private TableColumn<studentData, String> addStudents_col_studentNum;
    @FXML
    private TableColumn<studentData, String> addStudents_col_year;
    @FXML
    private TableColumn<studentData, String> addStudents_col_course;
    @FXML
    private TableColumn<studentData, String> addStudents_col_firstName;
    @FXML
    private TableColumn<studentData, String> addStudents_col_lastName;
    @FXML
    private TableColumn<studentData, String> addStudents_col_gender;
    @FXML
    private TableColumn<studentData, String> addStudents_col_birth;
    @FXML
    private TableColumn<studentData, String> addStudents_col_status;
    @FXML
    private TextField addStudents_studentNum;
    @FXML
    private ComboBox<String> addStudents_year;
    @FXML
    private ComboBox<String> addStudents_course;
    @FXML
    private TextField addStudents_firstName;
    @FXML
    private TextField addStudents_lastName;
    @FXML
    private DatePicker addStudents_birth;
    @FXML
    private ComboBox<String> addStudents_status;
    @FXML
    private ComboBox<String> addStudents_gender;
    @FXML
    private ImageView addStudents_imageView;
    @FXML
    private ImageView QRImage;
    @FXML
    private AnchorPane availableCourse_form;
    @FXML
    private TextField availableCourse_course;
    @FXML
    private TextField availableCourse_description;
    @FXML
    private TextField availableCourse_degree;
    @FXML
    private TableView<courseData> availableCourse_tableView;
    @FXML
    private TableColumn<courseData, String> availableCourse_col_course;
    @FXML
    private TableColumn<courseData, String> availableCourse_col_description;
    @FXML
    private TableColumn<courseData, String> availableCourse_col_degree;
    @FXML
    private AnchorPane studentGrade_form;
    @FXML
    private TextField studentGrade_studentNum;
    @FXML
    private TextField studentGrade_firstSem;
    @FXML
    private TextField studentGrade_secondSem;
    @FXML
    private TableView<studentData> studentGrade_tableView;
    @FXML
    private TableColumn<studentData, String> studentGrade_col_studentNum;
    @FXML
    private TableColumn<studentData, String> studentGrade_col_year;
    @FXML
    private TableColumn<studentData, String> studentGrade_col_course;
    @FXML
    private TableColumn<studentData, String> studentGrade_col_firstSem;
    @FXML
    private TableColumn<studentData, String> studentGrade_col_secondSem;
    @FXML
    private TableColumn<studentData, String> studentGrade_col_final;
    @FXML
    private TextField studentGrade_search;
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    private Image image;
    static int flag=0;
    private Image generateQRCodeImage(String data) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, com.google.zxing.BarcodeFormat.QR_CODE, 300, 300);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        byteArrayOutputStream.flush();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        return new Image(byteArrayInputStream);
    }
    public void homeDisplayTotalEnrolledStudents() {
        String sql = "SELECT COUNT(*) FROM student";
        connect = database.connectDb();
        int countEnrolled = 0;
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            if (result.next()) {
                countEnrolled = result.getInt("COUNT(*)");
            }
            home_totalEnrolled.setText(String.valueOf(countEnrolled));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void homeDisplayFemaleEnrolled() {
        String sql = "SELECT COUNT(*) FROM student WHERE gender = 'Female' and status = 'Enrolled'";
        connect = database.connectDb();
        try {
            int countFemale = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            if (result.next()) {
                countFemale = result.getInt("count(*)");
            }
            home_totalFemale.setText(String.valueOf(countFemale));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void homeDisplayMaleEnrolled() {
        String sql = "SELECT COUNT(*) FROM student WHERE gender = 'Male' and status = 'Enrolled'";
        connect = database.connectDb();
        try {
            int countMale = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            if (result.next()) {
                countMale = result.getInt("COUNT(*)");
            }
            home_totalMale.setText(String.valueOf(countMale));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addStudentsAdd() {
        String insertData = "INSERT INTO student " + "(studentNum,year,course,firstName,lastName,gender,birth,status,image) " + "VALUES(?,?,?,?,?,?,?,?,?)";
        connect = database.connectDb();
        try {
            Alert alert;
            if (addStudents_studentNum.getText().isEmpty() || addStudents_year.getSelectionModel().getSelectedItem() == null
                    || addStudents_course.getSelectionModel().getSelectedItem() == null || addStudents_firstName.getText().isEmpty()
                    || addStudents_lastName.getText().isEmpty() || addStudents_gender.getSelectionModel().getSelectedItem() == null
                    || addStudents_birth.getValue() == null || addStudents_status.getSelectionModel().getSelectedItem() == null
                    || getData.path == null || getData.path == "") {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                String checkData = "SELECT studentNum FROM student WHERE studentNum = '" + addStudents_studentNum.getText() + "'";
                statement = connect.createStatement();
                result = statement.executeQuery(checkData);
                if (result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Student #" + addStudents_studentNum.getText() + " was already exist!");
                    alert.showAndWait();
                } else {
                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, addStudents_studentNum.getText());
                    prepare.setString(2, (String) addStudents_year.getSelectionModel().getSelectedItem());
                    prepare.setString(3, (String) addStudents_course.getSelectionModel().getSelectedItem());
                    prepare.setString(4, addStudents_firstName.getText());
                    prepare.setString(5, addStudents_lastName.getText());
                    prepare.setString(6, (String) addStudents_gender.getSelectionModel().getSelectedItem());
                    addStudents_birth.setConverter(new StringConverter<LocalDate>() {
                        private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        @Override
                        public String toString(LocalDate date) {
                            if (date != null) {
                                return dateFormatter.format(date);
                            } else {
                                return "";
                            }
                        }
                        @Override
                        public LocalDate fromString(String string) {
                            if (string != null && !string.isEmpty()) {
                                return LocalDate.parse(string, dateFormatter);
                            } else {
                                return null;
                            }
                        }
                    });
                    prepare.setString(7, String.valueOf(addStudents_birth.getValue()));
                    prepare.setString(8, (String) addStudents_status.getSelectionModel().getSelectedItem());
                    String uri = getData.path;
                    uri = uri.replace("\\", "\\\\");
                    prepare.setString(9, uri);
                    prepare.executeUpdate();
                    String insertStudentGrade = "INSERT INTO student_grade " + "(studentNum,year,course,first_sem,second_sem,final) " + "VALUES(?,?,?,?,?,?)";
                    prepare = connect.prepareStatement(insertStudentGrade);
                    prepare.setString(1, addStudents_studentNum.getText());
                    prepare.setString(2, (String) addStudents_year.getSelectionModel().getSelectedItem());
                    prepare.setString(3, (String) addStudents_course.getSelectionModel().getSelectedItem());
                    prepare.setString(4, "0.0");
                    prepare.setString(5, "0.0");
                    prepare.setString(6, "0.0");
                    prepare.executeUpdate();
                    addStudentsShowListData();
                    addStudentsClear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addStudentsUpdate() {
        String uri = getData.path;
        uri = uri.replace("\\", "\\\\");
        String updateData = "UPDATE student SET " + "year = '" + addStudents_year.getSelectionModel().getSelectedItem()
                + "', course = '" + addStudents_course.getSelectionModel().getSelectedItem() + "', firstName = '" + addStudents_firstName.getText()
                + "', lastName = '" + addStudents_lastName.getText()
                + "', gender = '" + addStudents_gender.getSelectionModel().getSelectedItem() + "', birth = '" + addStudents_birth.getValue()
                + "', status = '" + addStudents_status.getSelectionModel().getSelectedItem() + "', image = '" + uri + "' WHERE studentNum = '"
                + addStudents_studentNum.getText() + "'";
        connect = database.connectDb();
        try {
            Alert alert;
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to UPDATE Student #" + addStudents_studentNum.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get().equals(ButtonType.OK)) {
                statement = connect.createStatement();
                statement.executeUpdate(updateData);
                addStudentsShowListData();
                addStudentsSelect();
                addStudentsClear();
            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addStudentsDelete() {
        String deleteData = "DELETE FROM student WHERE studentNum = '" + addStudents_studentNum.getText() + "'";
        connect = database.connectDb();
        try {
            Alert alert;
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to DELETE Student #" + addStudents_studentNum.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get().equals(ButtonType.OK)) {
                statement = connect.createStatement();
                statement.executeUpdate(deleteData);
                String checkData = "SELECT studentNum FROM student_grade " + "WHERE studentNum = '" + addStudents_studentNum.getText() + "'";
                prepare = connect.prepareStatement(checkData);
                result = prepare.executeQuery();
                if (result.next()) {
                    String deleteGrade = "DELETE FROM student_grade WHERE " + "studentNum = '" + addStudents_studentNum.getText() + "'";
                    statement = connect.createStatement();
                    statement.executeUpdate(deleteGrade);
                }
                addStudentsShowListData();
                addStudentsClear();
            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addStudentsClear() {
        addStudents_studentNum.setText("");
        addStudents_year.setValue("");
        addStudents_course.setValue("");
        addStudents_firstName.setText("");
        addStudents_lastName.setText("");
        addStudents_gender.setValue("");
        addStudents_birth.setValue(null);
        addStudents_status.setValue("");
        addStudents_imageView.setImage(null);
        QRImage.setImage(null);
        getData.path = "";
    }
    public void addStudentsInsertImage() {
        FileChooser open = new FileChooser();
        open.setTitle("Open Image File");
        open.getExtensionFilters().add(new ExtensionFilter("Image File", "*.jpg", "*.png"));
        File file = open.showOpenDialog(main_form.getScene().getWindow());
        if (file != null) {
            image = new Image(file.toURI().toString(), 120, 149, false, true);
            addStudents_imageView.setImage(image);
            getData.path = file.getAbsolutePath();
        }
    }
    public void addStudentsYearList() {
    }
    public void addStudentsCourseList() {
        String listCourse = "SELECT * FROM course";
        connect = database.connectDb();
        try {
            if(flag==0){
            prepare = connect.prepareStatement(listCourse);
            result = prepare.executeQuery();
            while (result.next()) {
                addStudents_course.getItems().add(result.getString("course"));
            }
            flag+=1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void addStudentsGenderList() {
    }
    private String[] statusList = {"Enrolled", "Not Enrolled", "Inactive"};
    public void addStudentsStatusList() {
    }
    public ObservableList<studentData> addStudentsListData() {
        ObservableList<studentData> listStudents = FXCollections.observableArrayList();
        String sql = "SELECT * FROM student";
        connect = database.connectDb();
        try {
            studentData studentD;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while (result.next()) {
                studentD = new studentData(result.getInt("studentNum"),
                        result.getString("year"),
                        result.getString("course"),
                        result.getString("firstName"),
                        result.getString("lastName"),
                        result.getString("gender"),
                        result.getDate("birth"),
                        result.getString("status"),
                        result.getString("image"));
                listStudents.add(studentD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listStudents;
    }
    private ObservableList<studentData> addStudentsListD;
    public void addStudentsShowListData() {
        addStudentsListD = addStudentsListData();
        addStudents_tableView.setItems(null);
        addStudents_col_studentNum.setCellValueFactory(new PropertyValueFactory<>("studentNum"));
        addStudents_col_year.setCellValueFactory(new PropertyValueFactory<>("year"));
        addStudents_col_course.setCellValueFactory(new PropertyValueFactory<>("course"));
        addStudents_col_firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        addStudents_col_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        addStudents_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        addStudents_col_birth.setCellValueFactory(new PropertyValueFactory<>("birth"));
        addStudents_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        addStudents_tableView.setItems(addStudentsListD);
    }
    public void addStudentsSelect() throws IOException, WriterException {
        studentData studentD = addStudents_tableView.getSelectionModel().getSelectedItem();
        int num = addStudents_tableView.getSelectionModel().getSelectedIndex();
        if ((num - 1) < -1) {
            return;
        }
        addStudents_studentNum.setText(String.valueOf(studentD.getStudentNum()));
        addStudents_firstName.setText(studentD.getFirstName());
        addStudents_lastName.setText(studentD.getLastName());
        addStudents_year.setValue(studentD.getYear());
        addStudents_course.setValue(studentD.getCourse());
        addStudents_gender.setValue(studentD.getGender());
        addStudents_status.setValue(studentD.getStatus());
        addStudents_birth.setValue(LocalDate.parse(String.valueOf(studentD.getBirth())));
        String uri = "file:" + studentD.getImage();
        image = new Image(uri, 120, 149, false, true);
        addStudents_imageView.setImage(image);
        getData.path = studentD.getImage();
        String data = "Reg no: " + addStudents_studentNum.getText() +
                "\nYear: '" + addStudents_year.getSelectionModel().getSelectedItem() + "'\nCourse = '" + addStudents_course.getSelectionModel().getSelectedItem()
                + "\nFirstName = '" + addStudents_firstName.getText() + "\nLastName = '" + addStudents_lastName.getText()
                + "\nGender = '" + addStudents_gender.getSelectionModel().getSelectedItem() + "\nBirth = '" + addStudents_birth.getValue()
                + "\nStatus = '" + addStudents_status.getSelectionModel().getSelectedItem();
        Image image2 = generateQRCodeImage(data);
        QRImage.setImage(image2);
    }
    public void availableCourseAdd() {
        String insertData = "INSERT INTO course (course,description,degree) VALUES(?,?,?)";
        connect = database.connectDb();
        try {
            Alert alert;
            if (availableCourse_course.getText().isEmpty() || availableCourse_description.getText().isEmpty()
                    || availableCourse_degree.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                String checkData = "SELECT course FROM course WHERE course = '" + availableCourse_course.getText() + "'";
                statement = connect.createStatement();
                result = statement.executeQuery(checkData);
                if (result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Course: " + availableCourse_course.getText() + " was already exist!");
                    alert.showAndWait();
                } else {
                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, availableCourse_course.getText());
                    prepare.setString(2, availableCourse_description.getText());
                    prepare.setString(3, availableCourse_degree.getText());
                    prepare.executeUpdate();
                    availableCourseShowListData();
                    availableCourseClear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void availableCourseUpdate() {
        String updateData = "UPDATE course SET description = '" + availableCourse_description.getText() + "', degree = '"
                + availableCourse_degree.getText() + "' WHERE course = '" + availableCourse_course.getText() + "'";
        connect = database.connectDb();
        try {
            Alert alert;
            if (availableCourse_course.getText().isEmpty()
                    || availableCourse_description.getText().isEmpty()
                    || availableCourse_degree.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE Course: " + availableCourse_course.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(updateData);
                    availableCourseShowListData();
                    availableCourseClear();
                } else {
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void availableCourseDelete() {
        String deleteData = "DELETE FROM course WHERE course = '" + availableCourse_course.getText() + "'";
        connect = database.connectDb();
        try {
            Alert alert;
            if (availableCourse_course.getText().isEmpty() || availableCourse_description.getText().isEmpty() || availableCourse_degree.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE Course: " + availableCourse_course.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(deleteData);
                    availableCourseShowListData();
                    availableCourseClear();
                } else {
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void availableCourseClear() {
        availableCourse_course.setText("");
        availableCourse_description.setText("");
        availableCourse_degree.setText("");
    }
    public ObservableList<courseData> availableCourseListData() {
        ObservableList<courseData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM course";
        connect = database.connectDb();
        try {
            courseData courseD;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while (result.next()) {
                courseD = new courseData(result.getString("course"),
                        result.getString("description"),
                        result.getString("degree"));
                listData.add(courseD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }
    private ObservableList<courseData> availableCourseList;
    public void availableCourseShowListData() {
        availableCourseList = availableCourseListData();
        availableCourse_col_course.setCellValueFactory(new PropertyValueFactory<>("course"));
        availableCourse_col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        availableCourse_col_degree.setCellValueFactory(new PropertyValueFactory<>("degree"));
        availableCourse_tableView.setEditable(true);
        availableCourse_tableView.setItems(availableCourseList);
    }
    public void availableCourseSelect() {
        courseData courseD = availableCourse_tableView.getSelectionModel().getSelectedItem();
        int num = availableCourse_tableView.getSelectionModel().getSelectedIndex();
        if ((num - 1) < -1) {
            return;
        }
        availableCourse_course.setText(courseD.getCourse());
        availableCourse_description.setText(courseD.getDescription());
        availableCourse_degree.setText(courseD.getDegree());
    }
    public void studentGradesUpdate() {
        float finalCheck1 = 0;
        float finalCheck2 = 0;
        String checkData = "SELECT * FROM student_grade WHERE studentNum = '" + studentGrade_studentNum.getText() + "'";
        connect = database.connectDb();
        studentData s;
        float finalResult = 0;
        try {
            prepare = connect.prepareStatement(checkData);
            result = prepare.executeQuery();
            if (result.next()) {
                finalCheck1 = result.getFloat("first_sem");
                finalCheck2 = result.getFloat("second_sem");
            }
                finalResult = (Float.parseFloat(studentGrade_firstSem.getText())
                        + Float.parseFloat(studentGrade_secondSem.getText()))/2;
            String updateData = "UPDATE student_grade SET " + " first_sem = '" + studentGrade_firstSem.getText()
                    + "', second_sem = '" + studentGrade_secondSem.getText() + "', final = '" + finalResult + "' WHERE studentNum = '"
                    + studentGrade_studentNum.getText() + "'";
            Alert alert;
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to UPDATE Student #" + studentGrade_studentNum.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get().equals(ButtonType.OK)) {
                statement = connect.createStatement();
                statement.executeUpdate(updateData);
                studentGradesShowListData();
            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void studentGradesClear() {
        studentGrade_studentNum.setText("");
        studentGrade_firstSem.setText(String.valueOf(""));
        studentGrade_secondSem.setText("");
    }
    public ObservableList<studentData> studentGradesListData() {
        ObservableList<studentData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM student_grade";
        connect = database.connectDb();
        try {
            studentData studentD;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while (result.next()) {
                studentD = new studentData(result.getInt("studentNum"),
                        result.getString("year"),
                        result.getString("course"),
                        result.getDouble("first_sem"),
                        result.getDouble("second_sem"),
                        result.getDouble("final"));
                listData.add(studentD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }
    private ObservableList<studentData> studentGradesList;
    public void studentGradesShowListData() {
        studentGradesList = studentGradesListData();
        studentGrade_col_studentNum.setCellValueFactory(new PropertyValueFactory<>("studentNum"));
        studentGrade_col_year.setCellValueFactory(new PropertyValueFactory<>("year"));
        studentGrade_col_course.setCellValueFactory(new PropertyValueFactory<>("course"));
        studentGrade_col_firstSem.setCellValueFactory(new PropertyValueFactory<>("firstSem"));
        studentGrade_col_secondSem.setCellValueFactory(new PropertyValueFactory<>("secondSem"));
        studentGrade_col_final.setCellValueFactory(new PropertyValueFactory<>("finals"));
        studentGrade_tableView.setEditable(true);
        studentGrade_tableView.setItems(studentGradesList);
    }
    public void studentGradesSelect() {
        studentData studentD = studentGrade_tableView.getSelectionModel().getSelectedItem();
        int num = studentGrade_tableView.getSelectionModel().getSelectedIndex();
        if ((num - 1) < -1) {
            return;
        }
        studentGrade_studentNum.setText(String.valueOf(studentD.getStudentNum()));
        studentGrade_firstSem.setText(String.valueOf(studentD.getFirstSem()));
        studentGrade_secondSem.setText(String.valueOf(studentD.getSecondSem()));
    }
    public void studentGradesSearch() {
        FilteredList<studentData> filter = new FilteredList<>(studentGradesList, e -> true);
        studentGrade_search.textProperty().addListener((Observable, oldValue, newValue) -> {
            filter.setPredicate(predicateStudentData -> {
                if (newValue.isEmpty() || newValue == null) {
                    return true;
                }
                String searchKey = newValue.toLowerCase();
                if (predicateStudentData.getStudentNum().toString().contains(searchKey)) {
                    return true;
                } else if (predicateStudentData.getYear().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateStudentData.getCourse().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateStudentData.getFirstSem().toString().contains(searchKey)) {
                    return true;
                } else if (predicateStudentData.getSecondSem().toString().contains(searchKey)) {
                    return true;
                } else if (predicateStudentData.getFinals().toString().contains(searchKey)) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<studentData> sortList = new SortedList<>(filter);
        sortList.comparatorProperty().bind(studentGrade_tableView.comparatorProperty());
        studentGrade_tableView.setItems(sortList);
    }
    private double x = 0;
    private double y = 0;
    public void logout() {
        try {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get().equals(ButtonType.OK)) {
                logout.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });
                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);
                    stage.setOpacity(.8);
                });
                root.setOnMouseReleased((MouseEvent event) -> {
                    stage.setOpacity(1);
                });
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(scene);
                stage.show();
            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void displayUsername(){
        username.setText(getData.username);}
    public void defaultNav(){
        home_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, cornflowerblue,black);");
    }
    public void switchForm(ActionEvent event) {
        if (event.getSource() == home_btn) {
            home_form.setVisible(true);
            addStudents_form.setVisible(false);
            availableCourse_form.setVisible(false);
            studentGrade_form.setVisible(false);
            home_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, cornflowerblue, black);");
            addStudents_btn.setStyle("-fx-background-color:transparent");
            availableCourse_btn.setStyle("-fx-background-color:transparent");
            studentGrade_btn.setStyle("-fx-background-color:transparent");
            homeDisplayTotalEnrolledStudents();
            homeDisplayMaleEnrolled();
            homeDisplayFemaleEnrolled();
        } else if (event.getSource() == addStudents_btn) {
            final ExecutorService executorService = Executors.newFixedThreadPool(5);
            home_form.setVisible(false);
            addStudents_form.setVisible(true);
            availableCourse_form.setVisible(false);
            studentGrade_form.setVisible(false);
            addStudents_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, cornflowerblue,black);");
            home_btn.setStyle("-fx-background-color:transparent");
            availableCourse_btn.setStyle("-fx-background-color:transparent");
            studentGrade_btn.setStyle("-fx-background-color:transparent");
            addStudentsShowListData();
            addStudentsYearList();
            addStudentsGenderList();
            addStudentsStatusList();
        } else if (event.getSource() == availableCourse_btn) {
            home_form.setVisible(false);
            addStudents_form.setVisible(false);
            availableCourse_form.setVisible(true);
            studentGrade_form.setVisible(false);
            availableCourse_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, cornflowerblue, black);");
            addStudents_btn.setStyle("-fx-background-color:transparent");
            home_btn.setStyle("-fx-background-color:transparent");
            studentGrade_btn.setStyle("-fx-background-color:transparent");
            availableCourseShowListData();
        } else if (event.getSource() == studentGrade_btn) {
            home_form.setVisible(false);
            addStudents_form.setVisible(false);
            availableCourse_form.setVisible(false);
            studentGrade_form.setVisible(true);
            studentGrade_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, cornflowerblue,black);");
            addStudents_btn.setStyle("-fx-background-color:transparent");
            availableCourse_btn.setStyle("-fx-background-color:transparent");
            home_btn.setStyle("-fx-background-color:transparent");
            studentGradesShowListData();
            studentGradesSearch();
        }
    }
    public void close() {
        System.exit(0);
    }
    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayUsername();
        defaultNav();
        homeDisplayTotalEnrolledStudents();
        homeDisplayMaleEnrolled();
        homeDisplayFemaleEnrolled();
        addStudentsShowListData();
        addStudents_year.getItems().addAll("First Year", "Second Year", "Third Year", "Fourth Year");
        addStudents_gender.getItems().addAll("Male", "Female", "Others");
        addStudents_status.getItems().addAll("Enrolled", "Not Enrolled", "Inactive");
        addStudentsCourseList();
        availableCourseShowListData();
        studentGradesShowListData();
    }
}
