package bb.love_letter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatView {
    public ChatModel model;
    public ChatController controller;


    private GridPane view;
    private TextField messageField;
    private Button sentbutton;
    private ScrollPane scroll;
    private ListView<String> listView;


    public ChatView (ChatModel model, ChatController controller) {
        this.model = model;
        this.controller = controller;
        buildUI();
        setUpListeners();
        observeModelandUpdate();
    }

    private void buildUI() {
        view = new GridPane();
        scroll = new ScrollPane();
        messageField = new TextField();
        messageField.setPromptText("Type your message here...");
        sentbutton = new Button("Send");
        controller.addChatMessage(new ChatMessage(new User("Server"), "Welcome " + NetworkConnection.getInstance().getUser().getName() + "!"));
        listView = new ListView<>(model.getChatMessageStringObservableList());
        HBox chatBox = new HBox();
        HBox.setHgrow(messageField, Priority.ALWAYS);
        RowConstraints regRow = new RowConstraints();
        regRow.setVgrow(Priority.ALWAYS);
        view.getRowConstraints().add(regRow);
        scroll.setPannable(true);

        //scroll.setStyle("-fx-background: #FF0000");
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

    private void observeModelandUpdate() {
        model.getChatMessageObservableList().addListener(new ListChangeListener<ChatMessage>() {
            @Override
            public void onChanged(Change<? extends ChatMessage> change) {
                if (change.next()) {
                    for (ChatMessage chatMessage: change.getAddedSubList()) {
                        Label label = new Label(chatMessage.getMessage());
                        System.out.println("Msg: " + chatMessage.getMessage());
                    }
                }
            }
        });
    }

}
