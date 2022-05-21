package bb.love_letter.user_interface.test;

import bb.love_letter.game.User;
import bb.love_letter.networking.client.NetworkConnection;
import bb.love_letter.networking.data.ChatMessage;
import bb.love_letter.user_interface.model.ChatModel;
import bb.love_letter.user_interface.model_view.ChatViewModel;
import bb.love_letter.user_interface.view.ChatMessageDisplayItem;
import bb.love_letter.user_interface.view.ChatView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Zeynab Baiani
 */
public class ChatViewTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        NetworkConnection.getInstance().init(null, null, null, new User("Server"));

        ChatModel chatModel = new ChatModel();
        ChatView chatView = new ChatView();
        ChatViewModel chatViewModel = new ChatViewModel(null, chatModel, chatView);
        chatModel.addVBox(new ChatMessageDisplayItem(new ChatMessage(new User("Server"), "lksfnslk sdf")).getHBox());
        chatModel.addVBox(new ChatMessageDisplayItem(new ChatMessage(new User("User1"), "lksfnslk sdf")).getHBox());

        Scene scene = new Scene(chatView.asParent(), 700, 500);
        String css = String.valueOf(this.getClass().getResource("/Chat.css"));
        System.out.println(css);
        scene.getStylesheets().add(getClass().getResource("/Chat.css").toExternalForm());
        stage.setTitle("chat");
        stage.setScene(scene);
        stage.show();

    }
}
