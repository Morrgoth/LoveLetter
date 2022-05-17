package bb.love_letter.user_interface.controller;

import bb.love_letter.user_interface.Client;
import bb.love_letter.user_interface.model.LoginModel;

/**
 *
 * @author Bence Ament
 */
public class LoginController {
    public LoginModel model;
    private Client client;

    public LoginController (LoginModel model, Client client) {
        this.model = model;
        this.client = client;
    }

    public void requestLogin(String ip, int port, String username) {
        model.setIp(ip);
        model.setPort(port);
        model.setUsername(username);
        client.login(ip, port, username);
    }

    public LoginModel getModel() {
        return model;
    }
}