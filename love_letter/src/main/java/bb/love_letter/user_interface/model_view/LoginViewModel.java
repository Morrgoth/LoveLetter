package bb.love_letter.user_interface.model_view;

import bb.love_letter.networking.data.ServerEvent;
import bb.love_letter.user_interface.model.LoginModel;
import bb.love_letter.user_interface.view.LoginView;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;

public class LoginViewModel {

    private LoginModel model;
    private LoginView view;

    public LoginViewModel(LoginModel loginModel, LoginView loginView) {
        model = loginModel;
        view = loginView;
    }

    private void observeModelandUpdate() {

        model.errorMessageProperty().addListener(new ChangeListener<String>() {//**************
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldVal, String newVal) {
                if (!newVal.equals(oldVal) && !newVal.equals("")) {
                    view.getErrorLabel().setText(newVal);//****************
                }
            }
        });
    }

}
