package bb.love_letter;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Chat extends Application {



    @Override
    public void start(Stage stage) {
        ChatModel loginModel = new ChatModel();
        ChatController loginController = new ChatController(loginModel);
        ChatView loginView = new ChatView(loginModel, loginController);
        Scene scene = new Scene(loginView.asParent(), 700, 500);
        stage.setTitle("Chat");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
