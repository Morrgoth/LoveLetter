package bb.love_letter.user_interface;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
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