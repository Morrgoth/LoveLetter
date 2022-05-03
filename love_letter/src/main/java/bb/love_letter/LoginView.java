package bb.love_letter;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class LoginView {
    private GridPane view;
    public LoginModel model;
    public LoginController controller;
    private TextField xField;
    private TextField yField;
    private Label sumLabel;
    private Button button;
    public LoginView(LoginModel model, LoginController controller) {
        this.model = model;
        this.controller = controller;
        buildUI();
    }

    private void buildUI() {
        view = new GridPane();
        xField = new TextField();
        yField = new TextField();
        sumLabel = new Label();
        button = new Button("Add");

        view.setVgap(8);
        view.addRow(0, new Label("X:"), xField);
        view.addRow(1, new Label("Y:"), yField);
        view.addRow(2, new Label("Sum:"), sumLabel);
        view.addRow(3, button);
    }

    public Parent asParent() {
        return view ;
    }
}
