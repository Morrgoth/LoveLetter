package bb.love_letter;

import com.google.gson.Gson;
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

public class ChatView {
    public ChatModel model;
    public ChatController controller;


    private GridPane view;
    private TextField messageField;
    private Button sentbutton;
    private ScrollPane scroll;
    private ListView<ChatMessage> listView;


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
        controller.addChatMessage(new ChatMessage(new User("name"), "Hallo"));
        listView = new ListView<>(model.getChatMessageObservableList());
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
                ChatMessage chatMessage = new ChatMessage(NetworkConnection.getInstance().getUser(), message);
                Envelope envelope = new Envelope(chatMessage, Envelope.TypeEnum.CHATMESSAGE);
                Gson gson = new Gson();
                String json = gson.toJson(envelope);
                try {
                    NetworkConnection.getInstance().getOutputStream().write(json);
                    messageField.setText("");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void observeModelandUpdate() {
        model.getChatMessageObservableList().addListener(new ListChangeListener<ChatMessage>() {
            @Override
            public void onChanged(Change<? extends ChatMessage> change) {
                if (change.wasAdded()) {
                    for (ChatMessage chatMessage: change.getAddedSubList()) {
                        Label label = new Label(chatMessage.getMessage());
                        //scroll.get
                    }
                }
            }
        });
    }

}
