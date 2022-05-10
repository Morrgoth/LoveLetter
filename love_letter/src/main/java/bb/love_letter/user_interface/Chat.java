package bb.love_letter.user_interface;

import bb.love_letter.user_interface.ChatController;
import bb.love_letter.user_interface.ChatModel;
import bb.love_letter.user_interface.ChatView;
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
        String css = this.getClass().getResource("Chat.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("chat");
        stage.setScene(scene);
        stage.show();

    }
}
