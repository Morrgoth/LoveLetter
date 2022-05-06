package bb.love_letter;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ChatView {
    public ChatModel model;
    public ChatController controller;


    private GridPane view;

    private TextField messageField;
    private Label sumLabel;
    private Button button;



    public ChatView (ChatModel model, ChatController controller) {
        this.model = model;
        this.controller = controller;
        buildUI();
    }

    private void buildUI() {
        view = new GridPane();

        messageField = new TextField();
        messageField.setPromptText("Type your message here...");
        button = new Button("Send");

        view.setVgap(8);

        view.addRow(3, new Label(), messageField);

        view.addRow(3, button);

        view.addColumn(0);
    }

    public Parent asParent() {
        return view ;
    }

}
