package bb.love_letter.user_interface;

import bb.love_letter.game.User;
import bb.love_letter.networking.data.ChatMessage;
import bb.love_letter.networking.NetworkConnection;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

/**
 *
 * @author Zeynab Baiani
 */
public class ChatView {
    public ChatModel model;
    public ChatController controller;


    private GridPane view;
    private TextField messageField;
    private Button sentbutton;
    private ListView<HBox> listView;


    public ChatView (ChatModel model, ChatController controller) {
        this.model = model;
        this.controller = controller;
        buildUI();
        setUpListeners();
        //observeModelandUpdate();
    }

    private void buildUI() {
        view = new GridPane();
        messageField = new TextField();
        messageField.setPromptText("Type your message here...");
        messageField.setId("messageField");
        sentbutton = new Button("Send");
        controller.addChatMessage(new ChatMessage(new User("Server"), "Welcome " + NetworkConnection.getInstance().getUser().getName() + "!"));
        listView = new ListView<>(model.getvBoxObservableList());
        //listView.setStyle("-fx-background-color: transparent;");
        HBox chatBox = new HBox();
        HBox.setHgrow(messageField, Priority.ALWAYS);
        RowConstraints regRow = new RowConstraints();
        regRow.setVgrow(Priority.ALWAYS);
        view.getRowConstraints().add(regRow);

        chatBox.getChildren().addAll(messageField, sentbutton);
        messageField.setPrefWidth(600);
        sentbutton.setPrefWidth(100);
        view.addRow(0,listView);
        view.addRow(1, chatBox);
    }

    public Parent asParent() {
        return view ;
    }

    private void setUpListeners() {
        sentbutton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String message = messageField.getText();
                model.setCurrentMessage(message);
                messageField.setText("");
            }
        });
    }
}
