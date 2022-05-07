package bb.love_letter;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class ChatView {
    public ChatModel model;
    public ChatController controller;


    private GridPane view;
    private TextField messageField;
    private Button sentbutton;
    private ScrollPane scroll;



    public ChatView (ChatModel model, ChatController controller) {
        this.model = model;
        this.controller = controller;
        buildUI();
    }

    private void buildUI() {
        view = new GridPane();
        scroll = new ScrollPane();
        messageField = new TextField();
        messageField.setPromptText("Type your message here...");
        sentbutton = new Button("Send");
        HBox chatBox = new HBox();
        HBox.setHgrow(messageField, Priority.ALWAYS);
        RowConstraints regRow = new RowConstraints();
        regRow.setVgrow(Priority.ALWAYS);
        view.getRowConstraints().add(regRow);

        //scroll.setStyle("-fx-background: #FF0000");
        chatBox.getChildren().addAll(messageField, sentbutton);
        messageField.setPrefWidth(600);
        sentbutton.setPrefWidth(100);
        view.addRow(0,scroll);
        view.addRow(1, chatBox);








    }

    public Parent asParent() {
        return view ;
    }

}
