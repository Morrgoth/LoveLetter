package bb.love_letter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Login extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("client.fxml"));
        LoginModel loginModel = new LoginModel();
        LoginController loginController = new LoginController(loginModel);
        LoginView loginView = new LoginView(loginModel, loginController);

        Scene scene = new Scene(loginView.asParent(), 300, 350);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}