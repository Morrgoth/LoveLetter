package bb.love_letter.user_interface.view;

import bb.love_letter.user_interface.controller.ChatController;
import bb.love_letter.user_interface.model.ChatModel;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

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
    public ChatView (ChatModel model, ChatController controllert) {
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
        sentbutton.setId("sentButton");
        listView = new ListView<>(model.getHBoxObservableList());
        listView.setMouseTransparent(true);
        listView.setFocusTraversable(false);
        listView.getStyleClass().add("listView");
        GridPane chatBox = new GridPane();
        chatBox.getStyleClass().add("chatbox");
        chatBox.addColumn(1, messageField  );
        chatBox.addColumn(2, sentbutton);
        chatBox.setHgap(10);
        RowConstraints regRow = new RowConstraints();
        regRow.setVgrow(Priority.ALWAYS);
        view.getRowConstraints().add(regRow);

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

        messageField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    String message = messageField.getText();
                    model.setCurrentMessage(message);
                    messageField.setText("");
                }
            }
        });
    }
}