package bb.love_letter.user_interface.view;

import bb.love_letter.user_interface.Client;
import bb.love_letter.user_interface.controller.LoginController;
import bb.love_letter.user_interface.model.LoginModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Bence Ament
 */
public class LoginView {
    private  GridPane view;
    public LoginModel model;
    public  LoginController controller;
    private  TextField ipField;
    private  TextField portField;
    private  TextField usernameField;
    private Label errorLabel;
    private  Button button;

    public  TextField getUsernameField() {
        return usernameField;
    }
    public  LoginController getController(){
        return controller;
    }

    public  TextField getIpField(){
        return ipField;
    }
    public  TextField getPortField(){
        return portField;
    }
    public Label getErrorLabel(){
        return errorLabel;
    }
    public  Button getButton(){
        return button;
    }
    public LoginView(LoginModel model) {
        this.model = model;
        buildUI();
    }


    private void buildUI() {
        view = new GridPane();
        Label title = new Label("Login");
        Separator separator = new Separator();
        ipField = new TextField();
        ipField.setPromptText("IP");
        portField = new TextField();
        portField.setPromptText("Port");
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        errorLabel = new Label();
        button = new Button("Login");

        view.setVgap(16);
        view.setAlignment(Pos.CENTER);
        GridPane.setHalignment(button, HPos.CENTER);
        GridPane.setHalignment(title, HPos.CENTER);

        view.addRow(0, title);
        view.addRow(1, separator);
        view.addRow(2, ipField);
        view.addRow(3, portField);
        view.addRow(4, usernameField);
        view.addRow(5, button);
        view.addRow(6, errorLabel);
    }




    public Parent asParent() {
        return view ;
    }
}
