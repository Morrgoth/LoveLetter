package bb.love_letter.user_interface.model;

import bb.love_letter.networking.data.ServerEvent;
import javafx.beans.property.*;

/**
 *
 * @author Bence Ament
 */
public class LoginModel {
    private final StringProperty ip = new SimpleStringProperty();
    private final IntegerProperty port = new SimpleIntegerProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty errorMessage = new SimpleStringProperty();
    private final BooleanProperty successfulLogin = new SimpleBooleanProperty(false);
    private ServerEvent loginConfirmation = null;
    public final StringProperty ipProperty() {
        return this.ip;
    }
    public final String getIp() {
        return this.ipProperty().get();
    }
    public final void setIp(String ip) {
        this.ipProperty().set(ip);
    }
    public final IntegerProperty portProperty() {
        return this.port;
    }
    public final int getPort() {
        return this.portProperty().get();
    }
    public final void setPort(int port) {
        this.portProperty().set(port);
    }
    public final StringProperty usernameProperty() {
        return this.username;
    }
    public final String getUsername() {
        return this.usernameProperty().get();
    }
    public final void setUsername(String username) {
        this.usernameProperty().set(username);
    }
    public final StringProperty errorMessageProperty() {
        return this.errorMessage;
    }
    public final String getErrorMessage() {
        return this.errorMessageProperty().get();
    }
    public final void setErrorMessage(String errorMessage) {
        this.errorMessageProperty().set(errorMessage);
    }
    public final BooleanProperty successfulLoginProperty() {
        return successfulLogin;
    }
    public final boolean getSuccessfulLogin() {
        return this.successfulLoginProperty().get();
    }
    public final void setSuccessfulLogin(boolean successfulLogin) {
        this.successfulLoginProperty().set(successfulLogin);
    }
    public ServerEvent getLoginConfirmation() {
        return loginConfirmation;
    }
    public void setLoginConfirmation(ServerEvent loginConfirmation) {
        this.loginConfirmation = loginConfirmation;
    }
}
