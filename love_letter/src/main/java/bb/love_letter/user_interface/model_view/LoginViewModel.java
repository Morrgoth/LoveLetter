package bb.love_letter.user_interface.model_view;

import bb.love_letter.user_interface.Client;
import bb.love_letter.user_interface.model.LoginModel;
import bb.love_letter.user_interface.view.LoginView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class LoginViewModel {

    private LoginModel model;
    private LoginView view;
    private Client client;

    public LoginViewModel(Client client, LoginModel loginModel, LoginView loginView) {
        this.client=client;
        model = loginModel;
        view = loginView;
        setupListeners();
        observeModelandUpdate();
    }

    private void setupListeners() {
        view.getButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                submitLoginForm();
            }
        });

        view.getIpField().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    submitLoginForm();
                }
            }
        });

        view.getPortField().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    submitLoginForm();
                }
            }
        });

        view.getUsernameField().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    submitLoginForm();
                }
            }
        });
    }


    private void observeModelandUpdate() {
        model.errorMessageProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldVal, String newVal) {
                if (!newVal.equals(oldVal) && !newVal.equals("")) {
                    view.getErrorLabel().setText(newVal);
                }
            }
        });
    }

    public void requestLogin(String ip, int port, String username) {
        model.setIp(ip);
        model.setPort(port);
        model.setUsername(username);
        client.login(ip, port, username);
    }

    private void submitLoginForm() {
        if (view.getIpField().getText() == null || view.getIpField().getText().trim().isEmpty()) {
            model.setErrorMessage("Error: Missing IP address!");
        } else if (view.getIpField().getText() == null || view.getPortField().getText().trim().isEmpty()) {
            model.setErrorMessage("Error: Missing port number!");
        } else if (view.getUsernameField().getText() == null || view.getUsernameField().getText().trim().isEmpty()) {
            model.setErrorMessage("Error: Missing username!");
        } else {
            String ip = view.getIpField().getText();
            int port = Integer.parseInt(view.getPortField().getText());
            String username = view.getUsernameField().getText();
            requestLogin(ip, port, username);
        }
    }


}
