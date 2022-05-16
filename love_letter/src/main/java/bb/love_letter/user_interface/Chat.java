package bb.love_letter.user_interface;

import bb.love_letter.game.User;
import bb.love_letter.networking.NetworkConnection;
import bb.love_letter.networking.data.ChatMessage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Zeynab Baiani
 */
public class Chat extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        NetworkConnection.getInstance().init(null, null, null, new User("Server"));

        ChatModel chatModel = new ChatModel();
        ChatController chatController = new ChatController(chatModel);
        ChatView chatView = new ChatView(chatModel, chatController);
        chatModel.addVBox(new ChatMessageDisplay(new ChatMessage(new User("Server"), "lksfnslk sdf")).getHBox());
        chatModel.addVBox(new ChatMessageDisplay(new ChatMessage(new User("User1"), "lksfnslk sdf")).getHBox());

        Scene scene = new Scene(chatView.asParent(), 700, 500);
        String css = String.valueOf(this.getClass().getResource("/Chat.css"));
        System.out.println(css);
        scene.getStylesheets().add(getClass().getResource("/Chat.css").toExternalForm());
        stage.setTitle("chat");
        stage.setScene(scene);
        stage.show();

    }
}
