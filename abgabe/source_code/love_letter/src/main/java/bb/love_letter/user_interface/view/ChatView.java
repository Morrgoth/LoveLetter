package bb.love_letter.user_interface.view;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

/**
 *
 * @author Zeynab Baiani
 */
public class ChatView {
    private GridPane view;
    private TextField messageField;
    private Button sentbutton;
    private ListView<HBox> listView;
    public ChatView () {
        buildUI();
    }

    private void buildUI() {
        view = new GridPane();
        messageField = new TextField();
        messageField.setPromptText("Type your message here...");
        messageField.setId("messageField");
        sentbutton = new Button("Send");
        sentbutton.setId("sentButton");
        listView = new ListView<>();
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

    public Button getSentbutton() {
        return sentbutton;
    }

    public TextField getMessageField() {
        return messageField;
    }

    public ListView<HBox> getListView() {
        return listView;
    }
}
