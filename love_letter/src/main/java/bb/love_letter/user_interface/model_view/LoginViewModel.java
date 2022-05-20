package bb.love_letter.user_interface.model_view;

import bb.love_letter.networking.data.ServerEvent;
import bb.love_letter.user_interface.model.LoginModel;
import bb.love_letter.user_interface.view.LoginView;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class LoginViewModel {

    private LoginModel model;
    private LoginView view;

    public LoginViewModel(LoginModel loginModel, LoginView loginView) {
        model = loginModel;
        view = loginView;
    }

    public void start() {
        setupListeners();
        observeModelandUpdate();
    }

    private void setupListeners() {
        view.getButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String ip = view.getIpField().getText();
                int port = Integer.parseInt(view.getPortField().getText());
                String username = view.getUsernameField().getText();
                model.setIp(ip);
                model.setPort(port);
                model.setUsername(username);
                view.getController().requestLogin(ip, port, username);
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

}
