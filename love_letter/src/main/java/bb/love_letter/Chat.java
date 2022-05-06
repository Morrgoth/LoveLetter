package bb.love_letter;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Chat extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        ChatModel chatModel = new ChatModel();
        ChatController chatController = new ChatController(chatModel);
        ChatView chatView = new ChatView(chatModel, chatController);

        Scene scene = new Scene(chatView.asParent(), 700, 500);
        stage.setTitle("chat");
        stage.setScene(scene);
        stage.show();

    }
}
